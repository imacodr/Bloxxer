package dev.snazzy.bloxxer;

import dev.snazzy.bloxxer.events.ChatListener;
import dev.snazzy.bloxxer.tools.BloxxyCola;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bloxxer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        String apiKey = getConfig().getString("api_key");
        String universeId = getConfig().getString("universe_id", null);

        System.out.println(universeId);

        BloxxyCola cola;

        if (universeId != null) {
            cola = new BloxxyCola(apiKey, universeId);
        } else {
            cola = new BloxxyCola(apiKey);
        }

        getLogger().info("Bloxxer enabled!");

        getServer().getPluginManager().registerEvents(new ChatListener(cola), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
