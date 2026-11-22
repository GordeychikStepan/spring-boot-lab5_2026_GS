package ru.gordeychik.lab5.service1.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Systems {
    ERP("ERP", "Enterprise Resource Planning"),
    CRM("CRM", "Customer Relationship Management"),
    WMS("WMS", "Warehouse Management System"),
    SERVICE_1("Service 1", "Первый сервис");

    private final String value;
    private final String description;

    Systems(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonValue
    public String getValue() { return value; }

    public String getDescription() { return description; }

    @JsonCreator
    public static Systems fromValue(String value) {
        for (Systems system : values()) {
            if (system.value.equals(value)) { return system; }
        }
        throw new IllegalArgumentException(
                "Неизвестная система: " + value);
    }

    @Override
    public String toString() { return value; }
}
