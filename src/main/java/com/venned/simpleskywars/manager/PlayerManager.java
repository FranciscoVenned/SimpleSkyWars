package com.venned.simpleskywars.manager;

import com.venned.simpleskywars.utils.AbstractPlayer;
import org.bukkit.entity.Player;

public class PlayerManager extends AbstractPlayer {

    public PlayerManager(Player player, SkyWarsGameManager game) {
        super(player, game);
    }

    public Player getPlayer() {
        return player;
    }

}
