package holy.mascots.spheres.effectHandler;

import holy.mascots.Mascots;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.leymooo.antirelog.Antirelog;
import ru.leymooo.antirelog.manager.PvPManager;

import java.util.HashMap;
import java.util.Map;

public class RushEffectHandler implements EffectHandler {
    private final PvPManager pvPManager;
    private final Map<Player, Integer> activePlayers = new HashMap<Player, Integer>();
    public RushEffectHandler() {
        pvPManager = Antirelog.getInstance().getPvpManager();
        Bukkit.getScheduler().runTaskTimer(Mascots.getInstance(), ()-> {
            for (Map.Entry<Player, Integer> entity : activePlayers.entrySet()) {
                addEffect(entity.getKey(), entity.getValue());
            }
        }, 0,10*20);
    }

    @Override
    public void stop(Player player) {
        activePlayers.remove(player);
        Bukkit.getScheduler().runTask(Mascots.getInstance(),()-> takeEffect(player));
    }

    @Override
    public void start(Player player, int value) {
        activePlayers.put(player, value);
        Bukkit.getScheduler().runTask(Mascots.getInstance(),()->addEffect(player, value));
    }

    private void addEffect(Player player, int value) {
        if (pvPManager.isInPvP(player)) return;
        PotionEffect effect = new PotionEffect(PotionEffectType.FAST_DIGGING, 13*20, value);
        player.addPotionEffect(effect);
    }

    private void takeEffect(Player player) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

}
