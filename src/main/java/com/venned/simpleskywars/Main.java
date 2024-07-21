package com.venned.simpleskywars;

import com.grinderwolf.swm.api.SlimePlugin;
import com.grinderwolf.swm.plugin.SWMPlugin;
import com.venned.simpleskywars.commands.MainCommand;
import com.venned.simpleskywars.loader.GameManagerLoader;
import com.venned.simpleskywars.manager.PlayerManager;
import com.venned.simpleskywars.manager.ScoreboardManager;
import com.venned.simpleskywars.manager.SkyWarsGameManager;
import com.venned.simpleskywars.placeholder.SkyWarsPlaceholder;
import com.venned.simpleskywars.utils.MapUtils;
import com.venned.simpleskywars.utils.PortalUtils;
import com.venned.simpleskywars.utils.RegisterListeners;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;


public final class Main extends JavaPlugin implements Listener {

    SlimePlugin slimePlugin;
    ScoreboardManager scoreboardManager;
    GameManagerLoader loader;
    RegisterListeners registerListeners;
    BukkitTask scoreboardUpdateTask;
    MapUtils mapUtils;
    PortalUtils portalUtils;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("PlaceholderAPI detected, registering placeholders...");
            new SkyWarsPlaceholder(this).register();
        }

        loader = new GameManagerLoader(this);
        mapUtils = new MapUtils();
        slimePlugin = SWMPlugin.getInstance();
        scoreboardManager = new ScoreboardManager();
        portalUtils = new PortalUtils(this);


        getCommand("skywars").setExecutor(new MainCommand(this, mapUtils, portalUtils));

        scoreboardManager.loadConfig(this);
        registerListeners = new RegisterListeners(this, portalUtils);
        getServer().getPluginManager().registerEvents(this, this);

        scoreboardUpdateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            for (Player player : getServer().getOnlinePlayers()) {
                scoreboardManager.updateScore(player);
            }
        }, 0, 20);
    }

    @Override
    public void onDisable() {

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        scoreboardManager.setScoreboard(player);
    }

    @EventHandler
    public void onChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        scoreboardManager.changeWorld(player);
    }

    public GameManagerLoader getLoader() {
        return loader;
    }

    public PlayerManager getPlayerManager(Player player) {
        for (SkyWarsGameManager game : getLoader().getArenas()) {
            for (PlayerManager manager : game.getPlayersManager()) {
                if (manager.getPlayer().equals(player)) {
                    return manager;
                }
            }
        }
        return null;
    }

    public SlimePlugin getSlimePlugin() {
        return slimePlugin;
    }

    /*
    @EventHandler
    public void onChat(BlockBreakEvent event)  {
        String world_name = event.getPlayer().getWorld().getName();
        World world = event.getPlayer().getWorld();
        System.out.println("world_name " + world_name);

        World defaultWorld = (World)Bukkit.getWorlds().get(0);
        Location spawnLocation = defaultWorld.getSpawnLocation();

        event.getPlayer().teleport(spawnLocation);

        if (Bukkit.unloadWorld(world, true)) {
            event.getPlayer().sendMessage("world unloaded");
        }

        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                WorldsConfig config = ConfigManager.getWorldConfig();
                WorldData worldData = (WorldData)config.getWorlds().get(world_name);

                if(worldData != null) {
                    try {
                        SlimeLoader loader = SWMPlugin.getInstance().getLoader(worldData.getDataSource());
                        SlimeWorld slimeWorld =  slimePlugin.loadWorld(loader, world_name, true, worldData.toPropertyMap());
                        SWMPlugin.getInstance().generateWorld(slimeWorld);
                    } catch (CorruptedWorldException | NewerFormatException |  WorldInUseException | UnknownWorldException | IOException e){
                        e.printStackTrace();
                    }

                }
            }
        }, 120);
    }

     */
}
