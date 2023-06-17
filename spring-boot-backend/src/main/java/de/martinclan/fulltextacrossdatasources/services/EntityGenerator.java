package de.martinclan.fulltextacrossdatasources.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class EntityGenerator {
    private EntityGenerator() {}
    private static final Logger logger = LoggerFactory.getLogger(EntityGenerator.class);

    public static <T> void generate(String name, long numberToCreate, JpaRepository<T, String> repository, Supplier<T> generator) {
        logger.info("Generating " + name);
        long currentNumberOfObjects = repository.count();
        List<CompletableFuture> futures = new ArrayList<>();
        while (currentNumberOfObjects < numberToCreate) {
            long objectsToCreateAndSave = Math.min(1000, numberToCreate - currentNumberOfObjects);
            long startIndex = currentNumberOfObjects;
            futures.add(CompletableFuture.runAsync(() -> {
                saveN(objectsToCreateAndSave, repository, generator);
                logger.info(
                        "Saved " + name + ": " + startIndex + "-" + (
                                startIndex + objectsToCreateAndSave
                        )
                );
            }));
            currentNumberOfObjects += objectsToCreateAndSave;
        }
        futures.forEach(CompletableFuture::join);
        logger.info("Generating " + name + " finished");
    }

    private static <T> void saveN(long n, JpaRepository<T, String> repository, Supplier<T> generator) {
        List<T> objectsToSave = new ArrayList<>();
        for (long i = n; i > 0; i--) {
            objectsToSave.add(generator.get());
        }
        repository.saveAll(objectsToSave);
    }
}
