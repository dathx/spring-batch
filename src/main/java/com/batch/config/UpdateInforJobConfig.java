package com.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.batch.listener.UpdateInforListener;
import com.batch.tasklet.UpdateJobTitleTasklet;
import com.batch.tasklet.UpdatePhoneTasklet;

@Configuration
public class UpdateInforJobConfig {
	private final String JOB_NAME = "updateInforJob";

	@Autowired
	private UpdateInforListener updateInforListener;

	@Autowired
	private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private UpdateJobTitleTasklet updateJobTitleTasklet;

	@Autowired
	private UpdatePhoneTasklet updatePhoneTasklet;

	@Bean
	public Job updateInforJob(JobRepository jobRepository) {
		return new JobBuilder(JOB_NAME, jobRepository).incrementer(new RunIdIncrementer()).listener(updateInforListener)
				.start(splitFlow()).build().build();
	}

	private Flow splitFlow() {
		return (Flow) new FlowBuilder<>("splitFlow").split(simpleAsyncTaskExecutor)
				.add(updateJobTitleFlow(), updatePhoneFlow()).build();
	}

	private Flow updateJobTitleFlow() {
		return (Flow) new FlowBuilder<>("updateJobTitleFlow").from(updateJobTitleStep()).build();
	}

	private Step updateJobTitleStep() {
		return new StepBuilder("updateJobTitleStep", jobRepository)
				.tasklet(updateJobTitleTasklet, platformTransactionManager).build();
	}

	private Flow updatePhoneFlow() {
		return (Flow) new FlowBuilder<>("updatePhoneFlow").from(updatePhoneStep()).build();
	}

	private Step updatePhoneStep() {
		return new StepBuilder("updatePhoneStep", jobRepository).tasklet(updatePhoneTasklet, platformTransactionManager)
				.build();
	}

}
