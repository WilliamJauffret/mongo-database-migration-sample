package fr.william.databasemigrationsample.storage.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CartItemOfferingStorageModel {
    private int actualQuantity;
    private int maxQuantity;
    private String defaultPriceType;
    private Prices prices;
    private Date releaseDate;

    @Data
    public static class Prices {
        private MonetaryAmountRepresentation price;
        private MonetaryAmountRepresentation oldPrice;
        private MonetaryAmountRepresentation discount;
        private MonetaryAmountRepresentation totalPrice;
        private MonetaryAmountRepresentation totalOldPrice;
        private MonetaryAmountRepresentation totalDiscount;
        private MonetaryAmountRepresentation totalLoyalty;
    }
}
