package umbral.characters.enemy;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.enemy.frame.Enemy;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class BootlegMinion extends Enemy {
    private SpriteDraw sd = new SpriteDraw(),
                       energy = new SpriteDraw(),
                       death = new SpriteDraw();

    private boolean deathComplete = false;

    private double rotation = 0.0;

    public BootlegMinion(int x, int y, int hp, int dmg, int ms, int bc) {super(x, y, hp, dmg, ms, bc);}

    @Override
    public void draw(Graphics2D g, Map<String, BufferedImage> img) {
        super.draw(g, img);

        if (getHealth() > 0) {
            //sd.drawSprite(img.get("fireCast"), getXPos() - 8, getYPos() - 8, g, 96);
            energy.drawSprite(img.get("mediumEnergy"), getXPos(), getYPos(), g, 64);
            sd.drawSprite(img.get("bootlegMinion"), getXPos(), getYPos(), g, 64);
        } else {
            death.drawSprite(img.get("bloodExplosion"), getXPos() - 10, getYPos() - 10, g, 100);
        }
    }

    @Override
    public void anim(AudioPlayer ap) {
        super.anim(ap);

        if (sd.complete())
            sd.reset();
        sd.simpleIterate();

        if (energy.complete())
            energy.reset();
        energy.simpleIterate();

        rotation += (rotation >= 360) ? -rotation : 1;

        if (getHealth() <= 0)
            death.simpleIterate();

        if (getHealth() <= 0 && !deathComplete && !death.complete()) {
            deathComplete = true;
            ap.play("bootlegMinionDeath");
        }
    }

    public boolean dead() {
        return getHealth() <= 0 && deathComplete && death.complete();
    }

    @Override
    public void attack(int playerX, int playerY, BufferedImage t0, BufferedImage t1, AudioPlayer ap) {
        super.attack(playerX, playerY, t0, t1, ap);

        //enemy basic attack patterns
        if (getAtkDel() > 0 && getHealth() > 0) {
            int eXPos = getXPos() + 16,
                eYPos = getYPos() + 16;

            /*
             * 0&1 -> Basic
             * 2&3 -> Aimed
             */
            switch(getAtkType()) {
                case 0:
                    if (getAtkDel() % 7 == 0 && getAtkDel() > 60) {
                        ap.play("bootlegMinionShot");
                        for (int i = 0; i < 3; i++) {
                            shot.add(new Bullet(eXPos, eYPos, 3, i * 120 - rotation + 30, getDamage(), t0, false));
                            shot.add(new Bullet(eXPos, eYPos, 3, i * 120 + rotation + 30, getDamage(), t0, false));
                        }
                    }
                    break;

                case 1:
                    if (getAtkDel() % 10 == 0 && getAtkDel() >= 20) {
                        ap.play("bootlegMinionShot");
                        for (int i = 0; i < 3; i++) {
                            shot.add(new Bullet(eXPos, eYPos, 2, 0.1, 4, i * 120 + rotation, 0.5, getDamage(), t0, false));
                        }
                    }
                    break;

                case 2:
                    if (getAtkDel() % 70 == 0) {
                        ap.play("bootlegMinionWarp");
                        for (double i = 0; i < 2 * Math.PI; i += Math.PI / 32) {
                            double x = 64 * Math.sin(i * 2),
                                   y = 64 * Math.cos(i);
                            double angleToPlayer1 = Math.toDegrees(Math.atan2(playerY - (eYPos + y), playerX - (eXPos + x)));
                            shot.add(new Bullet(eXPos + x, eYPos + y, 0, 0.05, 4, angleToPlayer1, 0, getDamage(), t1, false));
                        }
                    }
                    break;

                case 3:
                    if (getAtkDel() % 60 == 0) {
                        ap.play("bootlegMinionWarp");
                        for (double i = 0; i < 2 * Math.PI; i += Math.PI / 16) {
                            double x = 32 * Math.sin(i),
                                    y = 32 * Math.cos(i);
                            double angleToPlayer1 = Math.toDegrees(Math.atan2(playerY - (eYPos + y), playerX - (eXPos + x)));
                            shot.add(new Bullet(eXPos + x, eYPos + y, 0, 0.05, 4, angleToPlayer1, 0, getDamage(), t1, false));
                        }
                    }
                    break;
            }

        }

        reduceDel();
    }
}
