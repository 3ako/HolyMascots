package holy.mascots.spheres;

import holy.mascots.spheres.utils.NBTEditor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter @AllArgsConstructor
public class ItemSphere {
    private final Sphere sphere;
    private final Sphere.Type type;

    public boolean comparison(ItemSphere is) {
        return (is.sphere == sphere && is.type == type);
    }

    public boolean comparison(ItemStack stack) {
        String type = NBTEditor.getString(stack, "sphere", "type");
        String name = NBTEditor.getString(stack, "sphere", "name");
        return (this.sphere.getName().equals(name) && this.type.getType().name().equals(type));
    }
}
