package umbral.characters.player;

import umbral.audio.AudioPlayer;
import umbral.characters.base.SpriteDraw;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Player {
    private String name,
                   flavor,
                   desc;

    //individual stats
    private int health,
                spreadMod,
                damage,
                attackSpeed,
                movementSpeed,
                blockChance;

    private double creditMod;

    private SpriteDraw spriteDraw = new SpriteDraw(),
                       death = new SpriteDraw();

    private boolean deathComplete = false;

    //IGNORED
    private int deathType;

    //java bean necessities
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getFlavor() {return flavor;}
    public void setFlavor(String flavor) {this.flavor = flavor;}

    public String getDesc() {return desc;}
    public void setDesc(String flavor) {this.desc = flavor;}

    public int getHealth() {return health;}
    public void setHealth(int health) {this.health = health;}

    public int getSpreadMod() {return spreadMod;}
    public void setSpreadMod(int spreadMod) {this.spreadMod = spreadMod;}

    public double getCreditMod() {return creditMod;}
    public void setCreditMod(double creditMod) {this.creditMod = creditMod;}

    public int getDamage() {return damage;}
    public void setDamage(int damage) {this.damage = damage;}

    public int getAttackSpeed() {return attackSpeed;}
    public void setAttackSpeed(int attackSpeed) {this.attackSpeed = attackSpeed;}

    public int getMovementSpeed() {return movementSpeed;}
    public void setMovementSpeed(int movementSpeed) {this.movementSpeed = movementSpeed;}

    public int getBlockChance() {return blockChance;}
    public void setBlockChance(int blockChance) {this.blockChance = blockChance;}

    public int getDeathType() {return deathType;}
    public void setDeathType(int deathType) {this.deathType = deathType;}

    public void drawChar(Map<String, BufferedImage> img, String charID, int playerHealth, int x, int y, Graphics2D g) {
        if (playerHealth > 0) {
            spriteDraw.drawSprite(img.get(charID), x, y, g, img.get(charID).getHeight());
        } else {
            death.drawSprite(img.get("roundExplosion"), x - 8, y - 8, g, 100);
        }
    }

    public void animateChar(AudioPlayer ap, int playerHealth) {
        spriteDraw.simpleAnim();
        if (playerHealth <= 0)
            death.simpleIterate();

        if (playerHealth <= 0 && !deathComplete) {
            deathComplete = true;
            ap.play("explosion");
        }
    }
}
