package com.spring.stockdetailbatch.batch;

import com.spring.stockdetailbatch.entity.JPA.StockDetail;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;


@Configuration
public class BatchJobStepConfig {


    @Autowired
    StockDetailReader stockDetailReader;

    @Autowired
    StockDetailProcessor stockDetailProcessor;

    @Autowired
    StockDetailWriter stockDetailWriter;


    @Bean
    public Step StockMasterStep(JobRepository jobRepository, PlatformTransactionManager transactionManager)
            throws Exception {
        return new StepBuilder("StockMasterStep", jobRepository)
                .partitioner("StockSlaveStep", customPartitioner())
                .step(StockSlaveStep(jobRepository, transactionManager))
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step StockSlaveStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception {
        return new StepBuilder("StockSlaveStep", jobRepository)
                .<StockDetail, StockDetail>chunk(1000, transactionManager)
                .reader(reader(null))
                .processor(stockDetailProcessor)
                .writer(stockDetailWriter)
                .build();
    }


    @Bean
    public Partitioner customPartitioner() {

        return part -> {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = null;
            try {
                resources = resolver.getResources("file:[Your file dir]/*.csv");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Map<String, ExecutionContext> map = new HashMap<>(16);
            for (int iCnt = 0; iCnt < resources.length; iCnt++) {
                ExecutionContext context = new ExecutionContext();
                context.putString("fileName", resources[iCnt].getFilename());
                map.put("partition" + resources[iCnt].getFilename(), context);
            }
            return map;
        };

    }


    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(64);
        taskExecutor.setCorePoolSize(64);
        taskExecutor.setQueueCapacity(64);
        taskExecutor.setAllowCoreThreadTimeOut(true);
        taskExecutor.setKeepAliveSeconds(15);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return taskExecutor;
    }


    @Bean
    @StepScope
    public FlatFileItemReader<StockDetail> reader(@Value("#{stepExecutionContext['fileName']}") String fileName) throws Exception {
        FlatFileItemReader<StockDetail> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("[Your file dir]\\" + fileName));
        reader.setLinesToSkip(1);
        reader.setLineMapper(new DefaultLineMapper<StockDetail>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("date", "open", "high", "low", "close", "adjClose", "volume");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<StockDetail>() {{
                setConversionService(StockDetailConversionService());
                setDistanceLimit(0);
                setTargetType(StockDetail.class);
            }});
        }});

        return reader;
    }

    public ConversionService StockDetailConversionService() {
        DefaultConversionService cs = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(cs);
        cs.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String text) {
                return LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
        });
        cs.addConverter(new Converter<String, BigDecimal>() {
            @Override
            public BigDecimal convert(String text) {

                return new BigDecimal(text, MathContext.DECIMAL128).setScale(7, RoundingMode.HALF_UP);
            }
        });
        cs.addConverter(new Converter<String, Long>() {
            @Override
            public Long convert(String text) {
                return Long.parseLong(text);
            }
        });
        return cs;
    }


}

