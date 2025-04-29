package dev.snazzy.bloxxer;

import dev.snazzy.bloxxer.events.ChatListener;
import dev.snazzy.bloxxer.rest.AuthenticatedHandler;
import dev.snazzy.bloxxer.tools.BloxxyCola;
import dev.snazzy.bloxxer.tools.Restboy;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bloxxer extends JavaPlugin {
    private Restboy restboy;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
        System.out.println("Hatsune Miku says hi :)");
        saveDefaultConfig();

        String robloxApiKey = getConfig().getString("roblox_api_key", "");
        String universeId = getConfig().getString("universe_id", null);
        String serverApiKey = getConfig().getString("api_key", "");

        System.out.println(universeId);

        this.restboy = new Restboy(8080);
        BloxxyCola cola;

        if (universeId != null) {
            cola = new BloxxyCola(robloxApiKey, universeId);
        } else {
            cola = new BloxxyCola(robloxApiKey);
        }

        getLogger().info("Bloxxer enabled!");

        getServer().getPluginManager().registerEvents(new ChatListener(cola), this);


        restboy.createContext("/api/receive", new AuthenticatedHandler(serverApiKey));
        restboy.start();

        getLogger().info("Restboy enabled!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (restboy.getHttpServer() != null) {
            restboy.stop();
        getLogger().info("Bloxxer is about to close :(");
        }
    }
}
