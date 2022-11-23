package fr.william.databasemigrationsample.storage.repository.primary;

import com.mongodb.client.result.UpdateResult;
import fr.william.databasemigrationsample.storage.model.CartStorageModel;
import fr.william.databasemigrationsample.storage.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class NewCartRepository implements CartRepository {

    private final ReactiveMongoTemplate reactiveMongo;

    public NewCartRepository(@Qualifier("mongoTemplatePrimary") ReactiveMongoTemplate reactiveMongo) {
        this.reactiveMongo = reactiveMongo;
    }

    public Mono<CartStorageModel> findById(UUID cartId) {
        log.debug("Finding cart {} in new database", cartId);
        return reactiveMongo.findById(cartId, CartStorageModel.class, CARTS.collectionName());
    }

    public Mono<CartStorageModel> save(CartStorageModel cart) {
        log.debug("Saving cart {} in new database", cart.getId());
        return reactiveMongo.save(cart, CARTS.collectionName());
    }

    public Mono<Boolean> deleteCart(UUID cartId) {
        log.debug("Deleting cart {} in new database", cartId);
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
                .then();
    }

    public Mono<UpdateResult> updateCartCustomerAndReturnUpdateResult(UUID cartId, String customerId) {
        log.debug("Updating cart {} in new database", cartId);
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(cartId));
        Update update = new Update();
        update.set("customerId", customerId);
        return reactiveMongo
                .updateFirst(query, update, CARTS.collectionName());
    }

}
