package com.batch.processor;

import org.springframework.batch.item.ItemProcessor;

import com.batch.entity.Person;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

	@Override
	public Person process(Person item) throws Exception {
		System.out.println("Item Process!");
		return item;
	}

}
