package com.spring.stockdetailbatch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class StockDetailBatchApplication {

    public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException  {
        ApplicationContext appCtx = SpringApplication.run(StockDetailBatchApplication.class, args);

        JobLauncher joblauncher = appCtx.getBean(JobLauncher.class);
        Job StockJob = appCtx.getBean("StockBatchProcessJob",Job.class);
        LocalDateTime startTime = LocalDateTime.now();
        JobExecution jobExecution = joblauncher.run(StockJob, new JobParameters());
        System.out.println(jobExecution.getStatus());
        LocalDateTime EndTimeTime = LocalDateTime.now();
        var hours = EndTimeTime.getHour() - startTime.getHour();
        var minutes = EndTimeTime.getMinute() - startTime.getMinute();
        var seconds = EndTimeTime.getSecond() - startTime.getSecond();
        System.out.println("Start: " + startTime + " End: " + EndTimeTime + " Time Taken: " + hours + " hours " + minutes + " minutes " + seconds + " seconds");
    }

}
