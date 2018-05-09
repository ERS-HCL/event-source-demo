package com.hcl.demo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor

public class CycleClosed implements DomainEvent {
    private UUID uuid;
    private Date date;
    private String type = "card.cycleclosed";

    public CycleClosed(UUID uuid, Date date) {

        this.uuid = uuid;
        this.date = date;
    }

    @Override
    public UUID aggregateUUID() {
        return uuid;
    }

    @Override
    public Date timestamp() {
        return date;
    }
}