package com.mcml.space.util;

import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author Vlvxingze, SotrForgotten
 */
public class PlayerList implements Listener {
    private final static Set<String> PLAYER_NAMES = AzureAPI.newCaseInsensitiveSet(true);
    
    private final static List<JoinReactor> JOIN_REACTORS = Lists.newCopyOnWriteArrayList();
    private final static List<QuitReactor> QUIT_REACTORS = Lists.newCopyOnWriteArrayList();
    
    static {
        for (World world : Bukkit.getWorlds()) {
            for (Player each : world.getPlayers()) PLAYER_NAMES.add(each.getName());
        }
    }
    
    public static void bind(JavaPlugin plugin) {
        assert plugin != null;
        Bukkit.getPluginManager().registerEvents(new PlayerList(), plugin);
    }
    
    public static void bind(JoinReactor reactor) {
        JOIN_REACTORS.add(reactor);
    }
    
    public static void bind(QuitReactor reactor) {
        QUIT_REACTORS.add(reactor);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent evt) {
        Player player = evt.getPlayer();
        if (!player.isOnline()) return;
        PLAYER_NAMES.add(player.getName());
        
        if (!JOIN_REACTORS.isEmpty()) for (JoinReactor re : JOIN_REACTORS) re.react(evt);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent evt) {
        Player player = evt.getPlayer();
        PLAYER_NAMES.remove(player.getName());
        
        if (!QUIT_REACTORS.isEmpty()) for (QuitReactor re : QUIT_REACTORS) re.react(evt);
    }
    
    public static boolean contains(Player player) {
        return PLAYER_NAMES.contains(player.getName());
    }
    
    public static boolean contains(String username) {
        return PLAYER_NAMES.contains(username);
    }
    
    public static boolean isEmpty() {
        return PLAYER_NAMES.isEmpty();
    }
    
    public static int size() {
        return PLAYER_NAMES.size();
    }
    
    public static void forEach(Predicate<Player> consumer) {
        for (World world : Bukkit.getWorlds()) {
            for (Player each : world.getPlayers()) consumer.apply(each);
        }
    }
}
