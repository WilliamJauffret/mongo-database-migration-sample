package fr.william.databasemigrationsample.service;

import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import fr.william.databasemigrationsample.storage.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Mono<CartStorageModel> findById(UUID cartId) {
        return cartRepository.findById(cartId);
    }

    public Mono<CartStorageModel> saveCart(CartStorageModel cart) {
        cart.initialize();
        return cartRepository
                .save(cart);
    }

}
