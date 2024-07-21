package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.Main;
import com.venned.simpleskywars.events.call.PlayerInteractArenaListener;
import com.venned.simpleskywars.listeners.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RegisterListeners {

    protected final Plugin plugin;
    protected final PortalUtils portalUtils;

    public RegisterListeners(Plugin plugin, PortalUtils portalUtils) {
        this.plugin = plugin;
        this.portalUtils = portalUtils;
        registerListeners();
    }

    public void registerListeners() {
        plugin.getServer().getPluginManager().registerEvents(new PlayerKillListener(((Main) plugin)), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PortalListener((Main)plugin, portalUtils), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractArenaListener((Main) plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerInteractListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerDeathListener((Main)plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GameStartListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerLeaveListener((Main) plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new GameWonListener(plugin), plugin);
    }
}
