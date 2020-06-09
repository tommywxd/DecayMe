package cc.tommyw.decayme;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

public class Events implements Listener {
    private final Main plugin;

    public Events(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void Move(PlayerMoveEvent e) {
        File started = new File("started");
        Player p = e.getPlayer();
        if (started.exists()) {
            Location loc = e.getPlayer().getLocation().clone().subtract(0, 1, 0);
            Block b = loc.getBlock();
            if (e.getPlayer().getGameMode() == GameMode.SPECTATOR) return;
            if (b.getType() == Material.BEDROCK) return;
            Location loc3 = e.getPlayer().getLocation().clone();
            Block b3 = loc3.getBlock();
            if(b3.getType() == Material.getMaterial(44)) {
                p.setVelocity(p.getLocation().getDirection().multiply(20));
                p.setVelocity(new Vector(p.getVelocity().getX(), 1.0, p.getVelocity().getZ()));
                p.sendMessage("No slabs for you!");
                BukkitRunnable task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        World w = Bukkit.getWorld("DecayMinigame");
                        w.spawnEntity(loc3, EntityType.PRIMED_TNT);
                    }
                };
                task.runTaskLater(plugin, 20);
            }
            BukkitRunnable task = new BukkitRunnable() {
                @Override
                public void run() {
                    b.setType(Material.LAVA);
                }
            };
            task.runTaskLater(plugin, 20);
            if (b.getType() == Material.SPONGE) {
                Random r = new Random();
                int low = 1;
                int high = 10;
                int result = r.nextInt(high - low) + low;
                if (result == 1) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 5 * 20, 1));
                    return;
                }
                if (result == 2) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 5 * 20, 1));
                    return;
                }
                if (result == 3) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 5 * 20, 1));
                    return;
                }
                if (result == 4) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 1));
                    return;
                }
                if (result == 5) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 5 * 20, 1));
                    return;
                }
                if (result == 6) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 1));
                    return;
                }
                if (result == 7) {
                    b.setType(Material.AIR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 1));
                    return;
                }
                if (result == 8) {
                    Location loc2 = e.getPlayer().getLocation().clone().add(0, 99, 0);
                    Block b2 = loc2.getBlock();
                    b2.setType(Material.GLASS);
                    b.setType(Material.AIR);
                    p.teleport(p.getLocation().add(0, 100, 0));
                    p.sendMessage("Do you like heights? Too bad!");
                }
            }
        }
    }

    @EventHandler
    public void Join(PlayerJoinEvent e) {
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    @EventHandler
    public void Die(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            p.spigot().respawn();
            p.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage(ChatColor.RED + p.getDisplayName() + ChatColor.WHITE + " was caught by the lava and is now a spectator.");
            Integer owo = 0;
            for (Player alive : Bukkit.getServer().getOnlinePlayers()) {
                if (alive.getGameMode().equals(GameMode.ADVENTURE)) {
                    owo++;
                }
            }
            if (owo == 1 | owo == 0) {
                String wname = "N/A";
                for (Player alive2 : Bukkit.getServer().getOnlinePlayers()) {
                    if (alive2.getGameMode().equals(GameMode.ADVENTURE)) {
                        wname = alive2.getDisplayName();
                    }
                    Bukkit.broadcastMessage(ChatColor.RED + wname + ChatColor.GOLD + " has won the game!");
                    for (Player alive : Bukkit.getServer().getOnlinePlayers()) {
                        alive.teleport(Bukkit.getWorld("world").getSpawnLocation());
                        alive.setGameMode(GameMode.SPECTATOR);
                    }
                    World mworld = Bukkit.getWorld("DecayMinigame");
                    Bukkit.getServer().unloadWorld("DecayMinigame", true);
                    try {
                        FileUtils.deleteDirectory(new File("DecayMinigame"));
                    } catch (IOException e1) {

                    }
                    File started = new File("started");
                    started.delete();
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageEvent(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player) e.getEntity();
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL && e.getEntity().getWorld().getName().equalsIgnoreCase("DecayMinigame")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void Leave(PlayerQuitEvent e) {
        Integer owo = 0;
        for (Player alive : Bukkit.getServer().getOnlinePlayers()) {
            if (alive.getGameMode().equals(GameMode.ADVENTURE)) {
                owo++;
            }
        }
        if (owo == 1) {
            for (Player alive2 : Bukkit.getServer().getOnlinePlayers()) {
                if (alive2.getGameMode().equals(GameMode.ADVENTURE)) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + "All other players have quit! Game Over.");
                }
                for (Player alive : Bukkit.getServer().getOnlinePlayers()) {
                    alive.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    alive.setGameMode(GameMode.SPECTATOR);
                }
                World mworld = Bukkit.getWorld("DecayMinigame");
                Bukkit.getServer().unloadWorld("DecayMinigame", true);
                try {
                    FileUtils.deleteDirectory(new File("DecayMinigame"));
                } catch (IOException e1) {

                }
                File started = new File("started");
                started.delete();
            }
            }
        }
    }
