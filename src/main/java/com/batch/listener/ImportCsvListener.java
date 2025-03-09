package com.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ImportCsvListener implements JobExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(ImportCsvListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("Before ImportCsvListener");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("After ImportCsvListener");
	}
}
