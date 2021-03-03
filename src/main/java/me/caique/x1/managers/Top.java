package me.caique.x1.managers;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import me.caique.x1.storage.Database;
import me.caique.x1.storage.SQL;
import me.caique.x1.utils.cUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Top {

    public static void send(Player p) {
        p.sendMessage("");
        p.sendMessage("§aDuelo - Top");
        new Top().castTops().forEach(playersTop -> {
            p.sendMessage("§7" + playersTop.getName() + " §f- §b" + playersTop.getWins() + " vitórias.");
        });
        p.sendMessage("");
    }

    public void add(Player p) {
        JSONObject json = new JSONObject();

        if (SQL.getInfos(p.getName()) == null) {
            json.put("wins", 1);
        }else {
            json = SQL.get(p.getName());
            int wins = Integer.parseInt(json.get("wins").toString());
            json.replace("wins", wins + 1);

        }

        SQL.putInfos(p.getName(), json.toJSONString());
    }

    public Stream<PlayersTop> castTops() {
        List<PlayersTop> objectsResult = new ArrayList<>();

        PreparedStatement stm = null;

        try {
            stm = Database.con.prepareStatement("SELECT * FROM duelos ORDER BY LENGTH(json) DESC, json DESC LIMIT 5");
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String name = rs.getString("chave");
                int wins = Integer.parseInt(cUtils.parseJSON(rs.getString("json")).get("wins").toString());
                objectsResult.add(new PlayersTop(name, wins));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objectsResult.stream();
    }

    private class PlayersTop {
        private String name;
        private Integer wins;

        public PlayersTop(String name, int wins) {
            this.name = name;
            this.wins = wins;
        }

        public String getName() {
            return name;
        }

        public Integer getWins() {
            return wins;
        }
    }

}
