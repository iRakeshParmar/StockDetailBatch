package com.spring.stockdetailbatch.batch;

import com.spring.stockdetailbatch.entity.JPA.StockDetail;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

@Component
public class StockDetailProcessor implements ItemProcessor<StockDetail, StockDetail> {


    @Override
    public StockDetail process(StockDetail item) throws Exception {
       // item.setSymbol(fileName);
        return item;
    }

}
