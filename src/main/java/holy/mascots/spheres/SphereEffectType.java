package holy.mascots.spheres;

import holy.mascots.spheres.effectHandler.*;
import org.bukkit.entity.Player;

public enum SphereEffectType {
    SPEED(new SpeedEffectHandler()),
    DAMAGE(new DamageEffectHandler()),
    ARMOR(new ArmorEffectHandler()),
    RUSH(new RushEffectHandler());

    private final EffectHandler handler;
    SphereEffectType(EffectHandler handler) {
        this.handler = handler;
    }

    public void start(Player player, int value) {
        handler.start(player, value);
    }

    public void stop(Player player) {
        handler.stop(player);
    }
}
