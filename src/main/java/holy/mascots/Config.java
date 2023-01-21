package holy.mascots;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    public Config(FileConfiguration file) {
        ANVIL_COST.EPIC = file.getInt("cost.epic");
        ANVIL_COST.MASCOT = file.getInt("cost.mascot");
        ANVIL_COST.NORMAL = file.getInt("cost.normal");
    }

    public static class ANVIL_COST {
        public static int NORMAL;
        public static int EPIC;
        public static int MASCOT;
    }
}
