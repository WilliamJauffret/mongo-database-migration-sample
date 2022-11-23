package fr.william.databasemigrationsample.endpoint;


import fr.william.databasemigrationsample.service.CartService;
import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/carts/v0")
@AllArgsConstructor
public class CartEndpoint {

    private final CartService cartService;

    @GetMapping("/{cartId}")
    public Mono<ResponseEntity> getCartById(@PathVariable UUID cartId) {
        return cartService.findById(cartId)
                .map(cart -> new ResponseEntity(cart, OK))
                .defaultIfEmpty(new ResponseEntity(NOT_FOUND));
    }


    @PostMapping
    public Mono<ResponseEntity> createCart(@RequestBody @Validated CartStorageModel cart) {
        return cartService
                .saveCart(cart).map(createdCart -> new ResponseEntity(createdCart, CREATED));
    }
}
