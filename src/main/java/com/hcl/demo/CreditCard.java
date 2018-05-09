package com.hcl.demo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;

@NoArgsConstructor
@ToString
@Getter
public class CreditCard {
    private UUID uuid;
    private BigDecimal limit;
    private BigDecimal used=BigDecimal.ZERO;

    private final List<DomainEvent> dirtyEvents = new ArrayList<>();

    public List<DomainEvent> getDirtyEvents() {
        return Collections.unmodifiableList(dirtyEvents);
    }
    public CreditCard(UUID uuid){
        this.uuid = uuid;
    }

    void assignLimit(BigDecimal amount) { //command (allowed to change state)
        if (limitAlreadyAssigned()){ //invariant
            throw new IllegalStateException(); //NACK
        }
        //ACK (Name it in passed tense)
        limitAssigned(new LimitAssigned(uuid,new Date(),amount));
    }

    private void limitAssigned(LimitAssigned limitAssigned) {
        this.limit = limitAssigned.getLimit();
        dirtyEvents.add(limitAssigned);
    }

    private boolean limitAlreadyAssigned() {
        if (limit != null) return true;
        else return false;
    }

    void withdraw(BigDecimal amount) {
        if (notEnoughMoneyToWithdraw(amount)) {
            throw new IllegalStateException();
        }
        cardWithDrawn(new CardWithdrawn(uuid,amount,new Date()));
    }

    private void cardWithDrawn(CardWithdrawn cardWithdrawn) {
        this.used = used.add(cardWithdrawn.getAmount());
        dirtyEvents.add(cardWithdrawn);
    }

    private boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
        return availableLimit().compareTo((amount))<0;
    }

    void repay(BigDecimal amount) {
        cardRepaid(new CardRepaid(uuid,amount,new Date()));
    }

    private void cardRepaid(CardRepaid cardRepaid) {
        used = used.subtract(cardRepaid.getAmount());
        dirtyEvents.add(cardRepaid);
    }

    public BigDecimal availableLimit() {
        return limit.subtract(used);
    }


}
