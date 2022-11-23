package fr.william.databasemigrationsample.storage.repository.deprecated;

import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static fr.william.databasemigrationsample.storage.MongoCollections.CARTS;


@Repository
@Slf4j
@ConditionalOnProperty(prefix = "spring.data.mongodb", name = "disable-deprecated-database", havingValue = "false")
public class DeprecatedCartRepository {


    private final ReactiveMongoTemplate reactiveMongo;


    public DeprecatedCartRepository(@Qualifier("mongoTemplateDeprecated") ReactiveMongoTemplate reactiveMongo) {
        this.reactiveMongo = reactiveMongo;
    }


    public Mono<CartStorageModel> findById(UUID cartId) {
        log.debug("Finding cart {} in deprecated database", cartId);
        return reactiveMongo.findById(cartId, CartStorageModel.class, CARTS.collectionName());
    }

    public Mono<CartStorageModel> save(CartStorageModel cart) {
        log.debug("Saving cart {} in deprecated database", cart.getId());
        return reactiveMongo.save(cart, CARTS.collectionName());
    }

    public Mono<Boolean> deleteCart(UUID cartId) {
        log.debug("Deleting cart {} in deprecated database", cartId);
        CartStorageModel sample = new CartStorageModel();
        sample.setId(cartId);
        return reactiveMongo
                .remove(sample, CARTS.collectionName())
                .map(it -> it.getDeletedCount() > 0 && it.wasAcknowledged())
        ;
    }

    public Mono<Void> updateCartCustomer(UUID cartId, String customerId) {
        log.debug("Updating cart {} in deprecated database", cartId);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(cartId));
        Update update = new Update();
        update.set("customerId", customerId);
        return reactiveMongo
                .updateFirst(query, update, CARTS.collectionName())
                .then()
        ;
    }
}
