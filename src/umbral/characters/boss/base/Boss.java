package umbral.characters.boss.base;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public abstract class Boss {
    private String name;

    private int xPos,
                yPos,
                bossHealth,
                bossDmg,
                bossMS,
                bossBC;

    private int defXPos,
                defYPos,
                defHealth,
                defMS;

    //private int[] executedAttacks = new int[4];

    private Random rand = new Random();
    protected ArrayList<Bullet> shot = new ArrayList<>();

    private boolean entered = false;

    public Boss(String name, int xPos, int yPos, int bossHealth, int bossDmg, int bossMS, int bossBC) {
        this.name = name;
        this.xPos = xPos;
        this.yPos = yPos;
        this.bossHealth = bossHealth;
        this.bossDmg = bossDmg;
        this.bossMS = bossMS;
        this.bossBC = bossBC;

        defXPos = xPos;
        defYPos = yPos;
        defHealth = bossHealth;
        defMS = bossMS;
    }

    public int getXPos() {return xPos;}
    public int getYPos() {return yPos;}
    public int getHealth() {return bossHealth;}
    public int getDefHealth() {return defHealth;}
    public int getBossDmg() {return bossDmg;}
    public int getBossMS() {return bossMS;}
    public int getBossBC() {return bossBC;}
    public boolean getEntered() {return  entered;}

    public void setBossHealth(int bossHealth) {this.bossHealth = bossHealth;}

    public ArrayList<Bullet> getShot() {return shot;}
    public void removeShot(int index) {shot.remove(index);}

    public void reset() {
        xPos = defXPos;
        yPos = defYPos;
        bossHealth = defHealth;
        bossMS = defMS;
        entered = false;
    }

    public void changeMS(int bossMS) {this.bossMS = bossMS;}
    public void move(int xChange, int yChange) {
        xPos += xChange;
        yPos += yChange;
    }

    public void targetedMove(int xTarget, int yTarget) {
        yPos += (yPos < yTarget) ? gcd(yPos, yTarget, bossMS) : (yPos > yTarget) ? -1 * gcd(yPos, yTarget, bossMS) : 0;
        xPos += (xPos < xTarget) ? gcd(xPos, xTarget, bossMS) : (xPos > xTarget) ? -1 * gcd(xPos, xTarget, bossMS) : 0;
    }

    public void dmgCalc(int dmg) {bossHealth -= (rand.nextInt(100) > bossBC) ? dmg:0;}

    public Random getRand() {return rand;}

    /**
     * recursive method to find the greatest common denominator.
     *
     * @param c The current coordinate number
     * @param dest The destination coordinate number
     * @param maxSpeed The highest value the GCD can be
     * @return THE GCD
     */
    public int gcd(int c, int dest, int maxSpeed) {
        return ((Math.abs(c - dest) % maxSpeed == 0)) ? maxSpeed : gcd(c, dest ,maxSpeed - 1);
    }

    public void enter(int finalYPos) {
        if (!entered) {
            yPos += (yPos < finalYPos) ? gcd(yPos, finalYPos, bossMS) : 0;
            entered = yPos >= finalYPos;
        }
    }

    public void enter(int finalXPos, int finalYPos) {
        if (!entered) {
            yPos += (yPos < finalYPos) ? gcd(yPos, finalYPos, bossMS) : 0;
            xPos += (xPos < finalXPos) ? gcd(xPos, finalXPos, bossMS) : 0;
            entered =yPos >= finalYPos && xPos >= finalXPos;
        }
    }

    public boolean isBossDefeated() {
        if (getHealth() <= 0 && shot.size() == 0) {
            reset();
            return true;
         }

        return false;
    }

    public void draw(Graphics2D g, Map<String, BufferedImage> img) {}
    public void math(Map<String, BufferedImage> img, int playerX, int playerY, AudioPlayer ap) {
        shot.forEach(Bullet::move);
        shot.removeIf(Bullet::offScreen);
    }

    public String getName() {return name;}
}
