package umbral.hud.shopkeepers;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShopkeeperLoad {
    private Map<Integer, ArrayList<Shopkeeper>> loadedShopkeeps = new HashMap<>();

    private void loadYAML() {
        Yaml yaml = new Yaml();

        InputStream inputStream = null;
        try {
            inputStream = Objects.requireNonNull(getClass().getClassLoader().getResource("yaml/shopkeepers.yaml")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Shopkeepers shopkeepers = yaml.loadAs(inputStream, Shopkeepers.class);

        for (Shopkeeper shopkeep : shopkeepers.getShopkeepers()) {
            ArrayList<Shopkeeper> temp = new ArrayList<>();
            if (loadedShopkeeps.get(shopkeep.getAfterBoss()) != null)
                temp = (loadedShopkeeps.get(shopkeep.getAfterBoss()).size() == 0) ? new ArrayList<>() : loadedShopkeeps.get(shopkeep.getAfterBoss());
            temp.add(shopkeep);
            loadedShopkeeps.put(shopkeep.getAfterBoss(), temp);
        }
    }

    public Map<Integer, ArrayList<Shopkeeper>> getLoadedShopkeepers() {
        loadYAML();
        return loadedShopkeeps;
    }
}
