package holy.mascots.spheres;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Iterator;

public class SphereEffectHandler implements Listener {
    private final SphereManager sphereManager;
    private final ArrayList<ActiveSphere> activeSpheres = new ArrayList<>();
    private BukkitTask monitorTask;
    public SphereEffectHandler(SphereManager sphereManager) {
        this.sphereManager = sphereManager;
        sphereManager.getPlugin().getServer().getPluginManager().registerEvents(this, sphereManager.getPlugin());
//        runScheduler();
        runSchedulerMonitor();
    }

    public void addSphere(Player player, ItemSphere sphere) {
        activeSpheres.add(new ActiveSphere(player, sphere));
    }

    public boolean isActiveSphere(ItemSphere sphere) {
        for (ActiveSphere s : activeSpheres) {
            if (s.getSphere() == sphere) return true;
        }
        return false;
    }

    public ItemSphere getActiveItemSphere(Player player) {
        ActiveSphere activeSphere = getActiveSphere(player);
        if (activeSphere == null) return null;
        return activeSphere.sphere;
    }

    public ActiveSphere getActiveSphere(Player player) {
        for (ActiveSphere s : activeSpheres) {
            if (s.getPlayer() == player) return s;
        }
        return null;
    }

    public void disableSphere(Player player) {
        ActiveSphere sphere = getActiveSphere(player);
        if (sphere == null) return;

    }

    private void runSchedulerMonitor() {
        monitorTask = Bukkit.getScheduler().runTaskTimerAsynchronously(sphereManager.getPlugin(), ()-> {
            final Iterator<ActiveSphere> iterator = activeSpheres.iterator();
            while (iterator.hasNext()) {
                final ActiveSphere sphere = iterator.next();
                if (sphere.isRemove()) {
                    iterator.remove();
                    continue;
                }
                ItemStack stack = sphere.player.getInventory().getItem(40);
                if (!sphereManager.isSphere(stack)) {
                    sphere.delete();
                    continue;
                }
                if (!sphere.sphere.comparison(stack)) {
                    sphere.delete();
                    System.out.println("DISABLE");
                }
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (getActiveItemSphere(player) != null) continue;
                ItemStack stack = player.getInventory().getItem(40);
                if (stack == null) continue;
                ItemSphere itemSphere = sphereManager.getItemSphere(stack);
                if (itemSphere == null) continue;
                addSphere(player, itemSphere);
            }
        },0,20);
    }

    private void stopSchedulerMonitor() {
        if (monitorTask != null)
            monitorTask.cancel();
    }


    @Getter
    public static class ActiveSphere {
        private final Player player;
        private final ItemSphere sphere;
        private boolean remove;

        public ActiveSphere(Player player, ItemSphere sphere) {
            this.player = player;
            this.sphere = sphere;
            for (SphereEffect effect : sphere.getType().getEffects()) {
                effect.start(player, effect.getValue());
            }
        }

        public void delete() {
            for (SphereEffect effect : sphere.getType().getEffects()) {
                effect.stop(player);
            }
            this.remove = true;
        }
    }
}
