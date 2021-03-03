package me.caique.x1.listeners;

import me.caique.x1.commands.Duelo;
import me.caique.x1.managers.Arena;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Combat implements Listener {

    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player los = e.getEntity();
        Player winn = e.getEntity().getKiller();

        if (Arena.getPlayers().contains(los) && Arena.getPlayers().contains(winn)) {
            Duelo.x1.end(winn);
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if (Arena.getPlayers().contains(p)) {
            Player winn = Arena.getDesafiado() == p ? Arena.getDesafiante() : Arena.getDesafiado();
            Duelo.x1.end(winn);
        }
    }

}
