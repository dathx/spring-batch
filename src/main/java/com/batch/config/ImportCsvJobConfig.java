package com.batch.config;

import com.batch.entity.Person;
import com.batch.listener.ImportCsvListener;
import com.batch.patitioner.GenericPartitioner;
import com.batch.processor.PersonProcessor;
import com.batch.repository.PersonRepository;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ImportCsvJobConfig {
    private final String JOB_NAME = "importPersons";

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private ListableJobLocator jobLocator;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ImportCsvListener importCsvListener;

    @Autowired
    private FlatFileItemReader<Person> importCsvReader;

    @Autowired
    private GenericPartitioner genericPartitioner;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private TaskExecutorPartitionHandler dataImportTaskExecutorPartitionHandler;

    @Autowired
    private SimpleAsyncTaskExecutor simpleAsyncTaskExecutor;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

//	@Autowired
//	private ImportCsvSplitterListener importCsvSplitterListener;


    @Bean
    public Job importPersons(JobRepository jobRepository) {
        return new JobBuilder(JOB_NAME, jobRepository).incrementer(new RunIdIncrementer()).listener(importCsvListener)
                .start(step(jobRepository, transactionManager)).build();
    }

    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("csv-import-step", jobRepository).partitioner("csv-import-step", genericPartitioner)
                .partitionHandler(dataImportTaskExecutorPartitionHandler).build();
    }

    @Bean
    @StepScope
    TaskExecutorPartitionHandler dataImportTaskExecutorPartitionHandler() {
        TaskExecutorPartitionHandler partitionHandler = new TaskExecutorPartitionHandler();
        partitionHandler.setTaskExecutor(simpleAsyncTaskExecutor);
        partitionHandler.setStep(dataImportStep());
        partitionHandler.setGridSize(2);
        return partitionHandler;

    }

    private Step dataImportStep() {
        return new StepBuilder(JOB_NAME, jobRepository).<Person, Person>chunk(1000, platformTransactionManager)
                .reader(importCsvReader).processor(new PersonProcessor()).writer(writer()).build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
                .resource(new ClassPathResource("/data/people-10000.csv")).linesToSkip(1).lineMapper(lineMapper())
                .targetType(Person.class).build();
    }

    private LineMapper<Person> lineMapper() {
        DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "userId", "firstName", "lastName", "gender", "email", "phone", "dateOfBirth",
                "jobTitle");

        BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Person.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    RepositoryItemWriter<Person> writer() {
        RepositoryItemWriter<Person> writer = new RepositoryItemWriter<>();
        writer.setRepository(personRepository);
        writer.setMethodName("save");
        return writer;
    }

    // @Scheduled(cron = "0/30 * * ? * *") // chay moi 30s
    public void runScheduler() {
        try {
            JobParameters jobParameters = new JobParametersBuilder().addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(jobLocator.getJob(JOB_NAME), jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException
                 | JobParametersInvalidException | JobRestartException | NoSuchJobException e) {
        }
    }


}
