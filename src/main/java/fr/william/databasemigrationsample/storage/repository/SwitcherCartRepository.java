package fr.william.databasemigrationsample.storage.repository;

import com.mongodb.client.result.UpdateResult;
import fr.william.databasemigrationsample.configuration.CustomMongoProperties;
import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import fr.william.databasemigrationsample.storage.repository.deprecated.DeprecatedCartRepository;
import fr.william.databasemigrationsample.storage.repository.primary.NewCartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Primary
@AllArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = "disable-deprecated-database", havingValue = "false")
public class SwitcherCartRepository implements CartRepository {


    private final NewCartRepository newCartRepository;
    private final DeprecatedCartRepository deprecatedCartRepository;

    private final SmartFunctionExecutor smartFunctionExecutor;

    private final CustomMongoProperties mongoProperties;

    public Mono<CartStorageModel> findById(UUID cartId) {
        return smartFunctionExecutor.applySmartly(cartId, (id) -> {
            Mono<CartStorageModel> newDatabase = newCartRepository.findById(cartId);
            return newDatabase.switchIfEmpty(Mono.defer(() -> deprecatedCartRepository.findById(cartId)));
        }, deprecatedCartRepository::findById);

    }

    public Mono<CartStorageModel> save(CartStorageModel cart) {
        return smartFunctionExecutor.applySmartly(cart, (cartToSave -> {
            if(mongoProperties.isMaintainDeprecatedUpdated()){
                return newCartRepository.save(cart).then(deprecatedCartRepository.save(cart));
            } else {
                return newCartRepository.save(cart);
            }
        }), deprecatedCartRepository::save);
    }


    public Mono<Boolean> deleteCart(UUID cartId) {
        return smartFunctionExecutor.applySmartly(cartId, (id) -> {
            if(mongoProperties.isMaintainDeprecatedUpdated()){
                return deprecatedCartRepository.deleteCart(cartId).then(newCartRepository.deleteCart(cartId));
            } else {
                return newCartRepository.deleteCart(cartId)
                        .flatMap(isDeleted -> {
                            if(!isDeleted){
                                return deprecatedCartRepository.deleteCart(cartId);
                            }
                            return Mono.just(true);
                        });
            }
        }, deprecatedCartRepository::deleteCart);

    }

    public Mono<Void> updateCartCustomer(UUID cartId, String customerId) {
        return smartFunctionExecutor.applySmartly(cartId, (id) -> {
            if(mongoProperties.isMaintainDeprecatedUpdated()){
                return newCartRepository.updateCartCustomer(cartId, customerId).then(deprecatedCartRepository.updateCartCustomer(cartId, customerId)).then();
            } else {
                return newCartRepository.updateCartCustomerAndReturnUpdateResult(cartId, customerId).flatMap(updateResult -> {
                    if(updateResult.getModifiedCount() == 0){
                        return deprecatedCartRepository.updateCartCustomer(cartId, customerId);
                    }
                    return Mono.empty();
                }).then();
            }
        }, (id) -> deprecatedCartRepository.updateCartCustomer(cartId, customerId));
    }

    @Override
    public Mono<UpdateResult> updateCartCustomerAndReturnUpdateResult(UUID cartId, String customerId) {
        return newCartRepository.updateCartCustomerAndReturnUpdateResult(cartId, customerId);
    }
}
