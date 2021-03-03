package me.caique.x1;

import me.caique.x1.commands.Duelo;
import me.caique.x1.listeners.Combat;
import me.caique.x1.listeners.Movement;
import me.caique.x1.storage.Database;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {

    private static Core instance;

    @Override
    public void onEnable() {
        instance = this;

        setupCommands();
        setupListeners();

        sendMessage("§aPlugin ativado.");

        Database.connect();
    }

    @Override
    public void onDisable() {
        Database.closeDatabase();

        sendMessage("§cPlugin desativado.");
    }

    public void sendMessage(String message) {
        Bukkit.getConsoleSender().sendMessage("[" + getName() + "] " + message);
    }

    public void setupCommands() {
        getCommand("duelo").setExecutor(new Duelo());
    }

    public void setupListeners() {
        Bukkit.getPluginManager().registerEvents(new Combat(), this);
        Bukkit.getPluginManager().registerEvents(new Movement(), this);
    }

    public static Core getInstance() {
        return instance;
    }
}
