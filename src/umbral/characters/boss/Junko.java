package umbral.characters.boss;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.boss.base.Boss;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

public class Junko extends Boss {
    private SpriteDraw sd = new SpriteDraw(),
                       death = new SpriteDraw();

    private final int PHASE_THREE_TIMER_DEF = 640;

    private int phaseCD = 40,
                movementCD = 0,
                phaseThreeTimerOne = PHASE_THREE_TIMER_DEF,
                phaseThreeTimerTwo = PHASE_THREE_TIMER_DEF;

    private double bossShotAngle = 0,
                   bossBob = 0,
                   bossBobC = 0.05;

    private boolean played = false;

    public Junko(String n, int x, int y, int hp, int dmg, int ms, int bc) {super(n, x, y, hp, dmg, ms, bc);}

    @Override
    public void draw(Graphics2D g, Map<String, BufferedImage> img) {
        super.draw(g, img);

        if (getHealth() > 0) {
            sd.drawSprite(img.get("junko"), getXPos(), (int) (getYPos() + bossBob), g, 128);
        } else {
            death.drawSprite(img.get("vortexExplosion"), getXPos() + 8, (int) (getYPos() + bossBob) + 8, g, 100);
        }

        shot.forEach(shot -> shot.drawShot(g));
    }

    @Override
    public void math(Map<String, BufferedImage> img, int playerX, int playerY, AudioPlayer ap) {
        super.math(img, playerX, playerY, ap);

        if (getEntered() && getHealth() > getDefHealth() * 0.75) {
            attackOne(img.get("redRound"), img.get("blueRound"), img.get("greyRound"), ap);
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.75 && getHealth() > getDefHealth() * 0.5) {
            if (getBossMS() < 0)
                changeMS(Math.abs(getBossMS()));

            attackTwo(img.get("redRound"), img.get("blueRound"), img.get("greyRound"), playerX, playerY, ap);
            bossShotAngle += 1.6; //rotation
        } else if (getEntered() && getHealth() <= getDefHealth() * 0.5 && getHealth() > getDefHealth() * 0.25) {
            attackThree(img.get("redRound"), img.get("blueRound"), img.get("greyRound"), playerX, playerY, ap);

            //Movement timers
            phaseThreeTimerOne -= (getYPos() >= 225 && phaseThreeTimerOne > 0) ? 1 : 0;
            phaseThreeTimerOne = (getYPos() <= -100 && phaseThreeTimerOne <= 0) ? PHASE_THREE_TIMER_DEF : phaseThreeTimerOne;

            phaseThreeTimerTwo -= (getYPos() <= -100 && phaseThreeTimerTwo > 0) ? 1 : 0;
            phaseThreeTimerTwo = (getYPos() >= 225 && phaseThreeTimerTwo <= 0) ? PHASE_THREE_TIMER_DEF : phaseThreeTimerTwo;
        }else if (getEntered() && getHealth() <= getDefHealth() * 0.25 && getHealth() > 0) {
            attackFour(img.get("redRound"), img.get("blueRound"), img.get("greyRound"), ap);
        } else if (!getEntered()) {
            enter(175);
            sd.changeFrame(1, 1);
        }

        //Boss bobbing
        bossBob += bossBobC;
        bossBobC = (bossBob > 0.5) ? -Math.abs(bossBobC) :
                   (bossBob < -0.5) ? Math.abs(bossBobC) :
                   bossBobC;

        //Phase shot delay(s)
        phaseCD -= (phaseCD > 0) ? 1 : 0;

        //movement delay
        movementCD -= (movementCD > 0) ? 1 : 0;

        if (getHealth() <= 0)
            death.simpleIterate();

        if (getHealth() <= 0 & !played) {
            played = true;
            ap.play("JunkoDeath");
        }

        if (getHealth() <= 0 && ap.isPlaying("JunkoTheme"))
            ap.stop("JunkoTheme");
    }

    @Override
    public void reset() {
        super.reset();

        phaseCD = 20;
        movementCD = 0;
        phaseThreeTimerOne = PHASE_THREE_TIMER_DEF;
        phaseThreeTimerTwo = PHASE_THREE_TIMER_DEF;

        bossShotAngle = 0;
        bossBob = 0;
        bossBobC = 0.05;

        played = false;

        sd.reset();
        death.reset();
    }

    @Override
    public boolean isBossDefeated() {
        if (getHealth() <= 0 && shot.size() == 0 && death.complete()) {
            reset();
            return true;
        }

        return false;
    }

    /*###ATTACK PHASES###*/
    private void attackOne(BufferedImage t0, BufferedImage t1, BufferedImage t2, AudioPlayer ap) {
        /*BULLETS START*/
        if (phaseCD <= 0 || ((phaseCD % 20 == 0 || phaseCD % 20 == 10) && movementCD > 0)) {
            ap.play("magicShot"); //Plays a bullet firing sound effect
        }

        //red/blue
        if (movementCD > 0) {
            for (int i = 0; i < 12; i++) {
                if (phaseCD % 20 == 0) {
                    shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 4, 30 * i, getBossDmg(), t0, false));
                } else if (phaseCD % 20 == 10) {
                    shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 4, 30 * i + 16, getBossDmg(), t1, false));
                }
            }
        }

        //Gray shots which appear as the boss moves
        if (phaseCD == 0 && !(movementCD > 0)) {
            for (int i = 0; i < 6; i++) {
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 1, i * 15 + 45, getBossDmg(), t2, false));
            }
        }

        phaseCDReset();
        /*BULLETS END*/

        /*MOVEMENT START*/
        //Once the movement delay <= 0
        if (movementCD <= 0) {
            sd.reset();
            move(getBossMS(), 0);

            //Determining which boss animation to use
            if (getBossMS() < 0) {
                sd.changeFrame(2, 0);
            } else {
                sd.changeFrame(2,3);
            }
        }

        //Once the boss reaches a point, have it shortly stop; then reverse the movement direction
        if (getXPos() >= 560) {
            changeMS(-Math.abs(getBossMS()));
            sd.anim(3, 0, 3, 2);

            //Setting the count to 50 only when it is zero
            movementCD = (movementCD == 0) ? 60 : movementCD;
        } else if (getXPos() <= 128) {
            changeMS(Math.abs(getBossMS()));
            sd.anim(3, 0, 3, 2);

            //Setting the count to 50 only when it is zero
            movementCD = (movementCD == 0) ? 60 : movementCD;
        }
        /*MOVEMENT END*/
    }

    private void attackTwo(BufferedImage t0, BufferedImage t1, BufferedImage t2, int playerX, int playerY, AudioPlayer ap) {
        /*BULLETS START*/
        //Creates the spinning vortex of bullets
        if (phaseCD % 5 == 0 && ((phaseCD <= 45 && phaseCD >= 5) || (phaseCD <= 145 && phaseCD >= 105)) && getXPos()== 328 && getYPos() == 225) {
            for (int i = 0; i < 6; i++) {
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 3, 60 * i + ((phaseCD <= 45) ? -bossShotAngle : bossShotAngle), getBossDmg(), (phaseCD <= 45) ? t0 : t1, false));
            }

            ap.play("magicShot");
        }

        //Creates wave directed towards player
        if ((phaseCD == 0) && getXPos() == 328 && getYPos() == 225) {
            for (int i = 0; i < 6; i++) {
                double angleToPlayer = Math.toDegrees(Math.atan2(playerY - getYPos(), playerX - getXPos())) + 7 + getRand().nextInt(2) - 4;
                shot.add(new Bullet(getXPos() + 34, getYPos() + 34, 1, i * 15 + angleToPlayer - 45, getBossDmg(), t2, false));
            }

            ap.play("magicShot"); //Plays a bullet firing sound effect
        }

        phaseCDReset();
        /*BULLETS END*/

        /*MOVEMENT START*/
        if (getXPos() > 328) {
            sd.reset();
            sd.changeFrame(2, 0);

            move(-gcd(getXPos(), 328, getBossMS()), 0);
        } else if (getXPos() < 328) {
            sd.reset();
            sd.changeFrame(2, 3);

            move(gcd(getXPos(), 328, getBossMS()), 0);
        }

        //Moving the boss downwards until y = 225
        if (getYPos() < 225) {
            move(0, gcd(getYPos(), 225, getBossMS()));
        }

        //Once the boss is stationary, have the sprite undergo the spell casting animation
        if (getXPos() == 328 && getYPos() == 225) {
            sd.anim(3, 0, 3, 2);
        }
        /*MOVEMENT END*/
    }

    private void attackThree(BufferedImage t0, BufferedImage t1, BufferedImage t2, int playerX, int playerY, AudioPlayer ap) {
        /*BULLETS START*/
        //Bullets which appear at the top and bottom of the screen
        if (phaseCD <= 0 && getYPos() <= -100) {
            for (int i = 0; i < 12; i++) {
                shot.add(new Bullet((i * 65) + 15, -5, 3, 90, getBossDmg(), t1, false));
                shot.add(new Bullet((i * 65) + 50, 725, 3, 270, getBossDmg(), t0, false));
            }

            ap.play("magicShot");
        } else if (phaseCD == 30 && getYPos() >= 225) { //Ring of bullets which appear when the boss is on-screen
            for (int i = 0; i < 9; i++) {
                shot.add(new Bullet((playerX) + 120 * Math.cos(Math.toRadians(i * 40)), (playerY) + 120 * Math.sin(Math.toRadians(i * 40)), -1, 40 * i, getBossDmg(), t2, false));
            }

            ap.play("magicShot");
        }

        phaseCDReset();
        /*BULLETS END*/

        /*MOVEMENT START*/
        if (getYPos() > -100 && phaseThreeTimerOne <= 0) {
            move(0, -getBossMS());
        } else if (getYPos() < 225 && phaseThreeTimerTwo <= 0) {
            move(0, getBossMS());
        }

        //Having the boss undergo the spell casting animation
        if (getYPos() >= 225) {
            sd.anim(3, 0, 3, 2);
        } else {
            sd.reset();
        }
        /*MOVEMENT END*/
    }

    private void attackFour(BufferedImage t0, BufferedImage t1, BufferedImage t2, AudioPlayer ap) {
        //Randomizing the source of some bullet streams
        int originX = getXPos() + 34,
            originY = getYPos() + 34;
        if (phaseCD % 10 == 0 && phaseCD > 45) {
            ap.play("magicShot");
            for (int i = 0; i < 32; i++) {
                shot.add(new Bullet(originX, originY, 2, 11.25 * i, getBossDmg(), t1, false));
            }
        } else if (phaseCD % 10 == 5 && phaseCD > 50){
            for (int i = 0; i < 32; i++) {
                shot.add(new Bullet(originX, originY, 2, (11.25 * i) + 5.625, getBossDmg(), t1, false));
            }
        }

        if (getHealth() <= getDefHealth() * 0.125) {
            if (phaseCD % 10 == 0 && phaseCD < 35 && phaseCD > 5) {
                ap.play("magicShot");
                for (int i = 0; i < 64; i++) {
                    shot.add(new Bullet(originX, originY, 2, (5.625 * i) + 2.8125, getBossDmg(), t0, false));
                }
            }
        }

        /*if ((phaseCD % 40 == 0) || ((getHealth() <= getDefHealth() * 0.15 && (phaseCD % 5 == 0)) && ((phaseCD % 40 == 0) || (phaseCD % 40 == 20))) || (getHealth() <= getDefHealth() * 0.05 && phaseCD % 5 == 0)) {
            ap.play("magicShot");
        }

        //Part one: rings which originate around the boss
        if (phaseCD % 40 == 0) {
            //Creating the ring of bullets
            for (int i = 0; i < 15; i++) {
                shot.add(new Bullet((originX + randomX) + (50 * Math.cos(Math.toRadians(i * 24))), (originY + randomY) + (50 * Math.sin(Math.toRadians(i * 24))), 3, 24 * i, getBossDmg(), t1, false));
            }
        }

        //Part two: rings which originate from both sides of the boss
        if (getHealth() <= getDefHealth() * 0.15 && (phaseCD % 5 == 0)) {
            if (phaseCD % 40 == 0) {
                for (int i = 0; i < 15; i++) {
                    shot.add(new Bullet(528 + randomX + 50 * Math.cos(Math.toRadians(i * 24)), originY + randomY + 50 * Math.sin(Math.toRadians(i * 24)), 4, 24 * i, getBossDmg(), t0, false));
                }
            } else if (phaseCD % 40 == 20) {
                for (int i = 0; i < 15; i++) {
                    shot.add(new Bullet(228 + randomX + 50 * Math.cos(Math.toRadians(i * 24)), originY + randomY + 50 * Math.sin(Math.toRadians(i * 24)), 4, 24 * i, getBossDmg(), t0, false));
                }
            }
        }

        //part three; stream of bullets
        if (getHealth() <= getDefHealth() * 0.05 && phaseCD % 5 == 0) {
            for (int i = 0; i < 15; i++) {
                shot.add(new Bullet(originX, originY, 6, 24 * i, getBossDmg(), t2, false));
            }
        }*/

        phaseCDReset();

        if (getYPos() < 250) {
            sd.changeFrame(2, 3);

            move(0 ,gcd(getYPos(), 250, 9));
        } else {
            sd.changeFrame(1, 0);
        }
    }

    private void phaseCDReset() {
        if (phaseCD <= 0) {
            phaseCD = (getHealth() > getDefHealth() * 0.75)                                        ? 40 :
                      (getHealth() <= getDefHealth() * 0.75 && getHealth() > getDefHealth() * 0.5) ? 160 :
                      (getHealth() <= getDefHealth() * 0.5)                                        ? 120 : 80;
        }
    }
}
