package fr.william.databasemigrationsample.storage.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CartItemStorageModel {
    @SerializedName("_id")
    private UUID id;
    private int version;
    private Date createdAt;
    private Date lastModifiedAt;


    private UUID productId;
    private UUID offerId;
    private int desiredQuantity;
    private CartItemOfferingStorageModel offering;
    
    public void initialize() {
        this.setId(UUID.randomUUID());
        this.setCreatedAt(new Date());
        this.setLastModifiedAt(new Date());
        this.setVersion(1);
    }
}
