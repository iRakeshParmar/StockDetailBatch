package com.spring.stockdetailbatch.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.PropertyVetoException;

@Configuration
@EnableBatchProcessing
@EnableJpaRepositories(basePackages = "com.spring.stockdetailbatch.repository")
public class BatchJobConfig {

    @Autowired
    BatchJobStepConfig batchJobStepConfig;

    @Bean
    public Job StockBatchProcessJob(JobRepository jobRepository,PlatformTransactionManager transactionManager) throws PropertyVetoException,Exception {
        return new JobBuilder("StockBatchProcessJob", jobRepository)
                .start(batchJobStepConfig.StockMasterStep(jobRepository,transactionManager))
                .build();
    }


    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception{
        TaskExecutorJobLauncher jLauncher = new TaskExecutorJobLauncher();
        jLauncher.setJobRepository(jobRepository);
        return jLauncher;
    }

}
