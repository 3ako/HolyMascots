package holy.mascots.spheres;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter @AllArgsConstructor
public class SphereMascot {
    private String displayName;
    private List<String> lore;
    private boolean status;
}
