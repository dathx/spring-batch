package com.batch.writer;

import org.springframework.batch.item.database.JpaItemWriter;

import com.batch.entity.Person;

import jakarta.persistence.EntityManagerFactory;

public class PersonItemWriter extends JpaItemWriter<Person> {

	public PersonItemWriter(EntityManagerFactory entityManagerFactory) {
		setEntityManagerFactory(entityManagerFactory);
	}

}
