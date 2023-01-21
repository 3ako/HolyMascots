package holy.mascots.commands;

import holy.mascots.Mascots;
import holy.mascots.spheres.Sphere;
import holy.mascots.spheres.SphereManager;
import holy.mascots.spheres.SphereType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GiveSphereCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 4)  return false;
        final Player player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED+"Player offline");
            return true;
        }

        try {
            final SphereType type = SphereType.valueOf(args[2].toUpperCase());

            SphereManager manager = Mascots.getInstance().getSphereManager();
            Sphere sphere = manager.getSphere(args[1]);
            if (sphere == null) {
                sender.sendMessage(ChatColor.RED+"Invalid Sphere name");
                return true;
            }
            try {
                int amount = Integer.parseInt(args[3]);
                ItemStack stack = sphere.getItemStack(type);
                stack.setAmount(amount);
                player.getInventory().addItem(stack);
                sender.sendMessage(ChatColor.GREEN+"ok");
                return true;
            } catch (Exception e) {
                return false;
            }
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED+"Invalid Sphere Type");
            return true;
        }

    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> users = new ArrayList<String>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                users.add(player.getDisplayName());
            }
            return users;
        }
        if (args.length == 2) {
            ArrayList<String> spheres = new ArrayList<>();
            for (Sphere sphere : Mascots.getInstance().getSphereManager().getSpheres()) {
                spheres.add(sphere.getName());
            }
            return spheres;
        }
        if (args.length == 3) {
            ArrayList<String> types = new ArrayList<>();
            for (SphereType type : SphereType.values()) {
                types.add(type.name());
            }
            return types;
        }
        if (args.length == 4) {
            ArrayList<String> amount = new ArrayList<>();
            amount.add("1");
            amount.add("5");
            amount.add("10");
            amount.add("20");
            amount.add("30");
            return amount;
        }
        return Collections.emptyList();
    }
}
