package com.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.batch.entity.Person;
import com.batch.repository.PersonRepository;

@Component
public class UpdateInforListener implements JobExecutionListener {
	@Autowired
	private PersonRepository personRepository;

	private static final Logger log = LoggerFactory.getLogger(UpdateInforListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		List<Person> listPersons = personRepository.findAll();
		if (listPersons == null || listPersons.isEmpty()) {
			jobExecution.setStatus(BatchStatus.STOPPING);
		}
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		log.info("After UpdateInforListener");
	}
}
