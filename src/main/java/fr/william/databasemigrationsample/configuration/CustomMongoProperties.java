package fr.william.databasemigrationsample.configuration;


import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.data.mongodb")
@Data
public class CustomMongoProperties {
    private MongoProperties primary;
    private MongoProperties deprecated;
    private int connectionTimeout = 300;
    private int readTimeout = 300;
    private boolean maintainDeprecatedUpdated;
    private boolean mongoDoubleDatabaseMigrationOn;
}
