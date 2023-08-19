package com.batch.batchdemo.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfig {


    private final JobRepository jobRepository;


    public BatchConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }



    @Bean
    public JobBuilder jobBuilder() {
        return new JobBuilder("init-job", jobRepository);
    }


    @Bean
    public StepBuilder stepBuilder() {
        return new StepBuilder("init-step",jobRepository);

    }



}
