package com.ccunarro.hostfully.data.reservation;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Reservation {

    public enum Type {
        BOOKING, BLOCK;
    }

    @Id
    @GeneratedValue(generator = "system-uuid")
    private UUID id;

    private UUID propertyId;

    private UUID userId;

    @Column(name = "start_date")
    private LocalDate start;

    @Column(name = "end_date")
    private LocalDate end;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    public Reservation() {
    }

    public Reservation(UUID propertyId, UUID userId, LocalDate start, LocalDate end, Type type) {
        this.propertyId = propertyId;
        this.type = type;
        this.userId = userId;
        this.start = start;
        this.end = end;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(UUID propertyId) {
        this.propertyId = propertyId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
