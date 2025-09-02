package com.functions.product;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductFunction {

    // Lista en memoria (thread-safe)
    private static List<Product> products = new CopyOnWriteArrayList<>();

    // Modelo Product
    public static class Product {
        private String id;
        private String name;
        private String description;
        private double price;

        public Product() {}  // necesario para JSON deserialization

        public Product(String id, String name, String description, double price) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
        }

        // Getters y setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    // GET: obtener todos
    @FunctionName("getProducts")
    public HttpResponseMessage getProducts(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "products") 
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(products)
                .build();
    }

    // GET: obtener por id
    @FunctionName("getProductById")
    public HttpResponseMessage getProductById(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "products/{id}") 
            HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        Product product = products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("No se encuentra el producto para el id " + id).build();
        }

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(product)
                .build();
    }

    // POST: crear
    @FunctionName("createProduct")
    public HttpResponseMessage createProduct(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, route = "products") 
            HttpRequestMessage<Optional<Product>> request,
            final ExecutionContext context) {

        Product product = request.getBody().orElse(null);
        if (product == null || product.getId() == null || product.getName() == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Producto debe tener id, nombre y descripción").build();
        }

        // verificar si existe
        boolean exists = products.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));

        if (exists) {
            return request.createResponseBuilder(HttpStatus.CONFLICT)
                    .body("Producto con id " + product.getId() + " ya existe").build();
        }

        products.add(product);
        return request.createResponseBuilder(HttpStatus.CREATED).body(product).build();
    }

    // PUT: actualizar
    @FunctionName("updateProduct")
    public HttpResponseMessage updateProduct(
            @HttpTrigger(name = "req", methods = {HttpMethod.PUT}, route = "products/{id}") 
            HttpRequestMessage<Optional<Product>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        Product updatedProduct = request.getBody().orElse(null);
        if (updatedProduct == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Invalid body").build();
        }

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                products.set(i, updatedProduct);
                return request.createResponseBuilder(HttpStatus.OK).body(updatedProduct).build();
            }
        }

        return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                .body("No se encuentra el producto para el id " + id).build();
    }

    // DELETE: eliminar
    @FunctionName("deleteProduct")
    public HttpResponseMessage deleteProduct(
            @HttpTrigger(name = "req", methods = {HttpMethod.DELETE}, route = "products/{id}") 
            HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        boolean removed = products.removeIf(p -> p.getId().equals(id));

        if (!removed) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("No se encuentra el producto para el id " + id).build();
        }

        return request.createResponseBuilder(HttpStatus.NO_CONTENT).build();
    }
}
