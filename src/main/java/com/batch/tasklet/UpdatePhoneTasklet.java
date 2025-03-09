package com.batch.tasklet;

import java.util.List;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.batch.entity.Person;
import com.batch.repository.PersonRepository;

@Component
public class UpdatePhoneTasklet implements Tasklet {

	@Autowired
	private PersonRepository personRepository;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		List<Person> list = personRepository.findAll();
		if (!CollectionUtils.isEmpty(list)) {
			list.stream().forEach(per -> {
				if (per.getGender().equals("Male")) {
					personRepository.delete(per);
				}
			});
			// personRepository.saveAll(list);
		}
		return null;
	}

}
