package holy.mascots;

import holy.mascots.commands.GiveSphereCommand;
import holy.mascots.spheres.SphereManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Mascots extends JavaPlugin {
    @Getter
    private static Mascots instance;
    @Getter
    private SphereManager sphereManager;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        new Config(getConfig());
        sphereManager = new SphereManager(this, getConfig().getList("spheres"));
        getServer().getPluginCommand("givesphere").setExecutor(new GiveSphereCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
