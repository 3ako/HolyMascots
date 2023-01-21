package holy.mascots.spheres.effectHandler;

import holy.mascots.Mascots;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class ArmorEffectHandler implements EffectHandler, Listener {
    private final Map<Player, Integer> activePlayers = new HashMap<Player, Integer>();
    public ArmorEffectHandler() {
        Mascots.getInstance().getServer().getPluginManager().registerEvents(this, Mascots.getInstance());
    }

    @Override
    public void stop(Player player) {
        activePlayers.remove(player);
    }

    @Override
    public void start(Player player, int value) {
        activePlayers.put(player, value);
    }

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        final Integer value = activePlayers.get(player);
        if (value != null) {
            event.setDamage(event.getDamage()-value);
        }
    }
}
