package xyz.epicebic.afkrewards;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommandHandler implements TabExecutor {

    private final AFKRewards plugin;

    public CommandHandler(AFKRewards plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This is a player only command.");
            return true;
        }
        if (args.length < 1) {
            player.sendMessage("Please specific an argument.");
            return true;
        }
        switch (args[0]) {
            case "set" -> {
                ItemStack stack = player.getInventory().getItemInMainHand();
                if (stack == null || stack.getType().isAir()) {
                    player.sendMessage("Please hold an item not air.");
                    return true;
                }

                plugin.saveReward(stack);
                player.sendMessage("Item saved.");
                return true;
            }

            case "toggle" -> {
                plugin.toggleRewards();
                player.sendMessage("Rewards have now been toggled " + formatBoolean(plugin.isRewardEnabled()));
                return true;
            }

            case "reload" -> {
                plugin.reloadConfig();
                player.sendMessage("Config reloaded.");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (args.length == 1) return StringUtil.copyPartialMatches(args[0], List.of("set", "toggle", "reload"), new ArrayList<>());
        return Collections.emptyList();
    }

    private String formatBoolean(boolean bool) {
        return bool ? "on" : "off";
    }
}
