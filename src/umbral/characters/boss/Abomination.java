package umbral.characters.boss;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.boss.base.Boss;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Abomination extends Boss {
    private SpriteDraw death = new SpriteDraw();

    private int phaseCD = 40,
                movementCD = 0,
                bossMSX,
                bossMSY;

    private double bossBob = 0,
                   bossBobC = 0.05,
                   rotation = 0.0,
                   spinSpeed = 0.5;

    private boolean played = false;

    public Abomination(String n, int x, int y, int hp, int dmg, int ms, int bc) {
        super(n, x, y, hp, dmg, ms, bc);
        this.bossMSX = ms;
        this.bossMSY = ms;
    }

    @Override
    public void draw(Graphics2D g, Map<String, BufferedImage> img) {
        super.draw(g, img);

        AffineTransform old = g.getTransform();
        for (int i = 0; i < 6; i++) {
            AffineTransform rotated = AffineTransform.getRotateInstance(Math.toRadians(rotation) + Math.toRadians(i * 60), getXPos() + (img.get("abominationLegs").getWidth() / 4) - 2, getYPos() + (img.get("abominationBody").getHeight() / 4) + 24);
            g.setTransform(rotated);
            g.drawImage(img.get("abominationLegs"), getXPos() - (img.get("abominationLegs").getWidth() / 4) + 2, (int) (getYPos() + (img.get("abominationBody").getHeight() / 4) - 24 + bossBob), null);
            g.setTransform(old);
        }

        if (getHealth() > 0) {
            g.drawImage(img.get("abominationBody"), getXPos() + (img.get("abominationBody").getWidth() / 4), (int) (getYPos() + (img.get("abominationBody").getHeight() / 4) + bossBob), null);
        } else {
            death.drawSprite(img.get("roundExplosion"), getXPos() + 7, (int) (getYPos() + bossBob) + 7, g, 100);
        }

        shot.forEach(shot -> shot.drawShot(g));
    }

    @Override
    public void math(Map<String, BufferedImage> img, int playerX, int playerY, AudioPlayer ap) {
        super.math(img, playerX, playerY, ap);

        if (getEntered() && getHealth() > getDefHealth() * 0.75) {
            attackOne(img.get("greyRound"), img.get("redRound"), ap);
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.75 && getHealth() > getDefHealth() * 0.5) {
            spinSpeed = 2.0;
            attackTwo(img.get("gearRound"), img.get("greyRound"), img.get("redRound"), ap);
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.5 && getHealth() > getDefHealth() * 0.25) {
            spinSpeed = (spinSpeed % 5.0 != 0) ? 5.0 : spinSpeed;
            attackThree(img.get("greyRound"), ap);
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.25 && getHealth() > 0) {
            spinSpeed = 10.0;
            attackFour(img.get("gearRound"), ap);
        } else if (!getEntered()) {
            enter(200);
        }

        //Boss bobbing
        bossBob += bossBobC;
        bossBobC = (bossBob > 0.5)  ? -Math.abs(bossBobC) :
                   (bossBob < -0.5) ? Math.abs(bossBobC) : bossBobC;

        //Phase shot delay(s)
        phaseCD -= (phaseCD > 0) ? 1 : 0;

        //movement delay
        movementCD -= (movementCD > 0) ? 1 : 0;

        rotation = (rotation >= 360) ? rotation % 360 : rotation + spinSpeed;

        if (getHealth() <= 0) {
            death.simpleIterate();

            bossMSX = (getXPos() > 600) ? -Math.abs(3) :
                      (getXPos() < 40)  ? Math.abs(3) : bossMSX;
            bossMSY = (getYPos() < 40) ? -Math.abs(3) :
                      (getYPos() > 800) ? 0 : 3;

            spinSpeed -= (spinSpeed > 0) ? 0.05 : 0;
            move(bossMSX, bossMSY);
        }

        if (getHealth() <= 0 & !played) {
            played = true;
            ap.play("explosion");
        }

        if (getHealth() <= 0 && ap.isPlaying(getName() + "Theme"))
            ap.stop(getName() + "Theme");
    }

    @Override
    public void reset() {
        super.reset();

        changeMS(Math.abs(getBossMS()));
        bossMSX = getBossMS();
        bossMSY = getBossMS();
        phaseCD = 20;
        movementCD = 0;

        bossBob = 0;
        bossBobC = 0.05;
        rotation = 0.0;
        spinSpeed = 0.5;

        played = false;

        death.reset();
    }

    @Override
    public boolean isBossDefeated() {
        if (getHealth() <= 0 && shot.size() == 0 && death.complete() && (getYPos() > 800 || getYPos() < -800)) {
            reset();
            return true;
        }

        return false;
    }

    /*###ATTACK PHASES###*/
    private void attackOne(BufferedImage t0, BufferedImage t1, AudioPlayer ap) {
        /*BULLETS START*/
        if (phaseCD % 20 == 0) {
            for (int i = 0; i < 6; i++) {
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 3, i * 60 - rotation, getBossDmg(), t0, false));
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 3, i * 60 + rotation, getBossDmg(), t1, false));
            }

            ap.play("WightShot");
        }

        phaseCDReset();
        /*BULLETS END*/
    }

    private void attackTwo(BufferedImage t0, BufferedImage t1, BufferedImage t2, AudioPlayer ap) {
        if (phaseCD >= 100) {
            int angle = (getRand().nextInt(1) == 0) ? -(getRand().nextInt(46)) : getRand().nextInt(46);
            shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 9, 290 + angle, getBossDmg(), t1, false));

            if (phaseCD % 5 == 0)
                ap.play("gearShot");
        } else if (phaseCD % 2 == 0) {
            int xPosition = getRand().nextInt(690) + 40;
            shot.add(new Bullet( xPosition, -10, getRand().nextInt(2) + 4, 90, getBossDmg(), t0, false));
        }

        if (phaseCD % 5 == 0) {
            for (int i = 0; i < 2; i++)
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 2, i * 180 + rotation, getBossDmg(), t2, false));

            ap.play("WightShot");
        }
        phaseCDReset();
        /*BULLETS END*/
    }

    private void attackThree(BufferedImage t0, AudioPlayer ap) {
        /*BULLETS START*/
        if (phaseCD % 5 == 0 && movementCD > 0) {
            spinSpeed = (movementCD >= 60) ? -Math.abs(spinSpeed) : Math.abs(spinSpeed);

            for (int i = 0; i < 6; i++)
                shot.add(new Bullet(getXPos() + 34 + (80 * Math.cos(Math.toRadians(i * 60 + rotation))), getYPos() + 42 + (80 * Math.sin(Math.toRadians(i * 60 + rotation))), 4, i * 60 + rotation, getBossDmg(), t0, false));
            ap.play("WightShot");
        }
        phaseCDReset();
        /*BULLETS END*/

        /*MOVEMENT START*/
        if (movementCD <= 0)
            move(getBossMS(), 0);

        //Once the boss reaches a point, have it shortly stop; then reverse the movement direction
        if (getXPos() >= 560) {
            changeMS(-Math.abs(getBossMS()));

            //Setting the count to 50 only when it is zero
            movementCD = (movementCD == 0) ? 120 : movementCD;
        } else if (getXPos() <= 128) {
            changeMS(Math.abs(getBossMS()));
            //Setting the count to 50 only when it is zero
            movementCD = (movementCD == 0) ? 120 : movementCD;
        }
        /*MOVEMENT END*/
    }

    private void attackFour(BufferedImage t0, AudioPlayer ap) {
        /*BULLET START*/
        if (getXPos() > 600 || getXPos() < 40 || getYPos() > 590 || getYPos() < 40) {
            for (int j = 0; j < 32; j++) {
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 1.5, j * 11.25, getBossDmg(), t0, false));
            }

            ap.play("explosion");

            //Boss losses an amount of HP until death
            setBossHealth((int) (getHealth() * 0.7));
        }
        phaseCDReset();
        /*BULLET END*/

        /*MOVEMENT START*/
        if (movementCD > 0) {
            move(bossMSX, bossMSY);

            bossMSX = (getXPos() > 600) ? -Math.abs(getRand().nextInt(6) + 3) :
                      (getXPos() < 40)  ? Math.abs(getRand().nextInt(6) + 3) : bossMSX;
            bossMSY = (getYPos() > 590) ? -Math.abs(getRand().nextInt(6) + 3) :
                      (getYPos() < 40)  ? Math.abs(getRand().nextInt(6) + 3) : bossMSY;
        } else {
            movementCD = 200;
        }
        /*MOVEMENT END*/
    }

    private void phaseCDReset() {
        if (phaseCD <= 0) {
            phaseCD = (getHealth() > getDefHealth() * 0.75)                                        ? 40 :
                      (getHealth() <= getDefHealth() * 0.75 && getHealth() > getDefHealth() * 0.5) ? 160 :
                      (getHealth() <= getDefHealth() * 0.5)                                        ? 120 : 80;
        }
    }
}
