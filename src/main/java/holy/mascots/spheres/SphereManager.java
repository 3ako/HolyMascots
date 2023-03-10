package holy.mascots.spheres;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import holy.mascots.Config;
import holy.mascots.spheres.utils.NBTEditor;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SphereManager {
    @Getter
    private final List<Sphere> spheres =  new ArrayList<>();
    @Getter
    private final SphereEffectHandler effectHandler;
    private final Map<SphereType, Map<Player,ItemStack>> craftCash = new HashMap<>();
    @Getter
    private final JavaPlugin plugin;
    public SphereManager(@NonNull JavaPlugin plugin, @NonNull List<?> sphereList) {
        this.plugin = plugin;
        parse(sphereList);
        plugin.getServer().getPluginManager().registerEvents(new SphereManagerBukkitListener(), plugin);
        effectHandler = new SphereEffectHandler(this);

        craftCash.put(SphereType.EPIC, new HashMap<>());
        craftCash.put(SphereType.LEGEND, new HashMap<>());
    }

    public boolean isSphere(ItemStack stack) {
        if (stack == null) return false;
        return NBTEditor.contains(stack, "sphere");
    }

    public ItemSphere getRandomSphereItem(SphereType type) {
        final Sphere sphere = spheres.get(new Random().nextInt(spheres.size()));
        return new ItemSphere(sphere, sphere.getType(type));
    }
    public Sphere getRandomSphere() {
        return spheres.get(new Random().nextInt(spheres.size()));
    }
    public ItemSphere getItemSphere(ItemStack stack) {
        SphereType type = getSphereType(stack);
        if (type == null) return null;
        Sphere sphere = getSphere(stack);
        if (sphere == null) return null;
        return new ItemSphere(sphere, sphere.getType(type));
    }

    public Sphere getSphere(String name) {
        for (Sphere sphere : spheres) {
            if (sphere.getName().equalsIgnoreCase(name)) return sphere;
        }
        return null;
    }

    public Sphere getSphere(ItemStack stack) {
        if (!isSphere(stack)) return null;
        for (Sphere sphere : spheres) {
            if (sphere.isItemStack(stack)) return sphere;
        }
        return null;
    }

    public SphereType getSphereType(ItemStack stack) {
        String type = NBTEditor.getString(stack, "sphere", "type");
        if (type != null) {
            return SphereType.valueOf(type);
        }
        return null;
    }

    private void parse(List<?> sphereList) {
        for (Object o : sphereList) {
            if (!(o instanceof LinkedHashMap map)) continue;
            final String name = (String) map.get("name");
            final List<Sphere.Type> types = new ArrayList<>();
            for (LinkedHashMap type : (List<LinkedHashMap>) map.get("types")) {
                final SphereType typeType = SphereType.valueOf((String) type.get("type"));
                final String typeDisplayName = (String) type.get("display-name");
                final List<String> typeLore = (List<String>) type.get("lore");
                final String texture = (String) type.get("texture");

                final List<SphereEffect> effectList = new ArrayList<>();
                for (LinkedHashMap effect : (List<LinkedHashMap>) type.get("effects")) {
                    final SphereEffectType effectType = SphereEffectType.valueOf((String) effect.get("type"));
                    final int value = (int) effect.get("value");
                    effectList.add(new SphereEffect(effectType, value));
                }

                final LinkedHashMap mascotMap = (LinkedHashMap) type.get("mascot");
                final boolean mascotStatus = (boolean) mascotMap.get("enabled");
                final String mascotDisplayName = (String) mascotMap.get("display-name");
                final List<String> mascotLore = (List<String>) mascotMap.get("lore");

                final SphereMascot mascot = new SphereMascot(mascotDisplayName, mascotLore, mascotStatus);
                types.add(new Sphere.Type(typeType, typeDisplayName, typeLore, mascot, texture,effectList));
            }

            final Sphere sphere = new Sphere(name, types);
            spheres.add(sphere);
        }
    }

    public class SphereManagerBukkitListener implements Listener {

        @EventHandler
        private void inventoryClickEvent(InventoryClickEvent event) {
            if (event.getInventory().getType() != InventoryType.ANVIL) return;
            if (event.getSlot() != 2) return;
            ItemStack stack = event.getCurrentItem();
            ItemSphere sphere = getItemSphere(stack);
            if (sphere == null) return;
            if (craftCash.containsKey(sphere.getType().getType()))
                craftCash.get(sphere.getType().getType()).remove((Player) event.getWhoClicked());
        }

        @EventHandler
        private void omAnvil(PrepareAnvilEvent event) {
            final AnvilInventory inventory = event.getInventory();
            ItemStack stack1 = inventory.getItem(0);
            ItemStack stack2 = inventory.getItem(1);
            if (stack1 == null || stack2 == null) return;
            if (stack1.getAmount() != 1 || stack2.getAmount() != 1) return;
            final Player player = (Player) event.getView().getPlayer();

            if (stack2.getType()==Material.TOTEM_OF_UNDYING) {
                ItemSphere sphere1 = getItemSphere(stack1);
                if (!sphere1.getType().getMascot().isStatus()) return;
                ItemStack stack3 = sphere1.getSphere().getItemStack(sphere1.getType().getType(),true);
                event.setResult(stack3);
                if (Config.ANVIL_COST.MASCOT >= 41 && player.getLevel()>=Config.ANVIL_COST.MASCOT)
                    setInstantBuild(player, true);
                plugin.getServer().getScheduler().runTask(plugin, () -> inventory.setRepairCost(60));
                return;
            }

            ItemSphere sphere1 = getItemSphere(stack1);
            ItemSphere sphere2 = getItemSphere(stack2);
            if (sphere1.getType().getType() != sphere2.getType().getType()) return;
            Sphere sphere3 = getRandomSphere();
            int cost = 0;
            ItemStack result = null;
            switch (sphere1.getType().getType()) {
                case NORMAL -> {
                    Map<Player, ItemStack> epicCash = craftCash.get(SphereType.EPIC);
                    if (epicCash.containsKey(player)) {
                        result = epicCash.get(player);
                    } else {
                        result = sphere3.getItemStack(SphereType.EPIC);
                        epicCash.put(player, result);
                    }
                    cost = Config.ANVIL_COST.NORMAL;
                }
                case EPIC -> {
                    Map<Player, ItemStack> legendCash = craftCash.get(SphereType.LEGEND);
                    if (legendCash.containsKey(player)) {
                        result = legendCash.get(player);
                    } else {
                        result = sphere3.getItemStack(SphereType.LEGEND);
                        legendCash.put(player, result);
                    }
                    cost = Config.ANVIL_COST.EPIC;
                }
            }
            if (result == null) return;
            event.setResult(result);
            if (cost >= 41 && player.getLevel() >= cost)
                setInstantBuild(player, true);
            else if (player.getLevel() < cost)
                setInstantBuild(player, false);
            int finalCost = cost;
            plugin.getServer().getScheduler().runTask(plugin, () -> inventory.setRepairCost(finalCost));
        }

        public void setInstantBuild(Player player, boolean instantBuild) {
            PacketContainer packet = new PacketContainer(PacketType.Play.Server.ABILITIES);
            packet.getBooleans().write(0, player.isInvulnerable());
            packet.getBooleans().write(1, player.isFlying());
            packet.getBooleans().write(2, player.getAllowFlight());
            packet.getBooleans().write(3, instantBuild);
            packet.getFloat().write(0, player.getFlySpeed() / 2);
            packet.getFloat().write(1, player.getWalkSpeed() / 2);

            try {
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        @EventHandler
        private void onPlaceBlock(BlockPlaceEvent event) {
            ItemStack stack = event.getItemInHand();
            if (isSphere(stack)) {
                event.setCancelled(true);
            }
        }
    }
}
