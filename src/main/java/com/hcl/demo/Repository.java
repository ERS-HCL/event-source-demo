package com.hcl.demo;

import com.sun.java.browser.plugin2.DOM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Repository {

    private final Map<UUID,List<DomainEvent>> eventStreams = new ConcurrentHashMap<>();

    void save(CreditCard creditCard){
        List<DomainEvent> currentStream = eventStreams.getOrDefault(
                creditCard.getUuid(), new ArrayList<>()
                );
        List<DomainEvent> newEvents = creditCard.getDirtyEvents();
        currentStream.addAll(newEvents);
        eventStreams.put(creditCard.getUuid(),currentStream);
        creditCard.eventsFlushed();
    }

    CreditCard load(UUID uuid){
        return CreditCard.recreate(uuid,eventStreams.getOrDefault(uuid,new ArrayList<>()));
    }
}
