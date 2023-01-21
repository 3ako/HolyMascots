package holy.mascots.spheres;

import holy.mascots.spheres.utils.NBTEditor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor @Getter
public class Sphere {
    private String name;
    private final List<Type> types;

    public ItemStack getItemStack(SphereType t) {
        return getItemStack(t, false);
    }
    public ItemStack getItemStack(SphereType t, boolean mascot) {
        Type type = getType(t);
        if (type == null) return null;

        ItemStack itemStack = new ItemStack(mascot?Material.TOTEM_OF_UNDYING:Material.PLAYER_HEAD);

        if (!mascot) {
            UUID hashAsId = new UUID(type.getTexture().hashCode(), type.getTexture().hashCode());
            itemStack = Bukkit.getUnsafe().modifyItemStack(itemStack, "{SkullOwner:{Id:\"" + hashAsId + "\",Properties:{textures:[{Value:\""
                    + type.getTexture() + "\"}]}}}");
        }

        ItemMeta meta = itemStack.getItemMeta();
        assert meta != null;
        if (mascot) {
            meta.setDisplayName(type.getMascot().getDisplayName());
            meta.setLore(type.getMascot().getLore());
        } else {
            meta.setDisplayName(type.getDisplayName());
            meta.setLore(type.getLore());
        }
        itemStack.setItemMeta(meta);

        itemStack = NBTEditor.set(itemStack, name, "sphere", "name");
        itemStack = NBTEditor.set(itemStack, type.type.name(), "sphere", "type");
        return itemStack;
    }

    public boolean isItemStack(ItemStack stack) {
        String name = NBTEditor.getString(stack, "sphere", "name");
        if (this.name.equals(name)) return true;
        return false;
    }

    public Type getType(SphereType type) {
        for (Type t : types) {
            if (t.type == type) return t;
        }
        return null;
    }
    @Getter @AllArgsConstructor
    public static class Type {
        private SphereType type;
        private String displayName;
        private List<String> lore;
        private SphereMascot mascot;
        private String texture;
        private final List<SphereEffect> effects;

        public boolean hasEffect(SphereEffectType type) {
            for (SphereEffect effect : effects) {
                if (effect.getType() == type) return true;
            }
            return false;
        }
    }
}
