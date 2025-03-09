//package com.batch.config;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class PurgeDataJobConfig {
//	
//	private final String JOB_NAME = "purgeData";
//	
//	@Bean
//	Job purgeData(JobRepository jobRepository) {
//		return new JobBuilder(JOB_NAME, jobRepository).incrementer(new RunIdIncrementer();
//		
//	}
//
//}
