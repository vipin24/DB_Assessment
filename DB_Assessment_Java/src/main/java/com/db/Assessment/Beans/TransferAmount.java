package com.db.Assessment.Beans;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public @Data class TransferAmount {

    @NotNull
    @NotEmpty
    private String customerFromId;

    @NotNull
    @NotEmpty
    private String customerToId;

    @NotNull
    @Min(value = 1, message = "Transfer amount must greate than zero")
    private BigDecimal amount;

    @JsonCreator
    public TransferAmount(@JsonProperty("customerFromId") String accountFromId,
                    @JsonProperty("customerToId") String accountToId,
                    @JsonProperty("amount") BigDecimal amount){
        this.customerFromId = accountFromId;
        this.customerToId = accountToId;
        this.amount = amount;
    }

}
