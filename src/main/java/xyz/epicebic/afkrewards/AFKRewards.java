package xyz.epicebic.afkrewards;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class AFKRewards extends JavaPlugin {

    private ItemStack rewardStack;
    private World world;
    private int delay;
    private boolean enabled = true;
    private RewardRunnable runnable;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        reload();
        getCommand("afkreward").setExecutor(new CommandHandler(this));
    }

    private void reload() {
        if (getConfig().isSet("item")) {
            rewardStack = getConfig().getItemStack("item");
        }
        world = Bukkit.getWorld(getConfig().getString("world", "world"));
        delay = getConfig().getInt("delay");
        enabled = getConfig().getBoolean("enabled");
        if (runnable != null) runnable.cancel();
        if (enabled) {
            runnable = new RewardRunnable(this);
            Bukkit.getPluginManager().registerEvents(runnable, this);
            runnable.runTaskTimer(this, 60 * 20, 60 * 20);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ItemStack getRewardStack() {
        return rewardStack;
    }

    public void saveReward(ItemStack rewardStack) {
        this.rewardStack = rewardStack;
        getConfig().set("item", rewardStack);
        saveConfig();
    }

    public World getWorld() {
        return world;
    }

    public int getDelay() {
        return delay;
    }

    public boolean isRewardEnabled() {
        return enabled;
    }

    public void toggleRewards() {
        this.enabled = !this.enabled;
    }
}
