package fr.william.databasemigrationsample.configuration;

import com.mongodb.*;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.List;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
@Slf4j
@EnableReactiveMongoRepositories
public class MongoConfiguration {

    private final CustomMongoProperties customMongoProperties;



    public MongoConfiguration(CustomMongoProperties customMongoProperties) {
        this.customMongoProperties = customMongoProperties;
    }

    @Primary
    @Bean
    public MongoClient reactiveMongoClientPrimary() {
        log.info("Creating reactiveMongoClientPrimary with uri " + customMongoProperties.getPrimary().getUri());
        return MongoClients.create(createMongoClientSettings(customMongoProperties.getPrimary()));
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.data.mongodb", name = "disable-deprecated-database", havingValue = "false")
    public MongoClient reactiveMongoClientDeprecated() {
        log.info("Creating reactiveMongoClientDeprecated with uri " + customMongoProperties.getDeprecated().getUri());
        return MongoClients.create(createMongoClientSettings(customMongoProperties.getDeprecated()));
    }

    @Primary
    @Bean("mongoTemplatePrimary")
    public ReactiveMongoTemplate reactiveMongoTemplatePrimary(){
        var template = new ReactiveMongoTemplate(reactiveMongoClientPrimary(), customMongoProperties.getPrimary().getDatabase());
        return template;
    }

    @Bean("mongoTemplateDeprecated")
    @ConditionalOnProperty(prefix = "spring.data.mongodb", name = "disable-deprecated-database", havingValue = "false")
    public ReactiveMongoTemplate reactiveMongoTemplateDeprecated(){
        var template = new ReactiveMongoTemplate(reactiveMongoClientDeprecated(),customMongoProperties.getDeprecated().getDatabase());
        return template;
    }


    private MongoClientSettings createMongoClientSettings(MongoProperties mongoProperties){

        ConnectionString connectionString = new ConnectionString(mongoProperties.getUri());

        return MongoClientSettings.builder()
                .readConcern(ReadConcern.DEFAULT)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .applyToSocketSettings(builder -> {
                    builder.connectTimeout(customMongoProperties.getConnectionTimeout(), MILLISECONDS);
                    builder.readTimeout(customMongoProperties.getReadTimeout(), MILLISECONDS);
                })
                .applyToClusterSettings(builder -> builder.serverSelectionTimeout(customMongoProperties.getConnectionTimeout(), MILLISECONDS))
                .writeConcern(WriteConcern.MAJORITY)
                .readPreference(ReadPreference.primary())
                .applyConnectionString(connectionString)
                .build();
    }

}