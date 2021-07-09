package umbral.characters.items;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class ItemsLoad {
    public Items items;

    public void loadYAML() {
        Yaml yaml = new Yaml();

        InputStream inputStream = null;
        try {
            inputStream = Objects.requireNonNull(getClass().getClassLoader().getResource("yaml/items.yml")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        items = yaml.loadAs(inputStream, Items.class);
    }
}
