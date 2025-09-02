package com.example.bff.controller;

import com.example.bff.model.Product;
import com.example.bff.model.Warehouse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class BffController {

    private final WebClient webClient = WebClient.builder().build();

    // ------------------ PRODUCTS ------------------

    // GET todos
    @GetMapping(value = "/api/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Product>> getProducts() {
        String url = "https://producto-plan.azurewebsites.net/api/products?code=1BTxKaA75iXujiE5Ao7o4HZUoF6NviI6CMNYrfoKN7CzAzFu7zpzvQ==";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Product>>() {});
    }

    // GET por ID
    @GetMapping(value = "/api/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Product> getProductById(@PathVariable String id) {
        String url = "https://producto-plan.azurewebsites.net/api/products/" + id + "?code=6zbseQRT80_Vl5jZ1ptCXHp70XY19rt1e3Ekx9u9ONcmAzFuUI25-Q==";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Product.class);
    }

    // POST crear
    @PostMapping(value = "/api/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Product> createProduct(@RequestBody Product product) {
        String url = "https://producto-plan.azurewebsites.net/api/products?code=icuTG8Gd8WXGzNPDcQ9qwHs6HCF3JdBoSd5-S_xmkvAhAzFu6fNPwA==";
        return webClient.post()
                .uri(url)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }

    // PUT actualizar
    @PutMapping(value = "/api/products/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Product> updateProduct(@PathVariable String id, @RequestBody Product product) {
        String url = "https://producto-plan.azurewebsites.net/api/products/" + id + "?code=k-uebDExo2ShpMPAKe-0F4jyhGuS9rCW4Gk-J0ryDxltAzFuc5lPYQ==";
        return webClient.put()
                .uri(url)
                .bodyValue(product)
                .retrieve()
                .bodyToMono(Product.class);
    }

    // DELETE eliminar
    @DeleteMapping(value = "/api/products/{id}")
    public Mono<HttpStatus> deleteProduct(@PathVariable String id) {
        String url = "https://producto-plan.azurewebsites.net/api/products/" + id + "?code=PPgAA6KcedmspfDlKSxqyBXWKWhgE0QWy6Fe0tr6xQWuAzFui8YcgQ==";
        return webClient.delete()
                .uri(url)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(HttpStatus.NO_CONTENT);
                    } else {
                        return Mono.just(HttpStatus.NOT_FOUND);
                    }
                });
    }

    // ------------------ WAREHOUSES ------------------

    // GET todos
    @GetMapping(value = "/api/warehouses", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<List<Warehouse>> getWarehouses() {
        String url = "https://almacen-plan.azurewebsites.net/api/warehouses?code=U8PZgudQmG0c9zhDdzFzZJoSioQbfnqdEA0Q3UwKidAzAzFugUNq_Q==";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Warehouse>>() {});
    }

    // GET por ID
    @GetMapping(value = "/api/warehouses/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Warehouse> getWarehouseById(@PathVariable String id) {
        String url = "https://almacen-plan.azurewebsites.net/api/warehouses/" + id + "?code=n4PxululLpiz3X26xIGZFOf4YwZPDIQFfGcZKw0SgRh4AzFuJdC6Rw==";
        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(Warehouse.class);
    }

    // POST crear
    @PostMapping(value = "/api/warehouses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        String url = "https://almacen-plan.azurewebsites.net/api/warehouses?code=PJklmpsRD4fIZ2S-EzyHYJzqB0wjcnFIYgJ7Q8FqNvf_AzFu7AFwog==";
        return webClient.post()
                .uri(url)
                .bodyValue(warehouse)
                .retrieve()
                .bodyToMono(Warehouse.class);
    }

    // PUT actualizar
    @PutMapping(value = "/api/warehouses/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Warehouse> updateWarehouse(@PathVariable String id, @RequestBody Warehouse warehouse) {
        String url = "https://almacen-plan.azurewebsites.net/api/warehouses/" + id + "?code=5UW5ht9xQMh09uqGFB2jeW4sXEXlSTDWI20avRNSlG_UAzFuECn6sw==";
        return webClient.put()
                .uri(url)
                .bodyValue(warehouse)
                .retrieve()
                .bodyToMono(Warehouse.class);
    }

    // DELETE eliminar
    @DeleteMapping(value = "/api/warehouses/{id}")
    public Mono<HttpStatus> deleteWarehouse(@PathVariable String id) {
        String url = "https://almacen-plan.azurewebsites.net/api/warehouses/" + id + "?code=x5rjBhiy5Z1ZSUSHwuA9HIVBe3v-DPvYtgnBssp6cVqrAzFursVLvQ==";
        return webClient.delete()
                .uri(url)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return Mono.just(HttpStatus.NO_CONTENT);
                    } else {
                        return Mono.just(HttpStatus.NOT_FOUND);
                    }
                });
    }
}
