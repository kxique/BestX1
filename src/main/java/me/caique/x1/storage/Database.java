package me.caique.x1.storage;

import me.caique.x1.Core;
import org.bukkit.ChatColor;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public static Connection con = null;

    public static void connect() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (Core.getInstance().getConfig().getBoolean("mysql.usar")) {
            openMySQL();
        }else {
            openSQLite();
        }

        if (con != null) {
            SQL.createTable();
        }
    }

    public static void openMySQL() {
        String host = Core.getInstance().getConfig().getString("mysql.host");
        int port = Core.getInstance().getConfig().getInt("mysql.porta");
        String db = Core.getInstance().getConfig().getString("mysql.database");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db;

        String user = Core.getInstance().getConfig().getString("mysql.user");
        String password = Core.getInstance().getConfig().getString("mysql.senha");

        try {
            con = DriverManager.getConnection(url, user, password);
            Core.getInstance().sendMessage("§aConexão com o MySQL estabelecida.");
        } catch (SQLException e) {
            Core.getInstance().sendMessage("§4Ocorreu um erro ao conectar-se com o MySQL, desativando plugin.");
            Core.getInstance().getPluginLoader().disablePlugin(Core.getInstance());
            e.printStackTrace();
        }
    }

    public static void openSQLite() {
        File db = new File(Core.getInstance().getDataFolder(), "database.db");
        String URL = "jdbc:sqlite:" + db;

        try {
            con = DriverManager.getConnection(URL);
            Core.getInstance().sendMessage(ChatColor.GREEN + "Conexão com o SQLite estabelecida.");
        } catch (SQLException e) {
            e.printStackTrace();
            Core.getInstance().sendMessage("§4Ocorreu um erro ao conectar-se com o SQLite, desativando plugin.");
            Core.getInstance().getPluginLoader().disablePlugin(Core.getInstance());
        }
    }

    public static void closeDatabase() {
        if (con != null) {
            try {
                con.close();
                //Core.getInstance().sendMessage("§aConexão ao banco de dados fechada.");
            } catch (SQLException e) {
                //Core.getInstance().sendMessage("§4A conexão não pôde ser fechada.");
            }
        }
    }

}
