package umbral;

import umbral.background.Star;
import umbral.characters.base.Bullet;
import umbral.characters.base.VFX;
import umbral.characters.boss.Abomination;
import umbral.characters.boss.Bootleg;
import umbral.characters.boss.Junko;
import umbral.characters.boss.base.Boss;
import umbral.characters.enemy.BootlegMinion;
import umbral.characters.enemy.Gear;
import umbral.characters.enemy.Spirit;
import umbral.characters.enemy.frame.Enemy;
import umbral.characters.items.ActiveEffects;
import umbral.characters.items.Item;
import umbral.hud.HUD;
import umbral.hud.shopkeepers.Shop;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

class Game {
    //control variables; used to grab input from the Main class
    boolean moveUp,
            moveDown,
            moveLeft,
            moveRight,
            isShooting,
            toggleSpecial,
            focusToggle;

    //player chosen val
    String charID;
    int charNum;

    /*PLAYER START*/
    //base values
    int deathType,
        playerHealth,
        playerMaxHealth,
        baseSpreadMod,
        baseDmg,
        baseAS,
        baseMS,
        baseBlockChance;

    double baseCreditMod;

    private double baseAttackDel = 25,
                   attackDel = baseAttackDel;

    //statics like C?
    private double invuln = 0;
    private int circleDiameter = 0;

    //bonus values
    private int bonusHealth,
                bonusSpreadMod,
                bonusDmg,
                bonusAS,
                bonusMS,
                bonusBlockChance;

    private double bonusCreditMod;

    /*Coordinates START*/
    private int playerX = 360,
                playerY = 500;
    /*Coordinates END*/

    //items
    Item[] inventory = new Item[4];
    int selectedSlot;
    /*PLAYER END*/

    /*BOSSES START*/
    private int waveCount = 0;

    //todo: REMINDER -- First boss should be JUNKO
    private Boss[] boss = {
            new Junko("Junko", 328, -200, 1000, 1, 3, 0),
            new Abomination("ATATATATATAT", 328, -200, 1500, 1, 4, 5),
            new Bootleg("Bootleg", -200, -200, 2500, 1, 4, 5)
    };

    private boolean waveClear = false;
    /*BOSSES END*/

    /*ENEMY START*/
    private ArrayList<Enemy> enemies = new ArrayList<>();

    private int onScreenCap = 3,
                enemyMaxCount = 10,
                onScreenCount = 0,
                enemyCount = 0;
    /*ENEMY END*/

    /*SHOPKEEPER START*/
    private int creditCount = 0,
                creditBonus = 0,
                selectedKeep = 0;

    private boolean shopPhase = false,
                    healingAvailable = false;

    private Shop shop = new Shop();
    /*SHOPKEEPER END*/

    //current phase
    private int phase = 0;

    //Bullet Objects
    private ArrayList<Bullet> plrBullet = new ArrayList<>();
    private ArrayList<Bullet> enemyBullet = new ArrayList<>();

    //Active effects
    private ActiveEffects fx = new ActiveEffects();
    private ArrayList<VFX> vfx = new ArrayList<>();

    //active bonuses
    private double boostedDamage = 0;
    private double boostedAS = 0;
    private double boostedSM = 0;
    private double boostedMS = 0;
    private double boostedBC = 0;

    private Random random = new Random();

    private HUD hud = new HUD();

    private ArrayList<Star> stars = new ArrayList<>(); //background stars

    //game over & ending alpha colour
    private int endAlpha = 0;

    //TODO: testing variables -- remove when done
    private int trigger = 0;

    void draw(Graphics2D g) {
        //Star drawing
        stars.forEach(stars -> stars.draw(g));

        //Player Drawing
        plrBullet.forEach(plrBullet -> plrBullet.drawShot(g));
        Main.plr.get(charNum).drawChar(Main.loadedImages, charID, playerHealth, playerX - 16, playerY - 16, g);
        if (focusToggle && playerHealth > 0) {
            g.setColor(Color.red);
            g.fillOval(playerX - 8, playerY - 8, 16, 16);
        }

        if (invuln > 0 && playerHealth > 0) {
            g.setColor(new Color(0, 240, 255, (int) (255 * invuln)));
            g.drawOval(playerX - circleDiameter / 2, playerY - circleDiameter / 2, circleDiameter, circleDiameter);
        }

        /*
         * 0) Enemy Drawing
         * 1) Boss Drawing
         * 2) Shop Drawing
         *
         * Should've had it like this to begin with
         */
        switch(phase) {
            case 0:
                enemyBullet.forEach(enemyBullet -> enemyBullet.drawShot(g));
                enemies.forEach(enemy -> enemy.draw(g, Main.loadedImages));
                break;

            case 1:
                boss[waveCount % boss.length].draw(g, Main.loadedImages);
                hud.drawBossComp(Main.loadedImages.get("bossHealthBack"), Main.loadedImages.get("bossHealthOverlay"), boss[waveCount % boss.length].getName(), g);
                break;

            case 2: shop.drawShopWindow(g, Main.loadedImages); break;
        }

        //vfx drawing
        vfx.forEach(vfx -> vfx.drawEffect(g));

        //HUD drawing
        hud.drawPlayerComp(Main.loadedImages.get("playerHealthBack"), Main.loadedImages.get("playerHealthOverlay"), Main.loadedImages.get("noMovement"), Main.loadedImages.get("block"), g);
        hud.drawItemSlots(selectedSlot, inventory, Main.loadedImages, g, fx.getCDCount(), fx.getLastMaxCD());

        if (playerHealth >= 0) {
            g.setColor(new Color(0 ,0, 0, endAlpha));
            g.fillRect(0, 0, 720, 720);
        }
    }

    void math() {
        //todo: item testing; c edition :)
        //comment and uncomment as needed
        /*if (trigger == 0) {
            trigger = 1;
            inventory[0] = Main.itemLoader.items.getActiveItems().get(4);
        }*/

        //stat calculation
        int maxHealth = playerMaxHealth + bonusHealth;
        int spreadMod = Math.max(baseSpreadMod + bonusSpreadMod + (int) boostedSM, 1);
        double creditMod = baseCreditMod + bonusCreditMod;
        int damage = Math.max(baseDmg + bonusDmg + (int) boostedDamage, 1);
        int attackSpeed = Math.max(Math.min(baseAS + bonusAS + (int) boostedAS, 30), 1);
        int movementSpeed = (focusToggle) ? Math.min(Math.max(baseMS + bonusMS, 1), 3) : Math.max(baseMS + bonusMS, 1);
        int blockChance = Math.min(baseBlockChance + bonusBlockChance + (int) boostedBC, 100);

        //for clarity: if boostedMS is negative and makes baseMS + bonusMS >= 0; make movementSpeed 0
        movementSpeed = (movementSpeed + boostedMS <= 0) ? 0 :
                        (focusToggle) ? Math.min(Math.max(movementSpeed + (int) boostedMS, 1), 3) : movementSpeed + (int) boostedMS;

        //PLAYER CONTROLS
        if (!shopPhase) {
            playerX -= (moveLeft && playerHealth > 0 && playerX > 8) ? movementSpeed : 0;
            playerX += (moveRight && playerHealth > 0 && playerX < 712) ? movementSpeed : 0;
            playerY += (moveDown && playerHealth > 0 && playerY < 684) ? movementSpeed : 0;
            playerY -= (moveUp && playerHealth > 0 && playerY > 8) ? movementSpeed : 0;

            //fail safe for when the player moves too fast!
            playerX = (playerX < 8) ? 8 : Math.min(playerX, 712);
            playerY = (playerY < 8) ? 8 : Math.min(playerY, 684);

            if (isShooting && attackDel <= 0) {
                int randNum = random.nextInt(4 * spreadMod) - 2 * spreadMod;
                plrBullet.add(new Bullet(playerX, playerY, 25, 270 + randNum, damage, Main.loadedImages.get(charID + "Bullet"), false));

                Main.ap.play(charID + "Shot");
                attackDel = baseAttackDel - attackSpeed;
            }

            if (toggleSpecial && playerHealth > 0) {
                try {
                    if (Main.itemLoader.items.getActiveItems().contains(inventory[selectedSlot])) {
                        ArrayList<Bullet> temp = fx.effectToggled(inventory[selectedSlot].getItemID(), Main.loadedImages, Main.ap, Main.sd, playerX, playerY);
                        double[] secondary = fx.secondaryEffectToggled(inventory[selectedSlot].getItemID(), Main.ap, playerX, playerY, invuln, boostedDamage, boostedAS, boostedSM, boostedMS, boostedBC);

                        invuln = secondary[0];
                        boostedDamage = secondary[1];
                        boostedAS = secondary[2];
                        boostedSM = secondary[3];
                        boostedMS = secondary[4];
                        boostedBC = secondary[5];
                        
                        temp.forEach(e -> {
                            if (!plrBullet.contains(e)) {
                                plrBullet.add(e);
                            }
                        });

                        if (fx.getHealthCost(inventory[selectedSlot].getItemID())) {
                            playerHealth--;
                            toggleSpecial = false;
                        }
                    }
                } catch (NullPointerException e) {
                    System.out.println("TODO: Place sfx here");
                }
            }
        }

        //Player special cooldown
        fx.cooldownCountdown();

        /*
         * 0) Enemy
         * 1) Boss
         * 2) Shop
         *
         * Should've had it like this to begin with
         */
        phase = (!waveClear && !shopPhase) ? 0 :
                (waveClear && !shopPhase) ? 1 : 2;
        switch(phase) {
            case 0: //#######################################################################################################################################
                /*GENERAL HIT DETECTION START*/
                for (int i = 0; i < enemyBullet.size(); i++) {
                    int xDist = (int) Math.abs((enemyBullet.get(i).returnX()) - (playerX)),
                        yDist = (int) Math.abs((enemyBullet.get(i).returnY()) - (playerY)),
                        dist = (int) (Math.pow(xDist, 2) + Math.pow(yDist, 2));
                    riskReward(dist, creditMod);

                    if (dist < 64 && playerHealth > 0 && invuln == 0) {
                        for (Enemy enemy : enemies) {
                            if (enemy.getShot().contains(enemyBullet.get(i)) && invuln == 0) {
                                int dmg = enemyBullet.get(i).returnDmg();
                                enemy.removeShot(enemyBullet.get(i));
                                int roll = random.nextInt(99) + 1;
                                playerHealth -= (roll > blockChance) ? dmg : 0;
                                invuln = 1;
                                vfx.add(new VFX(Main.loadedImages.get("dissipate"), (int) enemyBullet.get(i).returnX(), (int) enemyBullet.get(i).returnY(), 32));
                                Main.ap.play("hit");
                            }
                        }

                        enemyBullet.remove(i);
                        i--;
                    } else if (dist < Math.pow(circleDiameter / 2.0, 2) && playerHealth > 0 && invuln >= 0.1) {
                        for (Enemy enemy: enemies) {
                            if (enemy.getShot().contains(enemyBullet.get(i)))
                                enemy.removeShot(enemyBullet.get(i));
                        }

                        vfx.add(new VFX(Main.loadedImages.get("dissipate"), (int) enemyBullet.get(i).returnX(), (int) enemyBullet.get(i).returnY(), 32));
                        enemyBullet.remove(i);
                        i--;
                    }
                }

                //Enemy collision with bullets
                for (Enemy enemy : enemies) {
                    for (int i = 0; i < plrBullet.size(); i++) {
                        if (enemy.getHealth() > 0 && plrBullet.get(i).returnX() <= enemy.getXPos() + 32 && plrBullet.get(i).returnX() >= enemy.getXPos() - 32 && plrBullet.get(i).returnY() <= enemy.getYPos() + 32 && plrBullet.get(i).returnY() >= enemy.getYPos() - 32) {
                            if (plrBullet.get(i).isExplosive()) {
                                vfx.add(new VFX(Main.loadedImages.get("ppeldoSmallExplosion"), (int) plrBullet.get(i).returnX(), (int) plrBullet.get(i).returnY(), 64));
                                Main.ap.play("explosion");
                            } else {
                                vfx.add(new VFX(Main.loadedImages.get("dissipate"), (int) plrBullet.get(i).returnX(), (int) plrBullet.get(i).returnY(), 32));
                                Main.ap.play("playerHit");
                            }

                            enemy.dmgCalc(plrBullet.get(i).returnDmg());

                            plrBullet.remove(i);
                            i--;
                        }
                    }
                }
                /*GENERAL HIT DETECTION END*/

                //spawning enemies
                if (onScreenCount < onScreenCap && enemyCount < enemyMaxCount && !waveClear && !shopPhase) {
                    switch (waveCount % boss.length) {
                        case 0:
                            enemies.add(new Spirit(random.nextInt(592) + 32, -50, 30, 1, random.nextInt(2) + 1, 0));
                            break;

                        case 1:
                            enemies.add(new Gear(random.nextInt(592) + 32, -50, 35, 1, random.nextInt(4) + 2, 1));
                            break;

                        case 2:
                            enemies.add(new BootlegMinion(random.nextInt(592) + 32, -50, 40, 1, random.nextInt(2) + 1, 0));
                            break;
                    }

                    onScreenCount++;
                    enemyCount++;
                }

                //deleting enemy instances
                for (int i = 0; i < enemies.size(); i++) {
                    if (enemies.get(i).dead()) {
                        onScreenCount--;
                        creditCount += (random.nextInt(25) + 1) * creditMod;
                        enemies.remove(i);
                        i--;
                    }
                }

                //moving enemy down && enemy bullets
                for (Enemy enemy : enemies) {
                    //moving enemy down
                    if (enemy.getYPos() < enemy.finalDestY(200, 325, 100, 100) && enemy.getHealth() > 0) {
                        enemy.moveY(1);
                        enemy.setDoneMoving(enemy.getYPos() >= enemy.finalDestY(200, 325, 100, 100));
                    }

                    switch (waveCount % boss.length) {
                        case 0:
                            enemy.attack(playerX, playerY, Main.loadedImages.get("yellowRound"), Main.loadedImages.get("orangeRound"), Main.ap);
                            break;

                        case 1:
                            enemy.attack(playerX, playerY, Main.loadedImages.get("gearRound"), Main.loadedImages.get("greyRound"), Main.ap);
                            break;

                        case 2:
                            enemy.attack(playerX, playerY, Main.loadedImages.get("purpleRound"), Main.loadedImages.get("crimsonRound"), Main.ap);
                            break;
                    }

                    enemy.getShot().forEach(e -> {
                        if (!enemyBullet.contains(e)) {
                            enemyBullet.add(e);
                        }
                    });

                    enemy.getShot().removeIf(Bullet::offScreen);
                }

                if (enemyCount >= enemyMaxCount && onScreenCount == 0 && enemyBullet.size() <= 0) {
                    waveClear = true;
                    enemyCount = 0;
                    final int ENEMY_MAX_COUNT_LIMIT = 75,
                              ENEMY_MAX_CAP_LIMIT = 12;
                    enemyMaxCount += (enemyMaxCount < ENEMY_MAX_COUNT_LIMIT) ? 1 + random.nextInt(3) : 0;
                    onScreenCap += (onScreenCap < ENEMY_MAX_CAP_LIMIT) ? 1 : 0;
                } else if (!Main.ap.isPlaying(boss[waveCount % boss.length].getName() + "Stage")) {
                    Main.ap.loop(boss[waveCount % boss.length].getName() + "Stage");
                }

                enemyBullet.forEach(Bullet::move);
                enemyBullet.removeIf(Bullet::offScreen);
                break;

            case 1: //#######################################################################################################################################
                if (Main.ap.isPlaying(boss[waveCount % boss.length].getName() + "Stage"))
                    Main.ap.stop(boss[waveCount % boss.length].getName() + "Stage");

                if (!Main.ap.isPlaying(boss[waveCount % boss.length].getName() + "Theme"))
                    Main.ap.loop(boss[waveCount % boss.length].getName() + "Theme");

                //player hit detection
                for (int i = 0; i < boss[waveCount % boss.length].getShot().size(); i++) {
                    double bulletX = boss[waveCount % boss.length].getShot().get(i).returnX(),
                           bulletY = boss[waveCount % boss.length].getShot().get(i).returnY();
                    int xDist = (int) Math.abs((bulletX) - (playerX)),
                        yDist = (int) Math.abs((bulletY) - (playerY)),
                        dist = (int) (Math.pow(xDist, 2) + Math.pow(yDist, 2));
                    riskReward(dist, creditMod);

                    if (dist < 64 && playerHealth > 0 && invuln == 0) {
                        int dmg = boss[waveCount % boss.length].getShot().get(i).returnDmg();
                        vfx.add(new VFX(Main.loadedImages.get("dissipate"), (int) bulletX, (int) bulletY, 32));
                        boss[waveCount % boss.length].removeShot(i);
                        int roll = random.nextInt(99) + 1;
                        playerHealth -= (roll > blockChance) ? dmg : 0;
                        invuln = 1;

                        Main.ap.play("hit");
                        i--;
                    } else if (dist < Math.pow(circleDiameter / 2.0, 2) && playerHealth > 0 && invuln >= 0.1) {
                        vfx.add(new VFX(Main.loadedImages.get("dissipate"), (int) bulletX, (int) bulletY, 32));
                        boss[waveCount % boss.length].removeShot(i);
                        i--;
                    }
                }

                //boss hit detection
                for (int i = 0; i < plrBullet.size(); i++) {
                    if (boss[waveCount % boss.length].getHealth() > 0 && plrBullet.get(i).returnX() <= boss[waveCount % boss.length].getXPos() + 80 && plrBullet.get(i).returnX() >= boss[waveCount % boss.length].getXPos() - 16 && plrBullet.get(i).returnY() <= boss[waveCount % boss.length].getYPos() + 80 && plrBullet.get(i).returnY() >= boss[waveCount % boss.length].getYPos() - 16) {
                        if (plrBullet.get(i).isExplosive()) {
                            vfx.add(new VFX(Main.loadedImages.get("ppeldoSmallExplosion"), (int) plrBullet.get(i).returnX(), (int) plrBullet.get(i).returnY(), 64));
                            Main.ap.play("explosion");
                        } else {
                            vfx.add(new VFX(Main.loadedImages.get("dissipate"), (int) plrBullet.get(i).returnX(), (int) plrBullet.get(i).returnY(), 32));
                            Main.ap.play("playerHit");
                        }

                        boss[waveCount % boss.length].dmgCalc(plrBullet.get(i).returnDmg());

                        plrBullet.remove(i);
                        i--;
                    }
                }

                //executing boss math
                boss[waveCount % boss.length].math(Main.loadedImages, this.playerX, this.playerY, Main.ap);
                shopPhase = boss[waveCount % boss.length].isBossDefeated();
                selectedKeep = (shopPhase) ? random.nextInt(Main.loadedShopkeepers.get(waveCount % boss.length).size()) : selectedKeep;
                creditCount += (shopPhase) ? (random.nextInt(60 + (int) Math.pow(waveCount, 3)) + 50) * creditMod : 0;
                healingAvailable = (shopPhase) ? (random.nextInt(100) > 80) : healingAvailable;
                break;

            case 2: //#######################################################################################################################################
                //work around for a minor bug; boss theme restarts upon shop opening
                //no idea why it happens
                if (Main.ap.isPlaying(boss[waveCount % boss.length].getName() + "Theme"))
                    Main.ap.stop(boss[waveCount % boss.length].getName() + "Theme");

                creditCount = shop.shopWindowMath(waveCount % boss.length, Main.loadedShopkeepers.get(waveCount % boss.length).get(selectedKeep), creditCount, moveUp, moveDown, moveLeft, moveRight, isShooting, healingAvailable, selectedSlot, playerHealth, Main.itemLoader.items, Main.ap);
                inventory = shop.updateInventory();
                playerHealth = (shop.healthUpdate() >= maxHealth) ? maxHealth :
                               (shop.healthUpdate() > 0)          ? shop.healthUpdate() : playerHealth;

                bonusHealth = shop.getBonusHealth();
                bonusSpreadMod = shop.getBonusSpreadMod();
                bonusDmg = shop.getBonusDmg();
                bonusAS = shop.getBonusAS();
                bonusMS = shop.getBonusMS();
                bonusBlockChance = shop.getBonusBlockChance();
                bonusCreditMod = shop.getBonusCreditMod();

                if (toggleSpecial && shop.shopPhaseStandby()) {
                    shopPhase = false;
                    waveClear = false;
                    shop.resetState(Main.ap, waveCount % boss.length);
                    fx.forceCoolDown();
                    waveCount++;
                }
                break;
        }

        //credit cap
        creditCount = Math.min(creditCount, 999999);

        //Player bullet attack speed
        attackDel -= (attackDel > 0) ? 1:0;

        //invulnerable state
        if (invuln > 0) {
            invuln -= 0.02;
            circleDiameter += (circleDiameter / 4) + 1;
        } else {
            invuln = 0;
            circleDiameter = 0;
        }

        //every frame, decay the active boost
        boostedDamage -= (boostedDamage > 0) ? 0.1 : (boostedDamage < 0) ? -0.1 : 0;
        boostedAS -= (boostedAS > 0) ? 0.1 : (boostedAS < 0) ? -0.1 : 0;
        boostedSM -= (boostedSM > 0) ? 0.1 : (boostedSM < 0) ? -0.1 : 0;
        boostedMS -= (boostedMS > 0) ? 0.05 : (boostedMS < 0) ? -0.05 : 0;
        boostedBC -= (boostedBC > 0) ? 0.1 : (boostedBC < 0) ? -0.1 : 0;

        //hud updates
        hud.healthUpdate(maxHealth, playerHealth, boss[waveCount % boss.length].getDefHealth(), boss[waveCount % boss.length].getHealth());
        hud.statusUpdate((movementSpeed + boostedMS <= 0), blockChance);
        hud.updateCredits(creditCount);
        hud.updatePhase(shopPhase);

        /*BULLETS*/
        plrBullet.forEach(Bullet::move);
        plrBullet.removeIf(Bullet::offScreen);

        /*ANIMATIONS START*/
        //bullet animations
        plrBullet.forEach(Bullet::bulletSpriteAnimation);

        //char animation
        Main.plr.get(charNum).animateChar(Main.ap, playerHealth);

        //enemy animation
        enemies.forEach(enemy -> enemy.anim(Main.ap));

        //effects animation
        vfx.forEach(VFX::animatedEffect);
        vfx.removeIf(VFX::fxComplete);

        //Background stars
        stars.add(new Star(
                random.nextInt(720),
                random.nextInt(720),
                Main.loadedImages.get("star")
        ));
        stars.forEach(Star::animate);
        stars.removeIf(Star::starDone);

        endAlpha += (playerHealth <= 0) ? 1 : 0;
        /*ANIMATIONS END*/

        if (endAlpha >= 255 && playerHealth <= 0) {
            Main.ap.stop(boss[waveCount % boss.length].getName() + ((phase == 0) ? "Stage" : "Theme"));
            Main.gameState = 0;
            endAlpha = 0;
        }
    }

    private void riskReward(int dist, double creditMod) {
        creditBonus += (dist < 4096 && playerHealth > 0 && invuln == 0) ? 1 : 0;
        if (creditBonus >= 25) {
            creditCount += 1 * creditMod;
            creditBonus = 0;
        }
    }
}
