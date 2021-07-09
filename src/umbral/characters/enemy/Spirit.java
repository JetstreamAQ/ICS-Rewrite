package umbral.characters.enemy;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.enemy.frame.Enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Spirit extends Enemy {
    private SpriteDraw sd = new SpriteDraw(),
                       death = new SpriteDraw();

    private boolean deathComplete = false;

    public Spirit(int x, int y, int hp, int dmg, int ms, int bc) {super(x, y, hp, dmg, ms, bc);}

    @Override
    public void draw(Graphics2D g, Map<String, BufferedImage> img) {
        super.draw(g, img);

        if (getHealth() > 0) {
            sd.drawSprite(img.get("spiritSprite"), getXPos(), getYPos(), g, img.get("spiritSprite").getHeight());
        } else {
            death.drawSprite(img.get("explosion"), getXPos() - 10, getYPos() - 10, g, 100);
        }
    }

    @Override
    public void anim(AudioPlayer ap) {
        super.anim(ap);

        sd.simpleAnim();

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
        if (getAtkDel() <= 0 && getHealth() > 0) {
            int eXPos = getXPos() + 16,
                eYPos = getYPos() + 16;

            /*
             * 0&1 -> Basic
             * 2&3 -> Aimed
             */
            ap.play("magicShot");
            switch(getAtkType()) {
                case 0:
                    for (int i = 0; i < 3; i++) {
                        shot.add(new Bullet(eXPos, eYPos, 3, 75 + (i * 15), getDamage(), t0, false));
                    }
                    break;

                case 1:
                    for (int i = 0; i < 10; i++) {
                        shot.add(new Bullet(eXPos, eYPos, 3, (i * 36), getDamage(), t0, false));
                    }
                    break;

                case 2:
                    double angleToPlayer1 = Math.toDegrees(Math.atan2(playerY - eYPos, playerX - eXPos));
                    shot.add(new Bullet(eXPos, eYPos, 3, angleToPlayer1, getDamage(), t1, false));
                    break;

                case 3:
                    double angleToPlayer2 = Math.toDegrees(Math.atan2(playerY - eYPos, playerX - eXPos)) - 15;
                    for (int i = 0; i < 3; i++) {
                        shot.add(new Bullet(eXPos, eYPos, 3, angleToPlayer2 + (i * 15), getDamage(), t1, false));
                    }
                    break;
            }

        }
        reduceDel();
    }
}