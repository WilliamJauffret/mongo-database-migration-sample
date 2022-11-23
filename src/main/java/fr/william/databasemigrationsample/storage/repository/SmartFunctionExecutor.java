package fr.william.databasemigrationsample.storage.repository;

import fr.william.databasemigrationsample.configuration.CustomMongoProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@AllArgsConstructor
public class SmartFunctionExecutor {

    private final CustomMongoProperties mongoProperties;

    public <O, I> O applySmartly(I input1, Function<I, O> newFunctionToApply, Function<I, O> deprecatedFunctionToApply){
        if(mongoProperties.isMongoDoubleDatabaseMigrationOn()){
            return newFunctionToApply.apply(input1);
        } else {
            return deprecatedFunctionToApply.apply(input1);
        }
    }
}
