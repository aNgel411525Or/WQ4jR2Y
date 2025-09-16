// 代码生成时间: 2025-09-17 05:49:55
package com.example.shoppingcart;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The main application class for the shopping cart.
 */
public class ShoppingCartApp {

    // Map to store shopping cart items
    private static final Map<String, Map<String, Integer>> carts = new HashMap<>();

    public static void main(String[] args) {
        // Set up routes for adding and getting cart items
        get("/cart/:cartId", (req, res) -> {
            String cartId = req.params(":cartId");
            return carts.getOrDefault(cartId, new HashMap<>());
        });

        post("/cart/:cartId", (req, res) -> {
            String cartId = req.params(":cartId");
            String itemId = req.queryParams("itemId");
            int quantity = Integer.parseInt(req.queryParams("quantity"));
            if (quantity <= 0) {
                halt(400, "Quantity must be greater than zero.");
            }
            carts.computeIfAbsent(cartId, k -> new HashMap<>()).merge(itemId, quantity, Integer::sum);
            return "Item added to the cart.";
        });

        // Start the Spark server
        port(8080);
    }
}
