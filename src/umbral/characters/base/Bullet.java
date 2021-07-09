package umbral.characters.base;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet {
    //damage
    private int damage,
                size;

    //Bullet Coordinates and speed
    private double xShot,
                   yShot,
                   speed,
                   angle,
                   angleChange,
                   speedChange,
                   maxSpeed;

    private BufferedImage bulletImg;

    //Explosive Bullet?
    private boolean explosive;

    //Initializing a new empty SpriteDraw object
    private SpriteDraw sd;

    /**
     * Bullet constructor to be used for standard bullets drawn using java's Graphics system
     *
     * @param xShot  x-coordinate of the bullet
     * @param yShot  y-coordinate of the bullet
     * @param speed  speed of the bullet
     * @param angle  the angle of the bullets direction in degrees
     */
    public Bullet (double xShot, double yShot, double speed, double angle, int damage, BufferedImage bulletImg, boolean explosive) {
        this.xShot = xShot;
        this.yShot = yShot;
        this.speed = speed;
        this.angle = Math.toRadians(angle);
        this.damage = damage;
        this.bulletImg = bulletImg;
        this.explosive = explosive;
    }

    /**
     * Bullet constructor to be used for standard bullets drawn using java's Graphics system
     *
     * INCLUDES ANGLE AND SPEED CHANGE VALUES
     *
     * @param xShot  x-coordinate of the bullet
     * @param yShot  y-coordinate of the bullet
     * @param speed  speed of the bullet
     * @param speedChange acceleration of the bullet
     * @param maxSpeed terminal speed of the bullet
     * @param angle the initial angle of the bullet
     * @param angleChange the rate at which the angle changes
     * @param bulletImg  the image the bullet will be drawn with
     */
    public Bullet (double xShot, double yShot, double speed, double speedChange, double maxSpeed, double angle, double angleChange, int damage, BufferedImage bulletImg, boolean explosive) {
        this.xShot = xShot;
        this.yShot = yShot;
        this.speed = speed;
        this.speedChange = speedChange;
        this.maxSpeed = maxSpeed;
        this.angle = Math.toRadians(angle);
        this.angleChange = Math.toRadians(angleChange);
        this.angle = Math.toRadians(angle);
        this.damage = damage;
        this.bulletImg = bulletImg;
        this.explosive = explosive;
    }

    /**
     * Bullet constructor  with a delay to be used for bullets which will use a defined image.
     * Intended for standard vertical movement.
     *
     * @param xShot  x-coordinate of the bullet
     * @param yShot  y-coordinate of the bullet
     * @param speed  speed of the bullet
     * @param image  the image the bullet will be drawn with
     * @param size  the integer dimensions of a single image frame
     */
    public Bullet(double xShot, double yShot, double speed, int angle, int damage, BufferedImage image, int size, boolean explosive, SpriteDraw sd) {
        this.xShot = xShot;
        this.yShot = yShot;
        this.speed = speed;
        this.angle = Math.toRadians(angle);
        this.damage = damage;
        this.bulletImg = image;
        this.size = size;
        this.explosive = explosive;
        this.sd = sd;
    }

    /**
     * Bullet constructor  with a delay to be used for bullets which will use a defined image.
     *
     * INCLUDES ANGLE AND SPEED CHANGE VALUES
     *
     * @param xShot  x-coordinate of the bullet
     * @param yShot  y-coordinate of the bullet
     * @param speed  speed of the bullet
     * @param speedChange acceleration of the bullet
     * @param maxSpeed terminal speed of the bullet
     * @param angle the initial angle of the bullet
     * @param angleChange the rate at which the angle changes
     * @param image  the image the bullet will be drawn with
     * @param size  the integer dimensions of a single image frame
     */
    public Bullet(double xShot, double yShot, double speed, double speedChange, double maxSpeed, int angle, double angleChange, int damage, BufferedImage image, int size, boolean explosive, SpriteDraw sd) {
        this.xShot = xShot;
        this.yShot = yShot;
        this.speed = speed;
        this.speedChange = speedChange;
        this.maxSpeed = maxSpeed;
        this.angle = Math.toRadians(angle);
        this.angleChange = Math.toRadians(angleChange);
        this.damage = damage;
        this.bulletImg = image;
        this.size = size;
        this.explosive = explosive;
        this.sd = sd;
    }

    /*===DRAW METHODS START===*/
    public void drawShot(Graphics2D g) {
        //saving the old default rotation
        AffineTransform oldRot = g.getTransform();

        //setting the rotation for the bullet only
        AffineTransform bulletRot = AffineTransform.getRotateInstance(angle + Math.toRadians(90), xShot, yShot);
        g.transform(bulletRot);
        if (size == 0) {
            g.drawImage(bulletImg, (int) (xShot - (bulletImg.getWidth() / 2)), (int) (yShot - (bulletImg.getHeight() / 2)), null);
        } else {
            sd.drawSprite(bulletImg, (int) xShot, (int) yShot, g, size);
        }

        //setting it so nothing else is rotated
        g.setTransform(oldRot);
    }

    //public void drawSpriteShot(Graphics2D g) {bullet.drawSprite((int)xShot, (int)yShot, g);}
    /*===DRAW METHODS END===*/

    //Bullet movement
    public void move() {
        speed += (Math.abs(speed) < Math.abs(maxSpeed)) ? speedChange : 0;
        angle += angleChange;

        //Using vector maths to properly calculate the x and y speeds needed to make the bullet move at the right angle
        xShot += speed * Math.cos(angle);
        yShot += speed * Math.sin(angle);
    }

    //Bullet animation
    public void bulletSpriteAnimation() {if (size > 0) sd.simpleIterate();}

    /*===CHANGE VALUES START===*/
    /*public void setX(double xShot) {this.xShot = xShot;}
    public void setY(double yShot) {this.yShot = yShot;}
    public void setSpeed(double speed) {this.speed = speed;}
    public void setSpeedChange(double speedChange) {this.speedChange = speedChange;}
    public void setMaxSpeed(double maxSpeed) {this.maxSpeed = maxSpeed;}
    public void setAngle(double angle) {this.angle = Math.toRadians(angle);}
    public void setAngleChange(double angleChange) {this.angleChange = Math.toRadians(angleChange);}
    public void setDamage(int damage) {this.damage = damage;}
    public void setBulletImg(BufferedImage bulletImg) {this.bulletImg = bulletImg;}
    public void setSize(int size) {this.size = size;}
    public void setExplosive (boolean explosive) {this.explosive = explosive;}
    public void setSd (SpriteDraw sd) {this.sd = sd;}*/

    public void change (double xShot, double yShot, double speed, double speedChange, double maxSpeed, int angle, double angleChange, int damage, BufferedImage image, int size, boolean explosive, SpriteDraw sd) {
        this.xShot = xShot;
        this.yShot = yShot;
        this.speed = speed;
        this.speedChange = speedChange;
        this.maxSpeed = maxSpeed;
        this.angle = Math.toRadians(angle);
        this.angleChange = Math.toRadians(angleChange);
        this.damage = damage;
        this.bulletImg = image;
        this.size = size;
        this.explosive = explosive;
        this.sd = sd;
    }
    /*===CHANGE VALUES END===*/

    /*===RETURN VALUES START===*/
    public double returnX() {return xShot;}
    public double returnY() {return yShot;}
    public double returnAngle() {return angle;}

    public int returnDmg() {
        boolean offBorder = (yShot < -20 || yShot > 740 || xShot < -20 || xShot > 740);
        return (offBorder) ? 0 : damage;
    }

    public boolean isExplosive() {return explosive;}
    /*===RETURN VALUES END===*/

    /*BULLET OFF SCREEN*/
    public boolean offScreen() {return (yShot < -40 || yShot > 760 || xShot < -40 || xShot > 760);}

    /*BULLET CLEANUP*/
    public void cleanUp() {
        this.xShot = -1000;
        this.yShot = -1000;
        this.speed = 0;
        this.speedChange = 0;
        this.maxSpeed = 0;
        this.angle = 0;
        this.angleChange = 0;
        this.damage = 0;
        this.bulletImg = null;
        this.size = 0;
        this.explosive = false;
        this.sd = null;
    }
}
