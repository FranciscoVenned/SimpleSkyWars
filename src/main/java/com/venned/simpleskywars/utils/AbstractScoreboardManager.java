package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.data.ScoreboardStorage;
import com.venned.simpleskywars.face.IScoreboardManager;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardObjective;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardObjective;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public abstract class AbstractScoreboardManager implements IScoreboardManager {

    protected void removeScoreboard(Player player) {
        Scoreboard scoreboard = ScoreboardStorage.playerScoreboards.get(player);
        if (scoreboard != null) {
            ScoreboardObjective objective = scoreboard.getObjective("zagd");
            if (objective != null) {
                Packet<?> removePacket = new PacketPlayOutScoreboardObjective(objective, 1); // 1 for REMOVE
                sendPacket(removePacket, player);
            }
        }
    }

    protected void sendPacket(Packet<?> packet, Player player) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
