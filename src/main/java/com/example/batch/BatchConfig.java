package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Job -> Step -> (Reader -> Processor -> Writer), processed in chunks.
 */
@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(new ClassPathResource("people.csv"))
                .linesToSkip(1)
                .delimited()
                .names("firstName", "lastName", "email")
                .targetType(Person.class)
                .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
                .sql("INSERT INTO person (first_name, last_name, email) VALUES (:firstName, :lastName, :email)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step importPersonStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 FlatFileItemReader<Person> reader,
                                 PersonItemProcessor processor,
                                 JdbcBatchItemWriter<Person> writer) {
        return new StepBuilder("importPersonStep", jobRepository)
                .<Person, Person>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importPersonJob(JobRepository jobRepository, Step importPersonStep) {
        return new JobBuilder("importPersonJob", jobRepository)
                .start(importPersonStep)
                .build();
    }
}
