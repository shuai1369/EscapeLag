package com.mcml.space.patches;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mcml.space.config.Patches;
import com.mcml.space.util.AzureAPI;

public class AntiBedExplode implements Listener {

    @EventHandler
    public void CheckInterackBed(PlayerInteractEvent e) {
        if (Patches.noBedExplore == true) {
            Player p = e.getPlayer();
            Block block = e.getClickedBlock();
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (block.getType() == Material.BED_BLOCK) {
                    if (p.getWorld().getEnvironment() == World.Environment.NETHER || p.getWorld().getEnvironment() == World.Environment.THE_END) {
                        e.setCancelled(true);
                        AzureAPI.log(p, Patches.AntiBedExplodeTipMessage);
                    }
                }
            }
        }
    }
}
// TODO handle explore event?