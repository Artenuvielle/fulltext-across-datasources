package de.martinclan.fulltextacrossdatasources.controllers;

import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import de.martinclan.fulltextacrossdatasources.entities.elasticsearch.CombinedDataEntity;
import de.martinclan.fulltextacrossdatasources.services.CombinedDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
public class DataController {
    @Autowired private CombinedDataService combinedDataService;

    @GetMapping("/index")
    public ResponseEntity<String> index() {
        CompletableFuture.runAsync(() -> combinedDataService.fetchAndIndexData());
        return ResponseEntity.ok("started");
    }

    @GetMapping("/data")
    @CrossOrigin(origins = "${CORS_ORIGIN}")
    public ResponseEntity<Map<String, Object>> searchAndPaginate(
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sortField,
            @RequestParam(required = false, defaultValue = "true") boolean sortDescending
    ) throws IOException {
        HitsMetadata<CombinedDataEntity> response = combinedDataService.searchAndPaginate(
                search,
                pageNumber,
                pageSize,
                sortField,
                sortDescending
        );
        TotalHits totalHits = response.total();
        Map<String, Object> result = Map.of(
                "pages", totalHits == null ? 0 : totalHits.value(),
                "data", response.hits().stream().map(Hit::source).collect(Collectors.toList())
        );
        return ResponseEntity.ok(result);
    }
}
