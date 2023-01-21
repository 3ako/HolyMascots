package holy.mascots.spheres.effectHandler;

import org.bukkit.entity.Player;

public interface EffectHandler {
    void stop(Player player);
    void start(Player player, int value);
}
