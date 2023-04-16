package com.spring.stockdetailbatch.batch;

import com.spring.stockdetailbatch.entity.JPA.StockDetail;
import com.spring.stockdetailbatch.repository.StockDetailRepository;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockDetailWriter implements ItemWriter<StockDetail> {

    @Autowired
    StockDetailRepository stockMainRepository;
    @Override
    public void write(Chunk<? extends StockDetail> chunk) throws Exception {
        this.stockMainRepository.saveAll(chunk.getItems());
    }
}
