package fr.william.databasemigrationsample.storage.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CustomerStorageModel {
    private String id;
    private UUID cartId;
}
