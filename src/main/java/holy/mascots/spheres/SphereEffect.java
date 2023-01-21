package holy.mascots.spheres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter @AllArgsConstructor
public class SphereEffect {
    private SphereEffectType type;
    private int value;

    public void start(Player player, int value) {
        type.start(player, value);
    }

    public void stop(Player player) {
        type.stop(player);
    }
}
