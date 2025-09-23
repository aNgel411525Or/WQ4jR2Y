// 代码生成时间: 2025-09-23 23:59:29
package com.example.notification;

import static spark.Spark.*;
import java.util.HashMap;
import java.util.Map;

public class MessageNotificationSystem {

    private static final String NOTIFICATION_ENDPOINT = "/notification";
    private static final String SUBSCRIPTION_ENDPOINT = "/subscribe";
    private static final String UNSUBSCRIPTION_ENDPOINT = "/unsubscribe";
    private static final String NOTIFICATION_CHANNEL = "notificationChannel";

    // Map to store subscriptions
    private static final Map<String, String> subscriptions = new HashMap<>();

    public static void main(String[] args) {
        // Initialize Spark app
        port(4567);

        // Handle subscription requests
        post(SUBSCRIPTION_ENDPOINT, (request, response) -> {
            String username = request.queryParams("username");
            String message = request.queryParams("message");
            if (username == null || message == null) {
                halt(400, "Username and message are required");
            }
            subscriptions.put(username, message);
            return "Subscribed successfully";
        }, new JsonTransformer());

        // Handle unsubscription requests
        delete(UNSUBSCRIPTION_ENDPOINT, (request, response) -> {
            String username = request.queryParams("username");
            if (username == null) {
                halt(400, "Username is required");
            }
            subscriptions.remove(username);
            return "Unsubscribed successfully";
        }, new JsonTransformer());

        // Handle notification sending
        post(NOTIFICATION_ENDPOINT, (request, response) -> {
            String message = request.queryParams("message");
            if (message == null) {
                halt(400, "Message is required");
            }
            for (Map.Entry<String, String> entry : subscriptions.entrySet()) {
                // Simulate sending a notification
                System.out.println("Sending notification to " + entry.getKey() + ": " + entry.getValue() + " - " + message);
            }
            return "Notifications sent successfully";
        }, new JsonTransformer());

        // Start the Spark web server
        after((request, response) -> {
            response.type("application/json");
        });
    }

    // Utility method to send notifications
    private static void sendNotifications(String message) {
        for (Map.Entry<String, String> entry : subscriptions.entrySet()) {
            // Simulate sending a notification
            System.out.println("Sending notification to " + entry.getKey() + ": " + entry.getValue() + " - " + message);
        }
    }
}
