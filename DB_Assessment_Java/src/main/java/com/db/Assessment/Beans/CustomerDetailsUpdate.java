package com.db.Assessment.Beans;

import lombok.Data;

import java.math.BigDecimal;


public @Data class CustomerDetailsUpdate {

    private final String customerId;
    private final BigDecimal amount;

}
