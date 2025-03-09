//package com.batch.config;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.ListableJobLocator;
//import org.springframework.batch.core.configuration.annotation.StepScope;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.launch.NoSuchJobException;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.data.RepositoryItemWriter;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import com.batch.entity.Person;
//import com.batch.listener.ImportCsvListener;
//import com.batch.processor.PersonProcessor;
//import com.batch.repository.PersonRepository;
//
//@Configuration
//public class ImportOld {
//	private final String JOB_NAME = "importPersons";
//
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private ListableJobLocator jobLocator;
//    
//	@Autowired
//	private PersonRepository personRepository;
//
//	@Autowired
//	private ImportCsvListener importCsvListener;
//	
//	@Autowired
//	private  FlatFileItemReader<Person> importCsvReader;
//
//	@Autowired
//	private PlatformTransactionManager transactionManager;
//
//	@Bean
//	public Job importPersons(JobRepository jobRepository) {
//		return new JobBuilder(JOB_NAME, jobRepository).incrementer(new RunIdIncrementer()).listener(importCsvListener)
//				.start(step(jobRepository, transactionManager)).build();
//	}
//
//	public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//		return new StepBuilder("csv-import-step", jobRepository).<Person, Person>chunk(10, transactionManager)
//				.reader(importCsvReader).processor(new PersonProcessor()).writer(writer()).build();
//	}
//    @Bean
//    @StepScope
//	public FlatFileItemReader<Person> reader() {
//		return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
//				.resource(new ClassPathResource("/data/people-10000.csv"))
//				.linesToSkip(1)
//				.lineMapper(lineMapper())
//				.targetType(Person.class)
//				.build();
//	}
//
//	private LineMapper<Person> lineMapper() {
//		DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();
//
//		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//		lineTokenizer.setDelimiter(",");
//		lineTokenizer.setStrict(false);
//		lineTokenizer.setNames("id", "userId", "firstName", "lastName", "gender", "email", "phone", "dateOfBirth", "jobTitle");
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
//	//@Scheduled(cron = "0/30 * * ? * *") // chay moi 30s
//	public void runScheduler() {
//		try {
//			JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
//					.toJobParameters();
//			jobLauncher.run(jobLocator.getJob(JOB_NAME), jobParameters);
//		} catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException
//				| JobParametersInvalidException | JobRestartException | NoSuchJobException e) {
//		}
//	}
//}
