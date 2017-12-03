package com.db.Assessment.Beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;


public @Data class CustomerDetails {

    @NotNull
    @NotEmpty
    private final String customerId;

    @NotNull
    @Min(value = 0, message = "Balance must not be less than zero.")
    private BigDecimal customerbalance;

    public CustomerDetails(String customerId) {
        this.customerId = customerId;
        this.customerbalance = BigDecimal.ZERO;
    }

    @JsonCreator
    public CustomerDetails(@JsonProperty("customerId") String customerId,
                   @JsonProperty("customerbalance") BigDecimal customerbalance) {
        this.customerId = customerId;
        this.customerbalance = customerbalance;
    }
}
