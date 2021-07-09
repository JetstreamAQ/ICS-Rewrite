package umbral.imageLoader;

import org.yaml.snakeyaml.Yaml;
import umbral.imageLoader.images.Images;
import umbral.imageLoader.images.Image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import javax.imageio.ImageIO;

public class ImageLoader {
    private Map<String, BufferedImage> loadedImages = new TreeMap<>();

    private BufferedImage bufferLoad(String path) {
        BufferedImage temp = null;
        URL url = getClass().getClassLoader().getResource(path);

        try {
            temp = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    private void loadYAML() {
        Yaml yaml = new Yaml();

        InputStream inputStream = null;
        try {
            inputStream = Objects.requireNonNull(getClass().getClassLoader().getResource("yaml/images.yml")).openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Images images = yaml.loadAs(inputStream, Images.class);

        for (Image image : images.getImages()) {
            BufferedImage imageToLoad = bufferLoad(image.getImageURL());
            loadedImages.put(image.getImageTag(), imageToLoad);
        }
    }

    public Map<String, BufferedImage> getLoadedImages() {
        loadYAML();
        return loadedImages;
    }
}
