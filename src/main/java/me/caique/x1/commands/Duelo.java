package me.caique.x1.commands;

import me.caique.x1.managers.Arena;
import me.caique.x1.managers.Top;
import me.caique.x1.utils.cUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Duelo implements CommandExecutor {

    public static Arena x1;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (!(sender instanceof Player)) {
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            sendHelp(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("desafiar")) {
            if (args.length < 2) {
                p.sendMessage("§cUse /duelo desafiar (jogador).");
                return true;
            }

            Player target = Bukkit.getPlayerExact(args[1]);
            if (target == null || !target.isOnline() || p == target) {
                p.sendMessage("§cJogador inválido.");
                return true;
            }

            if (!Arena.isAvailable()) {
                p.sendMessage("§cArena indisponível no momento.");
                return true;
            }

            x1 = new Arena(p, target);
            x1.sendRequest();
        }

        if (args[0].equalsIgnoreCase("aceitar")) {
            if (Arena.getDesafiado() == null || !Arena.getDesafiado().equals(p)) {
                p.sendMessage("§cVocê não foi desafiado para um duelo.");
                return true;
            }

            x1.acceptRequest();
        }

        if (args[0].equalsIgnoreCase("recusar")) {
            if (Arena.getDesafiado() == null || !Arena.getDesafiado().equals(p)) {
                p.sendMessage("§cVocê não foi desafiado para um duelo.");
                return true;
            }

            x1.rejectRequest();
        }

        if (args[0].equalsIgnoreCase("assistir")) {
            p.teleport(Arena.LOCSPEC);
        }

        if (args[0].equalsIgnoreCase("top")) {
            Top.send(p);
            cUtils.fireworkAnimation(p);
        }

        return false;
    }

    public void sendHelp(Player p) {
        p.sendMessage("");
        p.sendMessage("§aAjuda - Duelos");
        p.sendMessage("");
        p.sendMessage("§a/duelo desafiar (jogador) §f- §7desafia o jogador para um duelo.");
        p.sendMessage("§a/duelo aceitar §f- §7aceita um duelo.");
        p.sendMessage("§a/duelo recusar §f- §7nega o desafio de duelo.");
        p.sendMessage("§a/duelo assistir §f- §7vai para a arena.");
        p.sendMessage("§a/duelo top §f- §7mostra os tops duelistas.");
        p.sendMessage("");
    }

}
