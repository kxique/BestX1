package me.caique.x1.utils;

import me.caique.x1.managers.Arena;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class cUtils {

    public static void sendJSONRequest(Player desafiado, Player desafiante) {
        String JSON = "[\"\",{\"text\":\"Você recebeu um desafio para um duelo com \"},{\"text\":\""
                + desafiante.getName() + "\",\"color\":\"aqua\"},{\"text\":\".\n\"},{\"text\":\"CLIQUE AQUI\","
                + "\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"va"
                + "lue\":\"/duelo aceitar\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§7/duelo"
                + " aceitar\"}},{\"text\":\" para aceitar ou \"},{\"text\":\"CLIQUE AQUI\",\"bold\""
                + ":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/x"
                + "1 recusar\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§7/duelo recusar\"}}"
                + ",{\"text\":\" para rejeitar.\"}]";

        sendPacket(desafiado, JSON);
    }

    public static void sendJSONBroadcast() {
        String njs = "{\"text\":\"\"}\n";
        String JSON = "[\"\",{\"text\":\"O duelo entre \"},{\"text\":\"" + Arena.getDesafiante().getName() + " \",\"color\":\"yellow\"}," +
                "{\"text\":\"e \"},{\"text\":\"" + Arena.getDesafiado().getName() + " \",\"color\":\"yellow\"},{\"text\":\"começou!\\n\"}," +
                "{\"text\":\"Clique aqui\",\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"" +
                "value\":\"/duelo assistir\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"§7/duelo " +
                "assistir\"}},{\"text\":\" para ir até a arena assistir o combate.\"}]\n";

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            sendPacket(onlinePlayer, njs);
            sendPacket(onlinePlayer, JSON);
            sendPacket(onlinePlayer, njs);
        }
    }

    private static void sendPacket(Player p, String JSON) {
        IChatBaseComponent msg = IChatBaseComponent.ChatSerializer.a(JSON);
        PacketPlayOutChat packet = new PacketPlayOutChat(msg);

        EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        nmsPlayer.playerConnection.sendPacket(packet);
    }

    public static void fireworkAnimation(Player p) {
        Location loc = p.getLocation();
        Firework fw = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(1);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for(int i = 0; i < 5; i++){
            Firework fw2 = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public static JSONObject parseJSON(String jsonString) {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = (JSONObject) parser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
