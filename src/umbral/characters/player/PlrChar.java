package umbral.characters.player;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class PlrChar {
    private List<Player> characters;

    private void loadYAML() {
        Yaml yaml = new Yaml();

        InputStream inputStream = null;
        try {
            inputStream = Objects.requireNonNull(getClass().getClassLoader().getResource("yaml/plr/plrChar.yml")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Players players = yaml.loadAs(inputStream, Players.class);
        characters = players.getPlrChars();
    }

    public List<Player> getCharacters() {
        loadYAML();
        return characters;
    }
}
