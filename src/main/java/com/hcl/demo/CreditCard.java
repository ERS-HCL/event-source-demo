package com.hcl.demo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@ToString
@Getter
public class CreditCard {
    private UUID uuid;

    public CreditCard(UUID uuid){
        this.uuid = uuid;
    }

    void assignLimit(BigDecimal amount) {

    }

    void withdraw(BigDecimal amount) {

    }

    void repay(BigDecimal amount) {

    }

    public BigDecimal availableLimit() {
        return  BigDecimal.ZERO;
    }


}
