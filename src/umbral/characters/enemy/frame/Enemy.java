package umbral.characters.enemy.frame;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public abstract class Enemy {
    private int xPos,
                yPos;

    private Random rand = new Random();

    private int health,
                damage,
                movementSpeed,
                atkType,
                atkDefDel = rand.nextInt(60) + 101,
                atkDel = atkDefDel;

    private double blockChance;

    private boolean doneMoving = false;

    public ArrayList<Bullet> shot = new ArrayList<>();

    public Enemy(int xPos, int yPos, int health, int damage, int movementSpeed, double blockChance) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.health = health;
        this.damage = damage;
        this.movementSpeed = movementSpeed;
        this.blockChance = blockChance;

        atkType = rand.nextInt(4);
    }

    public int getXPos() {return xPos;}
    public int getYPos() {return yPos;}

    /**
     * Move enemy left or right
     *
     * @param dir takes in either "left"(0) or "right"(1)
     */
    public void moveX(int dir) {
        switch(dir) {
            case 0:
                xPos -= movementSpeed;
                break;

            case 1:
                xPos += movementSpeed;
                break;

            default:
                System.out.println("NOT VALID DIRECTION");
                break;
        }
    }

    /**
     * Move enemy up or down
     *
     * @param dir takes in either "up"(0) or "down"(1)
     */
    public void moveY(int dir) {
        switch(dir) {
            case 0:
                yPos -= movementSpeed;
                break;

            case 1:
                yPos += movementSpeed;
                break;

            default:
                System.out.println("NOT VALID DIRECTION");
                break;
        }
    }

    public int getHealth() {return health;}
    public int getDamage() {return damage;}
    public int getMovementSpeed() {return movementSpeed;}
    public double getBlockChance() {return blockChance;}
    public int getAtkType() {return atkType;}

    public int getAtkDel() {return atkDel;}
    public void reduceDel() {atkDel -= (atkDel > 0) ? 1:-atkDefDel;}

    /**
     * @param pos1 y-position for attack type 0
     * @param pos2 y-position for attack type 1
     * @param pos3 y-position for attack type 2
     * @param pos4 y-position for attack type 3
     *
     * @return null
     */
    public int finalDestY(int pos1, int pos2, int pos3, int pos4) {
        int retVal = 0;

        switch (atkType) {
            case 0:
                retVal = pos1;
                break;

            case 1:
                retVal = pos2;
                break;

            case 2:
                retVal = pos3;
                break;

            case 3:
                retVal = pos4;
                break;
        }

        return retVal;
    }

    public void setDoneMoving(boolean doneMoving) {this.doneMoving = doneMoving;}
    public boolean isDoneMoving() {return doneMoving;}

    public boolean dead() {return health <= 0;}

    public void dmgCalc(int dmg) {health -= (health > 0 && rand.nextInt(100) > blockChance) ? dmg:0;}

    public void draw(Graphics2D g, Map<String, BufferedImage> img) {}
    public void anim(AudioPlayer ap) {
        shot.removeIf(Bullet::offScreen);
    }

    public ArrayList<Bullet> getShot() {return shot;}
    public void removeShot(Bullet bullet) {shot.remove(bullet);}
    public void attack(int playerX, int playerY, BufferedImage t0, BufferedImage t1, AudioPlayer ap) {}
}
