package fr.william.databasemigrationsample.storage.repository;

import com.mongodb.client.result.UpdateResult;
import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartRepository {

    public Mono<CartStorageModel> findById(UUID cartId);
    public Mono<CartStorageModel> save(CartStorageModel cart);
    public Mono<Boolean> deleteCart(UUID cartId);
    public Mono<Void> updateCartCustomer(UUID cartId, String customerId);
    public Mono<UpdateResult> updateCartCustomerAndReturnUpdateResult(UUID cartId, String customerId);



}
