package dev.snazzy.bloxxer;

import dev.snazzy.bloxxer.commands.BloxxerCommand;
import dev.snazzy.bloxxer.events.ChatListener;
import dev.snazzy.bloxxer.rest.AuthenticatedHandler;
import dev.snazzy.bloxxer.tools.BloxxyCola;
import dev.snazzy.bloxxer.tools.Restboy;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Bloxxer extends JavaPlugin {
    private Restboy restboy;

    @Override
    public void onEnable() {
        // Plugin startup logic
        try {
        System.out.println("Hatsune Miku says hi :)");

        // Load Config
        saveDefaultConfig();
        String robloxApiKey = getConfig().getString("roblox_api_key", "");
        String universeId = getConfig().getString("universe_id", null);
        String serverApiKey = getConfig().getString("api_key", "");

        // Load Helpers (Restboy and BloxxyCola)
        this.restboy = new Restboy(8080);
        BloxxyCola cola;

        if (universeId != null) {
            cola = new BloxxyCola(robloxApiKey, universeId);
        } else {
            cola = new BloxxyCola(robloxApiKey);
        }

        getServer().getPluginManager().registerEvents(new ChatListener(cola), this);

        restboy.createContext("/api/receive", new AuthenticatedHandler(serverApiKey));
        restboy.start();

        getLogger().info("Restboy enabled!");

        registerCommands();
        getLogger().info("Bloxxer enabled!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerCommands() {
        getCommand("bloxxer").setExecutor(new BloxxerCommand(this));
        getCommand("bloxxer").setTabCompleter(new BloxxerCommand(this));

        getLogger().info("Commands registered!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (restboy.getHttpServer() != null) {
            restboy.stop();
        }
        getLogger().info("Bloxxer is about to close :(");
    }
}
