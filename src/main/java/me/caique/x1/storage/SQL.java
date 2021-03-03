package me.caique.x1.storage;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQL {

    public static void createTable() {
        PreparedStatement stm = null;

        try {
            stm = Database.con.prepareStatement("CREATE TABLE IF NOT EXISTS duelos(chave VARCHAR(48), json TEXT, PRIMARY KEY (chave))");
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void putInfos(String key, String json) {
        PreparedStatement stm = null;

        try {
            stm = Database.con.prepareStatement("INSERT INTO duelos(chave, json) VALUES (?,?) ON DUPLICATE KEY UPDATE json = VALUES(json)");
            stm.setString(1, key);
            stm.setString(2, json);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getInfos(String key) {
        PreparedStatement stm = null;

        try {
            stm = Database.con.prepareStatement("SELECT json FROM duelos WHERE chave = ?");
            stm.setString(1, key);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getString("json");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject get(String key) {
        JSONParser parser = new JSONParser();
        JSONObject brute = new JSONObject();

        if (getInfos(key) == null) {
            return null;
        }

        try {
            brute = (JSONObject) parser.parse(getInfos(key));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return brute;
    }

    public static void remove(String key) {
        PreparedStatement stm = null;

        try {
            stm = Database.con.prepareStatement("DELETE FROM duelos WHERE chave = ?");
            stm.setString(1 , key);
            stm.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
