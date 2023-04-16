package com.spring.stockdetailbatch.entity.JPA;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.Instant;

@Entity
@Table(name = "StockDetail")
public class StockDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id", nullable = false)
    private Long id;

    @Size(max = 50)
    @Column(name = "Symbol", length = 50)
    private String symbol;

    @Column(name = "\"Date\"")
    private Date date;

    @Column(name = "\"Open\"", precision = 18, scale = 8)
    private BigDecimal open;

    @Column(name = "High", precision = 18, scale = 8)
    private BigDecimal high;

    @Column(name = "Low", precision = 18, scale = 8)
    private BigDecimal low;

    @Column(name = "\"Close\"", precision = 18, scale = 8)
    private BigDecimal close;

    @Column(name = "\"Adj Close\"", precision = 18, scale = 8)
    private BigDecimal adjClose;

    @Column(name = "Volume")
    private Long volume;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getAdjClose() {
        return adjClose;
    }

   public void setAdjClose(BigDecimal adjClose) {
        this.adjClose = adjClose;
    }

    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

}