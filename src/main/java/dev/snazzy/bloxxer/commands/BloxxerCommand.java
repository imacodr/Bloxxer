package dev.snazzy.bloxxer.commands;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class BloxxerCommand implements CommandExecutor, TabExecutor {

    JavaPlugin plugin;

    public BloxxerCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
       if (!sender.hasPermission("bloxxer.fullControl") || !sender.isOp()) {
           sender.sendMessage("You do not have permission to use this command!");
           return true;
       }

        if (args.length == 0) {
            // No arguments were provided, just "/foo"
            return true;
        }

        if (args.length >= 1) {
            // Some arguments were provided
            if (args[0].equalsIgnoreCase("regenerate")) {
                // The first argument is "bar", therefore "/foo bar"
                this.plugin.getLogger().info("Regenerating API Key...");

                Player p = (Player)sender;
                p.sendMessage(Component.text(ChatColor.AQUA + "[BLOXXER] " + ChatColor.WHITE + "Regenerating API Key..."));

                String apiKey = generateApiKey(64);

                this.plugin.getConfig().set("api_key",  apiKey);
                this.plugin.saveConfig();


                p.sendMessage("");
                p.sendMessage(Component.text(ChatColor.AQUA + "[BLOXXER] " + ChatColor.WHITE + "Server API Key Regenerated"));
                p.sendMessage("");
                p.sendMessage(Component.text(ChatColor.AQUA + "[BLOXXER] " + ChatColor.WHITE + "Your new API Key is: " + truncate(apiKey, 16) + "..."));

                p.sendMessage("");

                TextComponent message = new TextComponent(ChatColor.GREEN + "[COPY NEW KEY]");
                message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, apiKey));
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Copy your API Key").create()));

                p.sendMessage(message);

                return true;
            }

            if (args[0].equalsIgnoreCase("info")) {
                // The first argument is "baz", therefore "/foo baz"
                this.plugin.getLogger().info("Info");
                Player p = (Player)sender;

                p.sendMessage(Component.text(ChatColor.AQUA + "[BLOXXER] " + ChatColor.WHITE + "Bloxxer v" + this.plugin.getDescription().getVersion() + " by Sam Perillo <imacodr>"));
                p.sendMessage("");
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                // The first argument is "baz", therefore "/foo baz"
                this.plugin.getLogger().info("Reload config...");

                Player p = (Player)sender;
                p.sendMessage(Component.text(ChatColor.AQUA + "[BLOXXER] " + ChatColor.WHITE + "Reloading config..."));
                this.plugin.reloadConfig();

                return true;
            }
        }

        return true;
    }

    public static String generateApiKey(int keyLengthBytes) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[keyLengthBytes];
        secureRandom.nextBytes(keyBytes);
        return Base64.getEncoder().encodeToString(keyBytes);
    }

    public static String truncate(String text, int maxLength) {
        if (text == null) {
            return null;
        }
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        List<String> tabCompletions = Arrays.asList("reload", "regenerate", "info");
        String input = args[0].toLowerCase();

        List<String> completions = null;

        for(String s : tabCompletions) {
            if (s.startsWith(input)) {
                if (completions == null) {
                    completions = new ArrayList<>();
                }
                completions.add(s);
            }
        }

        if (completions == null) {
            Collections.sort(tabCompletions);
        }

        return completions;

    }
}
