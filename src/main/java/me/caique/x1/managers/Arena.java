package me.caique.x1.managers;

import me.caique.x1.Core;
import me.caique.x1.utils.cUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;

public class Arena {

    private static Player desafiante;
    private static Player desafiado;

    private static boolean REQUEST = false;
    private static boolean RUNNING = false;

    private static final HashSet<Player> FREEZED = new HashSet<>();
    private static final HashSet<Player> PLAYERS = new HashSet<>();

    Location LOC1 = new Location(Bukkit.getWorld("world"), -278.5, 57, -686.5, -90, 0);
    Location LOC2 = new Location(Bukkit.getWorld("world"), -270.5, 57, -686.5, 90, 0);
    public static Location LOCSPEC = new Location(Bukkit.getWorld("world"), -274.5, 63, -691.5);


    public Arena(Player desafiante, Player desafiado) {
        Arena.desafiante = desafiante;
        Arena.desafiado = desafiado;
    }

    public static boolean isAvailable() {
        return !REQUEST && !RUNNING;
    }

    public void sendRequest() {
        REQUEST = true;

        getDesafiante().sendMessage("§fVocê enviou um desafio de §eduelo §fpara §a" + getDesafiado().getName() + "§f.");
        cUtils.sendJSONRequest(getDesafiado(), getDesafiante());

        PLAYERS.add(getDesafiante());
        PLAYERS.add(getDesafiado());

        new BukkitRunnable() {

            @Override
            public void run() {
                cancelRequest(CancelType.TIMEOUT);
            }
        }.runTaskLater(Core.getInstance(), 20 * 30);
    }

    public void cancelRequest(CancelType type) {
        if (REQUEST) {
            switch (type) {
                case TIMEOUT:
                    getDesafiante().sendMessage("§cO seu desafio de duelo não foi respondido.");
                    getDesafiado().sendMessage("§cVocê não respondeu ao desafio de duelo contra " + getDesafiante().getName() + ".");
                    break;
                case REJECT:
                    getDesafiante().sendMessage("§cO seu desafio de duelo foi recusado.");
                    getDesafiado().sendMessage("§cVocê recusou o desafio de duelo.");
                    break;
            }

            resetArena();
        }
    }

    public void acceptRequest() {
        REQUEST = false;
        RUNNING = true;

        cUtils.sendJSONBroadcast();

        getDesafiado().teleport(LOC1);
        getDesafiante().teleport(LOC2);

        for (Player player : PLAYERS) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 1000000, 250));
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 250));

            getFreezed().add(player);
            getFreezed().add(player);
        }

        sendCounter(5);
    }

    public void rejectRequest() {
        cancelRequest(CancelType.REJECT);
    }

    private void sendCounter(int seconds) {
        new BukkitRunnable() {

            int i = seconds;

            @Override
            public void run() {
                i--;

                if (i >= 0) {
                    for (Player player : PLAYERS) {
                        player.sendTitle("§a§l" + i, "");
                    }
                }

                if (i == -1) {
                    for (Player player : PLAYERS) {
                        player.removePotionEffect(PotionEffectType.SLOW);
                        player.removePotionEffect(PotionEffectType.JUMP);
                        getFreezed().clear();
                    }

                    cancel();
                }
            }
        }.runTaskTimer(Core.getInstance(), 20, 20);
    }

    public void end(Player winner) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("O duelo entre §a" + getDesafiante().getName() + " §fe §a" + getDesafiado().getName() + " §ffoi finalizado.");
        Bukkit.broadcastMessage("§e" + winner.getName() + " §ffoi o vencedor da batalha!");
        Bukkit.broadcastMessage("");

        new Top().add(winner);

        cUtils.fireworkAnimation(winner);

        boolean af = winner.getAllowFlight();
        winner.setAllowFlight(true);
        winner.setFlying(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                winner.teleport(LOCSPEC);

                winner.setFlying(false);
                winner.setAllowFlight(af);
            }
        }.runTaskLater(Core.getInstance(), 20 * 5);

        resetArena();
    }

    private void resetArena() {

        for (Player player : getFreezed()) {
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.JUMP);
        }

        getPlayers().clear();
        getFreezed().clear();
        desafiante = null;
        desafiado = null;
        REQUEST = false;
        RUNNING = false;
    }

    public static Player getDesafiante() {
        return desafiante;
    }

    public static Player getDesafiado() {
        return desafiado;
    }

    public static HashSet<Player> getPlayers() {
        return PLAYERS;
    }

    public static HashSet<Player> getFreezed() {
        return FREEZED;
    }

    private enum CancelType {
        TIMEOUT,
        REJECT
    }

}
