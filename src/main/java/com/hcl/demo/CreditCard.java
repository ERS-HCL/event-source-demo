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
    private BigDecimal limit;
    private BigDecimal used=BigDecimal.ZERO;

    public CreditCard(UUID uuid){
        this.uuid = uuid;
    }

    void assignLimit(BigDecimal amount) {
        if (limitAlreadyAssigned()){
            throw new IllegalStateException();
        }
        this.limit = amount;
    }

    private boolean limitAlreadyAssigned() {
        if (limit != null) return true;
        else return false;
    }

    void withdraw(BigDecimal amount) {
        if (notEnoughMoneyToWithdraw(amount)) throw new IllegalStateException();
        this.used = used.add(amount);
    }

    private boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
        return availableLimit().compareTo((amount))<0;
    }

    void repay(BigDecimal amount) {
        used = used.subtract(amount);
    }

    public BigDecimal availableLimit() {
        return limit.subtract(used);
    }


}
