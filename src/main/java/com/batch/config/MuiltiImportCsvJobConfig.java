//package com.batch.config;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.data.RepositoryItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.MultiResourceItemReader;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.batch.entity.Person;
//import com.batch.processor.PersonProcessor;
//import com.batch.repository.PersonRepository;
//
//@Configuration
//public class MuiltiImportCsvJobConfig {
//	private final String JOB_NAME = "muiltiImportCsvJob";
//
//	@Autowired
//	private PersonRepository personRepository;
//
//	@Autowired
//	private FlatFileItemReader<Person> muiltiImportCsvReader;
//
//	@Autowired
//	private PlatformTransactionManager transactionManager;
//
//	@Value("${files_path}")
//	private Resource[] resources;
//
//	@Bean
//	public Job muiltiImportCsvJob(JobRepository jobRepository) {
//		return new JobBuilder(JOB_NAME, jobRepository).incrementer(new RunIdIncrementer())
//				.start(muiltiImportCsv(jobRepository, transactionManager)).build();
//	}
//
//	public Step muiltiImportCsv(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//		return new StepBuilder("muilti-csv-import-step", jobRepository).<Person, Person>chunk(10, transactionManager)
//				.reader(multiResourceItemReader())
//				.processor(new PersonProcessor())
//				.writer(writer())
//				.build();
//	}
//
//	@Bean
//	@StepScope
//	public FlatFileItemReader<Person> muiltiImportCsvReader() {
//		return new FlatFileItemReaderBuilder<Person>().name("multiPersonItemReader")
//				.linesToSkip(1)
//				.lineMapper(lineMapper())
//				.targetType(Person.class)
//				.build();
//	}
//
//	public MultiResourceItemReader<Person> multiResourceItemReader() {
//		return new MultiResourceItemReaderBuilder<Person>()
//				.name("multi reader")
//				.resources(resources)
//				.delegate(muiltiImportCsvReader)
//				.build();
//	}
//
//	private LineMapper<Person> lineMapper() {
//		DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
//
//		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//		lineTokenizer.setDelimiter(",");
//		lineTokenizer.setStrict(false);
//		lineTokenizer.setNames("id", "userId", "firstName", "lastName", "gender", "email", "phone", "dateOfBirth",
//				"jobTitle");
//
//		BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//		fieldSetMapper.setTargetType(Person.class);
//		lineMapper.setLineTokenizer(lineTokenizer);
//		lineMapper.setFieldSetMapper(fieldSetMapper);
//
//		return lineMapper;
//	}
//
//	RepositoryItemWriter<Person> writer() {
//		RepositoryItemWriter<Person> writer = new RepositoryItemWriter<>();
//		writer.setRepository(personRepository);
//		writer.setMethodName("save");
//		return writer;
//	}
//
//}
