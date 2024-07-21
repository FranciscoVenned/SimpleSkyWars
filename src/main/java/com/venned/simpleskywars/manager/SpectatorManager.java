package com.venned.simpleskywars.manager;

import com.venned.simpleskywars.utils.AbstractGameManager;
import com.venned.simpleskywars.utils.Utils;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class SpectatorManager extends SkyWarsGameManager {

    private final Utils utils = new Utils(plugin);

    public SpectatorManager(AbstractGameManager existingManager) {
        super(existingManager);
    }

    @Override
    public World getWorld() {
        return super.getWorld();
    }

    public void playerTeleportLobby(Plugin plugin){
        List<PlayerManager> list_players = this.playerManagers;
        for (PlayerManager player_manager : list_players) {
            player_manager.getPlayer().teleport(utils.getLobby(plugin));
        }
    }
}
