package com.batch.controller;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
public class JobController {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private ListableJobLocator jobLocator;

	@PostMapping("/{jobId}")
	public String jobLauncher(@PathVariable(value = "jobId", required = true) String jobId) throws NoSuchJobException {

		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		try {
			JobExecution jobExecution = jobLauncher.run(jobLocator.getJob(jobId), jobParameters);
			return jobExecution.getStatus().toString();
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
			return e.getMessage();
		}

	}

	@PostMapping("/importPersons")
	public String importPersons() throws NoSuchJobException {
		return jobLauncher("importPersons");
	}

}
