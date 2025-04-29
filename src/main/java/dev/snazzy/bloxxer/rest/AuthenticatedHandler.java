package dev.snazzy.bloxxer.rest;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class AuthenticatedHandler implements HttpHandler {
    private final String apiKey;

    public AuthenticatedHandler(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String authHeader = exchange.getRequestHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.equals("Bearer " + apiKey)) {
            String response = "{\"error\":\"Unauthorized\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(401, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
            return;
        }

        // 👇 If authorized, do your normal logic here
        InputStream requestBody = exchange.getRequestBody();
        String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        Gson gson = new Gson();
        JSONObject json = gson.fromJson(body, JSONObject.class);

        String user = json.get("user").toString();
        String message = json.get("message").toString();

        System.out.println(message);

        Bukkit.getScheduler().runTask(
                JavaPlugin.getProvidingPlugin(getClass()),
                () -> Bukkit.broadcast(Component.text("<§c" + user + "§f> " + message))
        );

        String response = "{\"message\":\"Sent!\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}