package me.caique.x1.listeners;

import me.caique.x1.managers.Arena;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Movement implements Listener {

    @EventHandler
    public void move(PlayerMoveEvent e) {
        if (Arena.getFreezed().contains(e.getPlayer()) && e.getPlayer().isSprinting()) {
            e.setCancelled(true);
        }
    }

}
