package fr.william.databasemigrationsample.storage.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CartEligibleItemStorageModel extends CartItemStorageModel {
    private UUID campaignId;
    private int eligibleQuantity;
}
