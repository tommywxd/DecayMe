package cc.tommyw.decayme;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Start implements CommandExecutor {
    private final Main plugin;
    public Start(Main plugin) {
        this.plugin = plugin;
    }
    public boolean onCommand(CommandSender pSender, Command pCmd, String arg2, String[] arg3) {
        File started = new File("started");
        if(started.exists()) {
            pSender.sendMessage(ChatColor.RED + "There is already an ongoing game. Please wait for it to finish to start another one.");
            return true;
        }
        if(!started.exists()) {
            try {
                started.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bukkit.broadcastMessage(ChatColor.RED + "Game starting in 10 seconds!");
        String source = "DecayMap";
        File srcDir = new File(source);
        String destination = "DecayMinigame";
        File destDir = new File(destination);
        try {
            FileUtils.copyDirectory(srcDir, destDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        plugin.getServer().createWorld(new WorldCreator("DecayMinigame"));
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Player alive : Bukkit.getServer().getOnlinePlayers()) {
                    if (!alive.getGameMode().equals(GameMode.ADVENTURE)) {
                        alive.setGameMode(GameMode.ADVENTURE);
                    }
                }
                for (Player alive : Bukkit.getServer().getOnlinePlayers()) {
                    alive.teleport(Bukkit.getWorld("DecayMinigame").getSpawnLocation());
                }
                Bukkit.broadcastMessage(ChatColor.RED + "Game started!");
            }
        };
        task.runTaskLater(plugin, 10 * 20);
        return true;
    }
}
