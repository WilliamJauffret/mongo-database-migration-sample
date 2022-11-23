package fr.william.databasemigrationsample.storage;

public enum MongoCollections {
    CARTS("carts");

    private final String name;

    MongoCollections(String name) {
        this.name = name;
    }

    public String collectionName() {
        return name;
    }
}
