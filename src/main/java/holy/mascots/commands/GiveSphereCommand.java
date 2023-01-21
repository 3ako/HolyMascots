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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveSphereCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 3)  return false;
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
            ItemStack stack = sphere.getItemStack(type);
            player.getInventory().addItem(stack);
            sender.sendMessage(ChatColor.GREEN+"ok");
            return true;

        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED+"Invalid Sphere Type");
            return true;
        }

    }
}
