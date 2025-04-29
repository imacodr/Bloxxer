package dev.snazzy.bloxxer.tools;

import com.google.gson.Gson;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BloxxyCola {
    final private String MESSAGING_PUBLISH = "https://apis.roblox.com/cloud/v2/universes/{id}:publishMessage";

    private String apiKey;
    private String universeId;

    public BloxxyCola(String apiKey) {
        this.apiKey = apiKey;
    }

    public BloxxyCola(String apiKey, String universeId) {
        this.apiKey = apiKey;
        this.universeId = universeId;
    }

    public void setUniverseId(String universeId) {
        this.universeId = universeId;
    }

    public String getUniverseId() {
        return universeId;
    }

    public void publishChatMessage(String playerName, String message) throws IOException, InterruptedException {
        try {
            if (this.universeId == null) {
                throw new IllegalStateException("Universe ID is not set!");
            }

            RobloxChatMessage chatMessage = new RobloxChatMessage();
            chatMessage.setTopic("bloxxerhandler");
            chatMessage.setMessage("<" + playerName + "> " + message);
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(chatMessage);

            String url = MESSAGING_PUBLISH.replace("{id}", this.universeId);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("x-api-key", this.apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();


            HttpClient httpClient = HttpClient.newHttpClient();

            httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            Bukkit.getLogger().warning("[BLOXXYCOLA] Network error during message publish: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Bukkit.getLogger().warning("[BLOXXYCOLA] Network error during message publish: " + e.getMessage());
            Thread.currentThread().interrupt(); // Always re-set the interrupt flag
        } catch (Exception e) {
            Bukkit.getLogger().warning("[BLOXXYCOLA] Network error during message publish: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
