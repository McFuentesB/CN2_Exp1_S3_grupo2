package com.functions.warehouse;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class WarehouseFunction {

    // Lista en memoria (thread-safe)
    private static List<Warehouse> warehouses = new CopyOnWriteArrayList<>();

    // Modelo de Warehouse
    public static class Warehouse {
        private String id;
        private String name;
        private String location;

        public Warehouse() {}  // necesario para JSON deserialization

        public Warehouse(String id, String name, String location) {
            this.id = id;
            this.name = name;
            this.location = location;
        }

        // Getters y setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
    }

    // GET: obtener todos
    @FunctionName("getWarehouses")
    public HttpResponseMessage getWarehouses(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "warehouses") 
            HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(warehouses)
                .build();
    }

    // GET: obtener por id
    @FunctionName("getWarehouseById")
    public HttpResponseMessage getWarehouseById(
            @HttpTrigger(name = "req", methods = {HttpMethod.GET}, route = "warehouses/{id}") 
            HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        Warehouse warehouse = warehouses.stream()
                .filter(w -> w.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (warehouse == null) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("No se encuentra el almacen para el id " + id).build();
        }

        return request.createResponseBuilder(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(warehouse)
                .build();
    }

    // POST: crear
    @FunctionName("createWarehouse")
    public HttpResponseMessage createWarehouse(
            @HttpTrigger(name = "req", methods = {HttpMethod.POST}, route = "warehouses") 
            HttpRequestMessage<Optional<Warehouse>> request,
            final ExecutionContext context) {

        Warehouse warehouse = request.getBody().orElse(null);
        if (warehouse == null || warehouse.getId() == null || warehouse.getName() == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Almacen debe tener id, nombre y location").build();
        }

        // verificar si existe
        boolean exists = warehouses.stream()
                .anyMatch(w -> w.getId().equals(warehouse.getId()));

        if (exists) {
            return request.createResponseBuilder(HttpStatus.CONFLICT)
                    .body("Almacen con el id " + warehouse.getId() + " ya existe").build();
        }

        warehouses.add(warehouse);
        return request.createResponseBuilder(HttpStatus.CREATED).body(warehouse).build();
    }

    // PUT: actualizar
    @FunctionName("updateWarehouse")
    public HttpResponseMessage updateWarehouse(
            @HttpTrigger(name = "req", methods = {HttpMethod.PUT}, route = "warehouses/{id}") 
            HttpRequestMessage<Optional<Warehouse>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        Warehouse updatedWarehouse = request.getBody().orElse(null);
        if (updatedWarehouse == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body("Invalid body").build();
        }

        for (int i = 0; i < warehouses.size(); i++) {
            if (warehouses.get(i).getId().equals(id)) {
                warehouses.set(i, updatedWarehouse);
                return request.createResponseBuilder(HttpStatus.OK).body(updatedWarehouse).build();
            }
        }

        return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                .body("No se encuentra el almacen para el id " + id).build();
    }

    // DELETE: eliminar
    @FunctionName("deleteWarehouse")
    public HttpResponseMessage deleteWarehouse(
            @HttpTrigger(name = "req", methods = {HttpMethod.DELETE}, route = "warehouses/{id}") 
            HttpRequestMessage<Optional<String>> request,
            @BindingName("id") String id,
            final ExecutionContext context) {

        boolean removed = warehouses.removeIf(w -> w.getId().equals(id));

        if (!removed) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND)
                    .body("No se encuentra el almacen para el id " + id).build();
        }

        return request.createResponseBuilder(HttpStatus.NO_CONTENT).build();
    }
}
