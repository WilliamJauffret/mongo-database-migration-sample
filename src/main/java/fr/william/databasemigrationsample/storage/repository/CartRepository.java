package fr.william.databasemigrationsample.storage.repository;

import com.mongodb.client.result.UpdateResult;
import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartRepository {

    Mono<CartStorageModel> findById(UUID cartId);
    Mono<CartStorageModel> save(CartStorageModel cart);
    Mono<Boolean> deleteCart(UUID cartId);
    Mono<Void> updateCartCustomer(UUID cartId, String customerId);
    Mono<UpdateResult> updateCartCustomerAndReturnUpdateResult(UUID cartId, String customerId);



}
