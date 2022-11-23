package fr.william.databasemigrationsample.storage.model;

import lombok.Data;

@Data
public class MonetaryAmountRepresentation {
    private String currency;
    private int amount;
}
