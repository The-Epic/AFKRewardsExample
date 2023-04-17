package xyz.epicebic.afkrewards;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RewardRunnable extends BukkitRunnable implements Listener {

    private final AFKRewards plugin;
    private final Map<UUID, Instant> uuidLastReward = new HashMap<>();

    public RewardRunnable(AFKRewards plugin) {
        this.plugin = plugin;
    }


    @Override
    public void run() {
        Instant currentTime = Instant.now();
        for (Player player : plugin.getWorld().getPlayers()) {
            Instant lastReward = uuidLastReward.get(player.getUniqueId());
            if (lastReward == null || lastReward.isBefore(currentTime.minus(plugin.getDelay(), ChronoUnit.MINUTES))) {
                player.getInventory().addItem(plugin.getRewardStack());
                uuidLastReward.put(player.getUniqueId(), currentTime);
            }
        }
    }

    @EventHandler
    public void onPlayerWorldChange(PlayerChangedWorldEvent event ) {
        if (event.getPlayer().getWorld().equals(plugin.getWorld())) {
            uuidLastReward.put(event.getPlayer().getUniqueId(), Instant.now());
        } else {
            uuidLastReward.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        uuidLastReward.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().equals(plugin.getWorld())) {
            uuidLastReward.put(event.getPlayer().getUniqueId(), Instant.now());
        }
    }

}
