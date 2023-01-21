package holy.mascots.spheres.effectHandler;

import org.bukkit.entity.Player;

public class SpeedEffectHandler implements EffectHandler{
    @Override
    public void stop(Player player) {
        player.setWalkSpeed(0.2F);
    }

    @Override
    public void start(Player player, int value) {
        float speed = player.getWalkSpeed()/100*value + player.getWalkSpeed();
        player.setWalkSpeed(speed > 1? 1:speed);
    }

}
