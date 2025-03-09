package com.batch.processor;

import com.batch.entity.Person;
import org.springframework.batch.item.ItemProcessor;

public class PersonProcessor implements ItemProcessor<Person, Person> {

	@Override
	public Person process(Person person) throws Exception {

		if (person.getGender().equals("Male")) {
			person.setFirstName(person.getFirstName().toUpperCase());
			person.setLastName(person.getLastName().toUpperCase());
		}
		return person;
	}
}