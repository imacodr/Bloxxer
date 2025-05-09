package dev.snazzy.bloxxer.events;

import dev.snazzy.bloxxer.tools.BloxxyCola;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import net.kyori.adventure.text.Component;

import java.io.IOException;

public class ChatListener implements Listener {
    private final BloxxyCola cola;

    public ChatListener(BloxxyCola cola) {
        this.cola = cola;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            // Proper way: serialize the Component to plain text
            String plainMessage = PlainTextComponentSerializer.plainText().serialize(message);

            try {
                cola.publishChatMessage(source.getName(), plainMessage);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            return Component.text("<")
                    .append(sourceDisplayName)
                    .append(Component.text("> "))
                    .append(message);
        });
    }
}