package com.hcl.demo;

import javaslang.API;
import javaslang.Predicates;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.*;

import static javaslang.API.Case;
import static javaslang.collection.List.ofAll;

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

    private CreditCard limitAssigned(LimitAssigned limitAssigned) {
        this.limit = limitAssigned.getLimit();
        dirtyEvents.add(limitAssigned);
        return this;
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

    private CreditCard cardWithDrawn(CardWithdrawn cardWithdrawn) {
        this.used = used.add(cardWithdrawn.getAmount());
        dirtyEvents.add(cardWithdrawn);
        return this;
    }

    private boolean notEnoughMoneyToWithdraw(BigDecimal amount) {
        return availableLimit().compareTo((amount))<0;
    }

    void repay(BigDecimal amount) {

        cardRepaid(new CardRepaid(uuid,amount,new Date()));
    }

    private CreditCard cardRepaid(CardRepaid cardRepaid) {
        used = used.subtract(cardRepaid.getAmount());
        dirtyEvents.add(cardRepaid);
        return this;
    }

    public BigDecimal availableLimit() {
        return limit.subtract(used);
    }


    public void eventsFlushed() {
        dirtyEvents.clear();
    }

    public static CreditCard recreate(UUID uuid, List<DomainEvent> events) {
        return ofAll(events).foldLeft(new CreditCard(uuid), CreditCard::handle);
    }

    CreditCard handle(DomainEvent domainEvent) {
        return API.Match(domainEvent).of(
                Case(Predicates.instanceOf(LimitAssigned.class),
                        this::limitAssigned),
                Case(Predicates.instanceOf(CardRepaid.class),
                        this::cardRepaid),
                Case(Predicates.instanceOf(CardWithdrawn.class),
                        this::cardWithDrawn)
        );

    }
}
