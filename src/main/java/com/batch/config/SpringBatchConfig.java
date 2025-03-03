package com.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batch.entity.Person;
import com.batch.processor.PersonItemProcessor;
import com.batch.reader.CsvItemReader;
import com.batch.writer.PersonItemWriter;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	@Bean
	public CsvItemReader csvItemReader() {
		return new CsvItemReader();
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}

	@Bean
	public PersonItemWriter writer(EntityManagerFactory entityManagerFactory) {
		return new PersonItemWriter(entityManagerFactory);
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, CsvItemReader reader, PersonItemProcessor processor,
			PersonItemWriter writer) {
		return stepBuilderFactory.get("step1").<Person, Person>chunk(10).reader(reader).processor(processor)
				.writer(writer).build();
	}

	@Bean
	public Job importUserJob(JobBuilderFactory jobBuilderFactory, Step step1) {
		return jobBuilderFactory.get("importUserJob").start(step1).build();
	}
}
