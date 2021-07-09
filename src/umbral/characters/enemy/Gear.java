package umbral.characters.enemy;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.enemy.frame.Enemy;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Gear extends Enemy {
    private SpriteDraw sd = new SpriteDraw(),
                       cosmetic = new SpriteDraw(),
                       death = new SpriteDraw();

    private boolean deathComplete = false;

    private double rotation = 0.0;

    public Gear (int x, int y, int hp, int dmg, int ms, int bc) {
        super(x, y, hp, dmg, ms, bc);
    }

    @Override
    public void draw(Graphics2D g, Map<String, BufferedImage> img) {
        super.draw(g, img);

        if (getHealth() > 0) {
            cosmetic.drawSprite(img.get("mediumShine"), getXPos(), getYPos(), g, img.get("mediumShine").getHeight());
            AffineTransform old = g.getTransform();
            AffineTransform rotated = AffineTransform.getRotateInstance(Math.toRadians(rotation), getXPos() + 16, getYPos() + 16);
            g.setTransform(rotated);
            sd.drawSprite(img.get("gearSprite"), getXPos(), getYPos(), g, img.get("gearSprite").getHeight());
            g.setTransform(old);
        } else {
            death.drawSprite(img.get("verticalExplosion"), getXPos() - 10, getYPos() - 10, g, 100);
        }
    }

    @Override
    public void anim(AudioPlayer ap) {
        super.anim(ap);

        sd.simpleAnim();
        if (cosmetic.complete())
            cosmetic.reset();
        cosmetic.simpleIterate();
        rotation += (rotation >= 360) ? -rotation : 1;

        if (getHealth() <= 0)
            death.simpleIterate();

        if (getHealth() <= 0 && !deathComplete && !death.complete()) {
            deathComplete = true;
            ap.play("spiritDeath");
        }
    }

    public boolean dead() {
        return getHealth() <= 0 && deathComplete && death.complete();
    }

    @Override
    public void attack(int playerX, int playerY, BufferedImage t0, BufferedImage t1, AudioPlayer ap) {
        super.attack(playerX, playerY, t0, t1, ap);

        //enemy basic attack patterns
        if (getHealth() > 0 && isDoneMoving()) {
            int eXPos = getXPos() + 16,
                eYPos = getYPos() + 16;

            /*
             * 0 & 1 -> Basic
             * 2 & 3 -> Aimed
             */
            switch(getAtkType()) {
                case 0:
                    if (getAtkDel() % 40 == 0) {
                        for (int i = 0; i < 6; i++) {
                            shot.add(new Bullet(eXPos, eYPos, 3, (i * 60) + rotation, getDamage(), t0, false));
                        }

                        ap.play("gearShot");
                    }
                    break;

                case 1:
                    if (getAtkDel() % 20 == 0 && getAtkDel() >= 40) {
                        for (int i = 0; i < 2; i++) {
                            shot.add(new Bullet(eXPos, eYPos, 3, (i * 180) - rotation, getDamage(), t0, false));
                        }

                        ap.play("gearShot");
                    }
                    break;

                case 2:
                    if (getAtkDel() % 10 == 0 && getAtkDel() >= 40) {
                        double angleToPlayer1 = Math.toDegrees(Math.atan2(playerY - eYPos, playerX - eXPos));
                        shot.add(new Bullet(eXPos, eYPos, 3, angleToPlayer1, getDamage(), t1, false));

                        ap.play("gearShot");
                    }
                    break;

                case 3:
                    if (getAtkDel() <= 0) {
                        double angleToPlayer2 = Math.toDegrees(Math.atan2(playerY - eYPos, playerX - eXPos)) - 15;
                        for (int i = 0; i < 5; i++) {
                            shot.add(new Bullet(eXPos, eYPos, 3, angleToPlayer2 + (i * 15), getDamage(), t1, false));
                        }

                        ap.play("gearShot");
                    }
                    break;
            }
        }
        reduceDel();
    }
}
