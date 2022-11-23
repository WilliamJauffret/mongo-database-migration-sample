package fr.william.databasemigrationsample.storage.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class CartStorageModel {
    @SerializedName("_id")
    private UUID id;
    private int version;
    private Date createdAt;
    private Date lastModifiedAt;
    private List<CartItemStorageModel> items = new ArrayList<>();
    private Prices prices = new Prices();
    private String customerId;
    private boolean discountFailsoftMode = false;

    public void initialize() {
        this.setId(UUID.randomUUID());
        this.setCreatedAt(new Date());
        this.setLastModifiedAt(new Date());
        this.setVersion(1);
    }

    @Data
    public static class Prices {
        private MonetaryAmountRepresentation totalPrice;
        private MonetaryAmountRepresentation totalDiscount;
        private MonetaryAmountRepresentation totalLoyalty;
        private MonetaryAmountRepresentation totalCoupon;
    }

}
