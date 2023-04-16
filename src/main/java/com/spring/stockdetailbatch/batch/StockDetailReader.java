package com.spring.stockdetailbatch.batch;

import com.spring.stockdetailbatch.entity.JPA.StockDetail;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



@Component
public class StockDetailReader implements ItemReader<StockDetail> {

    @Override
    public StockDetail read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        FlatFileItemReader<StockDetail> reader = new FlatFileItemReader<StockDetail>();
        return reader.read();
    }

}
