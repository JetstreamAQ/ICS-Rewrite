package umbral.characters.boss;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.boss.base.Boss;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Bootleg extends Boss {
    private final int LASER_CAP = 300;

    private SpriteDraw sd = new SpriteDraw(),
                       death = new SpriteDraw();

    private int phaseCD = 0,
                movementCD = 0,
                laserCD = 0,
                pointerX = getXPos(),
                pointerY = getYPos();

    private double bossBob = 0,
                   bossBobC = 0.05,
                   rotation = 0.0,
                   spinSpeed = 0.75,
                   aimAngle = 0.0;

    private boolean played = false,
                    laserPhase = true;

    private int p2Movement = 0,
                p3Movement = 0;

    public Bootleg(String name, int xPos, int yPos, int bossHealth, int bossDmg, int bossMS, int bossBC) {
        super(name, xPos, yPos, bossHealth, bossDmg, bossMS, bossBC);
    }

    @Override
    public void draw(Graphics2D g, Map<String, BufferedImage> img) {
        super.draw(g, img);

        drawLasers(g);

        if (getHealth() > 0) {
            sd.drawSprite(img.get("bootlegBack"), getXPos() - 36, (int) (getYPos() - 75 + bossBob), g, 256);
            g.drawImage(img.get("bootleg"), getXPos() - 32, (int) (getYPos() - 32 + bossBob), null);
        } else {
            death.drawSprite(img.get("bloodExplosion"), getXPos() + 8, (int) (getYPos() + bossBob) + 8, g, 100);
        }

        shot.forEach(shot -> shot.drawShot(g));
    }

    private void drawLasers(Graphics2D g) {
        if (laserCD < LASER_CAP && phaseCD <= 0 && getEntered() && laserPhase) {
            g.setColor(Color.MAGENTA);
            g.setStroke(new BasicStroke((int) (15 * ((double) laserCD / LASER_CAP)), BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            g.drawLine(getXPos() + 32, getYPos() + 32, pointerX, pointerY);
            g.setStroke(new BasicStroke(1));
        } else if (getHealth() <= getDefHealth() * 0.8 && getHealth() > getDefHealth() * 0.6) {
            g.setColor(Color.RED);
            g.drawLine(getXPos() + 32, getYPos() + 32, pointerX, pointerY);
        }
    }

    @Override
    public void math(Map<String, BufferedImage> img, int playerX, int playerY, AudioPlayer ap) {
        super.math(img, playerX, playerY, ap);

        if (getEntered() && getHealth() > getDefHealth() * 0.8) {
            pointerX = playerX;
            pointerY = playerY;
            attackOne(img, ap, pointerX, pointerY);
            laserCDInc();
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.8 && getHealth() > getDefHealth() * 0.6) {
            if (laserPhase) {
                laserPhase = false;
                phaseCDReset();
            }
            spinSpeed = 0.5;
            attackTwo(img);
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.6 && getHealth() > getDefHealth() * 0.4) {
            if (!laserPhase) {
                laserPhase = true;
                phaseCD = 0;
                laserCD = 0;
            }
            pointerX = 328;
            pointerY = 720;
            attackThree(img, ap);
            laserCDInc();
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.4 && getHealth() > getDefHealth() * 0.2) {
            spinSpeed = 1.2;
            pointerX = playerX;
            pointerY = playerY;
            attackFour(img, ap);
            laserCDInc();
        } else if (getEntered() && getHealth() <= getDefHealth() * 2.0 && getHealth() > 0) {
            if (laserPhase) {
                laserPhase = false;
                phaseCDReset();
                spinSpeed = 1.5;
            }

            attackFive(img, ap);
        } else if (!getEntered()) {
            enter(328, 175);
        }

        //Boss bobbing
        bossBob += bossBobC;
        bossBobC = (bossBob > 0.5)  ? -Math.abs(bossBobC) :
                   (bossBob < -0.5) ? Math.abs(bossBobC) :
                                      bossBobC;

        //Phase shot delay(s)
        phaseCD -= (phaseCD > 0) ? 1 : 0;

        //movement delay
        movementCD -= (movementCD > 0) ? 1 : 0;

        //rotation
        rotation = (rotation >= 360) ? rotation % 360 : rotation + spinSpeed;

        sd.simpleIterate();
        if (sd.complete())
            sd.reset();

        if (getHealth() <= 0)
            death.simpleIterate();

        if (getHealth() <= 0 & !played) {
            played = true;
            ap.play("bootlegMinionDeath");
        }

        if (getHealth() <= 0 && ap.isPlaying("BootlegTheme"))
            ap.stop("BootlegTheme");
    }

    @Override
    public void reset() {
       super.reset();

       phaseCD = 0;
       laserPhase = true;
    }

    private void laserCDInc() {
        laserCD += (laserCD < LASER_CAP && phaseCD <= 0) ? 1 : 0;
    }

    private void phaseCDReset() {
        final int PHASE_ONE_CD = 150,
                  PHASE_TWO_CD = 360,
                  PHASE_THREE_CD = 150,
                  PHASE_FOUR_CD = 150,
                  PHASE_FIVE_CD = 180;
        if (phaseCD <= 0)
            phaseCD = (getHealth() > getDefHealth() * 0.8)                                          ? PHASE_ONE_CD :
                      (getHealth() <= getDefHealth() * 0.8 && getHealth() > getDefHealth() * 0.6)   ? PHASE_TWO_CD :
                      (getHealth() <= getDefHealth() * 0.6 && getHealth() > getDefHealth() * 0.4)   ? PHASE_THREE_CD :
                      (getHealth() <= getDefHealth() * 0.4 && getHealth() > getDefHealth() * 0.2)   ? PHASE_FOUR_CD : PHASE_FIVE_CD;
    }

    /*##### ATTACKS BEGIN #####*/
    /*##### ATTACKS BEGIN #####*/
    /*##### ATTACKS BEGIN #####*/
    /*##### ATTACKS BEGIN #####*/
    /*##### ATTACKS BEGIN #####*/

    private void attackOne(Map<String, BufferedImage> img, AudioPlayer ap, int playerX, int playerY) {
        if (phaseCD % 8 == 0 && phaseCD != 0) {
            for (int i = 0; i < 6; i++) {
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 60 - rotation + 30 + aimAngle, getBossDmg(), img.get("purpleRound"), false));
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 60 + rotation + 30 + aimAngle, getBossDmg(), img.get("purpleRound"), false));
            }
        }

        if (phaseCD % 80 == 0 && phaseCD != 0) {
            for (int i = 0; i < 64; i++) {
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 5.625 + rotation, getBossDmg(), img.get("blueRound"), false));
            }
        }

        if (phaseCD % 2 == 0 && phaseCD != 0) {
            for (int i = 0; i < 32; i ++) {
                double x = getXPos() + 32 + (16 * Math.cos(Math.toRadians(i * 11.25))),
                       y = getYPos() + 32 + (16 * Math.sin(Math.toRadians(i * 11.25))),
                       accel = getRand().nextInt(5) + 1;
                shot.add(new Bullet(x, y, 0, accel, 30, aimAngle, 0, getBossDmg(), img.get("bootlegLong"), false));
            }
        }

        //when the laser is active, reset the phase CD.  Else, update the angle to the player.
        if (laserCD == LASER_CAP) {
            laserCD = (phaseCD <= 0) ? 0 : laserCD;
            phaseCDReset();
            ap.play("bootlegLaser");
        } else if (laserCD != 0) {
            aimAngle = Math.toDegrees(Math.atan2(playerY - (getYPos() + 32), playerX - (getXPos() + 32)));
        }
    }

    private void attackTwo(Map<String, BufferedImage> img) {
        //TODO: Sound?
        //Hypotrochoid parameters
        int a = 1,
            c = 2,
            size = 64;
        double b = 0.1,
               rotationRad = Math.toRadians(rotation);
        double xPos = (a - b) * Math.cos(rotationRad) + c * Math.cos(((a / b) - 1) * rotationRad),
               yPos = (a - b) * Math.sin(rotationRad) - c * Math.sin(((a / b) - 1) * rotationRad),
               bulletX = size * xPos + (getXPos() + 32),
               bulletY = size * yPos + (getYPos() + 32);
        pointerX = (int) bulletX;
        pointerY = (int) bulletY;

        shot.add(new Bullet(bulletX, bulletY, 0, 0.05, 2, getRand().nextInt(360), 0, getBossDmg(), img.get("crimsonRound"), false));

        if (phaseCD == 1) {
            int lastP2Movement = p2Movement;
            do {
                p2Movement = getRand().nextInt(3);
            } while (p2Movement == lastP2Movement);
        }

        switch(p2Movement) {
            case 0: targetedMove(560, 75); break;
            case 1: targetedMove(120, 75); break;
            case 2: targetedMove(328, 280); break;
        }

        phaseCDReset();
    }

    private void attackThree(Map<String, BufferedImage> img, AudioPlayer ap) {
        if (phaseCD == 1)
            p3Movement = (p3Movement == 1) ? 0 : 1;

        switch(p3Movement) {
            case 0: targetedMove(560, 175); break;
            case 1: targetedMove(120, 175); break;
        }

        if (phaseCD % 8 == 0 && phaseCD != 0) {
            for (int i = 0; i < 9; i++) {
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 40 - rotation + 30 + aimAngle, getBossDmg(), img.get("purpleRound"), false));
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 40 + rotation + 30 + aimAngle, getBossDmg(), img.get("purpleRound"), false));
            }
        }

        if (phaseCD % 60 == 0 && phaseCD != 0) {
            for (int i = 0; i < 64; i++) {
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 5.625 + rotation, getBossDmg(), img.get("blueRound"), false));
            }
        }

        if (phaseCD % 2 == 0 && phaseCD != 0) {
            for (int i = 0; i < 32; i ++) {
                double x = getXPos() + 32 + (16 * Math.cos(Math.toRadians(i * 11.25))),
                       y = getYPos() + 32 + (16 * Math.sin(Math.toRadians(i * 11.25))),
                       accel = getRand().nextInt(5) + 1;
                shot.add(new Bullet(x, y, 0, accel, 30, aimAngle, 0, getBossDmg(), img.get("bootlegLong"), false));
            }
        }

        if (phaseCD == 0 && laserCD % 8 == 0 && (getXPos() != 560 || getXPos() != 120)) {
            ap.play("bootlegMinionShot");
            for (int i = 0; i < 6; i++) {
                double x = getXPos() + 32 + (32 * Math.cos(Math.toRadians(i * 60))),
                       y = getYPos() + 32 + (32 * Math.sin(Math.toRadians(i * 60)));
                shot.add(new Bullet(x, y, 4, i * 60 - rotation, getBossDmg(), img.get("crimsonRound"), false));
            }
        }

        //when the laser is active, reset the phase CD.
        if (laserCD == LASER_CAP && (getXPos() == 560 || getXPos() == 120)) {
            laserCD = (phaseCD <= 0) ? 0 : laserCD;
            phaseCDReset();
            ap.play("bootlegLaser");
        } else if (laserCD != 0) {
            aimAngle = Math.toDegrees(Math.atan2(pointerY - (getYPos() + 32), pointerX - (getXPos() + 32)));
        }
    }

    private void attackFour(Map<String, BufferedImage> img, AudioPlayer ap) {
        targetedMove(328, 250);

        if (getXPos() == 328 && getYPos() == 250 && laserCD % 60 == 0 && laserCD != 0) {
            ap.play("bootlegMinionWarp");
            for (double i = 0; i < 2 * Math.PI; i += Math.PI / 90) {
                //Hypotrochoid parameters
                int a = 1,
                        c = 2,
                        size = 32;
                double b = 0.1,
                       modRot = i + Math.toRadians(rotation);
                double xPos = (a - b) * Math.cos(modRot) + c * Math.cos(((a / b) - 1) * modRot),
                       yPos = (a - b) * Math.sin(modRot) - c * Math.sin(((a / b) - 1) * modRot),
                       bulletX = size * xPos + (getXPos() + 32),
                       bulletY = size * yPos + (getYPos() + 32);

                shot.add(new Bullet(bulletX, bulletY, 0, 0.5, 3, Math.toDegrees(modRot), 0, getBossDmg(), img.get("crimsonRound"), false));
            }
        }

        if (phaseCD % 2 == 0 && phaseCD != 0) {
            for (int i = 0; i < 32; i ++) {
                double x = getXPos() + 32 + (16 * Math.cos(Math.toRadians(i * 11.25))),
                       y = getYPos() + 32 + (16 * Math.sin(Math.toRadians(i * 11.25))),
                       accel = getRand().nextInt(5) + 1;
                shot.add(new Bullet(x, y, 0, accel, 30, aimAngle, 0, getBossDmg(), img.get("bootlegLong"), false));
            }
        }

        //when the laser is active, reset the phase CD.
        if (laserCD == LASER_CAP && getXPos() == 328 && getYPos() == 250) {
            laserCD = (phaseCD <= 0) ? 0 : laserCD;
            phaseCDReset();
            ap.play("bootlegLaser");
        } else if (laserCD != 0) {
            aimAngle = Math.toDegrees(Math.atan2(pointerY - (getYPos() + 32), pointerX - (getXPos() + 32)));
        }
    }

    private void attackFive(Map<String, BufferedImage> img, AudioPlayer ap) {
        targetedMove(328, 175);

        if (phaseCD % 4 == 0 && getYPos() == 175) {
            //TODO: Sound
            for (int i = 0; i < 4; i++) {
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 90 - rotation, getBossDmg(), img.get("purpleRound"), false));
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 90 + rotation, getBossDmg(), img.get("purpleRound"), false));
            }
        }

        if (phaseCD % 60 == 0 && phaseCD != 0 && phaseCD % 90 != 0) {
            for (int i = 0; i < 64; i++) {
                shot.add(new Bullet(getXPos() + 32, getYPos() + 32, 3, i * 5.625 + rotation, getBossDmg(), img.get("blueRound"), false));
            }
        }

        if (getYPos() == 175 && phaseCD % 90 == 0) {
            ap.play("bootlegMinionWarp");
            for (double i = 0; i < 2 * Math.PI; i += Math.PI / 90) {
                //Hypotrochoid parameters
                int a = 1,
                        c = 2,
                        size = 32;
                double b = 0.1,
                        modRot = i + Math.toRadians(rotation);
                double xPos = (a - b) * Math.cos(modRot) + c * Math.cos(((a / b) - 1) * modRot),
                        yPos = (a - b) * Math.sin(modRot) - c * Math.sin(((a / b) - 1) * modRot),
                        bulletX = size * xPos + (getXPos() + 32),
                        bulletY = size * yPos + (getYPos() + 32);

                shot.add(new Bullet(bulletX, bulletY, 0, 0.5, 3, Math.toDegrees(modRot), 0, getBossDmg(), img.get("crimsonRound"), false));
            }
        }

        phaseCDReset();
    }

    /*###### ATTACKS END ######*/
    /*###### ATTACKS END ######*/
    /*###### ATTACKS END ######*/
    /*###### ATTACKS END ######*/
    /*###### ATTACKS END ######*/
}
