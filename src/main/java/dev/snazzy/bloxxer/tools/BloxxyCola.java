package dev.snazzy.bloxxer.tools;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
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

            System.out.println(getUniverseId());
            if (this.universeId == null) {
                throw new IllegalStateException("Universe ID is not set!");
            }

            RobloxChatMessage chatMessage = new RobloxChatMessage();
            chatMessage.setTopic("cool");
            chatMessage.setMessage("<" + playerName + "> " + message);
            Gson gson = new Gson();
            String jsonRequest = gson.toJson(chatMessage);


            String url = MESSAGING_PUBLISH.replace("{id}", this.universeId);

            System.out.println(url);
            System.out.println(jsonRequest);
            System.out.println(this.apiKey);

            HttpRequest postRequest = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(10))
                    .header("x-api-key", this.apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                    .build();


            HttpClient httpClient = HttpClient.newHttpClient();

            HttpResponse<String> postResponse = httpClient.send(postRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println(postResponse.body());

        } catch (IOException e) {
            System.err.println("[BLOXXYCOLA] Network error during message publish: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("[BLOXXYCOLA] Request was interrupted: " + e.getMessage());
            Thread.currentThread().interrupt(); // Always re-set the interrupt flag
        } catch (Exception e) {
            System.err.println("[BLOXXYCOLA] Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
