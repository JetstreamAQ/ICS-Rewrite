package umbral.background;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * used mainly with the image of a 4 pointed star.
 * image to be loaded into a BufferedImage and passed into constructor.
 */
public class Star {
    //Star positions
    private int starX,
                starY;

    //Used to rotate the star
    private double angle = 0,
                   angleChange;

    private float opacity = 0.0f; //star opacity
    private boolean flashed = false; //used to make the star shine only once

    //private File starImg = new File ("resources/images/star.png");
    //private URL starImg = getClass().getClassLoader().getResource("images/background/star.png");
    private BufferedImage starImg;

    /**
     * Defines a star which will eventually be drawn on a window
     *
     * @param starX  The x-coordinate of the star
     * @param starY  The y-coordinate of the star
     */
    public Star (int starX, int starY, BufferedImage starImg) {
        this.starX = starX;
        this.starY = starY;
        this.starImg = starImg;

        //Randomizing the direction the star rotates
        Random rand = new Random();
        switch (rand.nextInt(2) + 1) {
            case 1: angleChange = -0.02; break;
            case 2: angleChange = 0.02; break;
        }
    }

    //Drawing the star
    public void draw (Graphics2D g) {

        AffineTransform origRot = g.getTransform(); //Saving the default rotation

        /*Drawing an image with centered coordinates*/
        //Allowing the star to spin
        AffineTransform starRot = AffineTransform.getRotateInstance(angle,
                                                             starX + starImg.getWidth()/2,
                                                             starY + starImg.getHeight()/2);
        g.transform(starRot);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
        g.drawImage(starImg, starX, starY, null);

        //Resetting the opacity for anything which is drawn after
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));

        //Resetting the rotation
        g.setTransform(origRot);
    }

    //Used to rotate the star and have it flash
    public void animate () {
        /*
         * Once the opacity reaches a value of '1.0f' a boolean indicating that the star has flashed is
         * set to true and the star's opacity fades all the way down to '0.0f'
         **/
        if (opacity < 0.9f && !flashed) {
            opacity += 0.1f;
        } else if (opacity > 0.1f && !flashed) {
            flashed = true;
        } else if (opacity > 0.1f && flashed) {
            opacity -= 0.1f;
        }

        //Rotates the star in the randomly picked direction; clockwise/counter-clockwise
        angle += angleChange;
        angle = (angle >= 360) ? 0:angle;

        //star slowly "falling"
        starY++;
    }

    //Returns true once the star has flashed
    public boolean starDone () {return opacity <= 0.1f && flashed;}
}
