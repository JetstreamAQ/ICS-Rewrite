package umbral.characters.items;

import umbral.audio.AudioPlayer;
import umbral.characters.base.Bullet;
import umbral.characters.base.SpriteDraw;
import umbral.characters.items.Effects.Effect;
import umbral.characters.items.Effects.IndividualFX.Album;
import umbral.characters.items.Effects.IndividualFX.Autoloader;
import umbral.characters.items.Effects.IndividualFX.Blunderbuss;
import umbral.characters.items.Effects.IndividualFX.DivineShield;
import umbral.characters.items.Effects.IndividualFX.DivineSpear;
import umbral.characters.items.Effects.IndividualFX.DivineSword;
import umbral.characters.items.Effects.IndividualFX.Eviction;
import umbral.characters.items.Effects.IndividualFX.HSG;
import umbral.characters.items.Effects.IndividualFX.LWSG;
import umbral.characters.items.Effects.IndividualFX.MSG;
import umbral.characters.items.Effects.IndividualFX.MissileManaFactory;
import umbral.characters.items.Effects.IndividualFX.Ouroboros;
import umbral.characters.items.Effects.IndividualFX.RogueCore;
import umbral.characters.items.Effects.IndividualFX.ScuffedSpeakers;
import umbral.characters.items.Effects.IndividualFX.ShipManual;
import umbral.characters.items.Effects.IndividualFX.TachyonDrive;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Map;

public class ActiveEffects {
    private int[] healthCostID = new int[]{6, 15};
    private ArrayList<Effect> effects = new ArrayList<>();

    private int globalCooldown = 0,
                lastMaxCD = 0;

    public ActiveEffects() {
        effects.add(new MissileManaFactory()); // 0
        effects.add(new DivineShield()); // 1
        effects.add(new DivineSpear()); // 2
        effects.add(new DivineSword()); // 3
        effects.add(new Ouroboros()); // 4
        effects.add(new LWSG()); // 5
        effects.add(new ShipManual()); // 6
        effects.add(new Album()); // 7
        effects.add(new TachyonDrive()); // 8
        effects.add(new RogueCore()); // 9
        effects.add(new Autoloader()); //10
        effects.add(new MSG()); //11
        effects.add(new HSG()); //12
        effects.add(new ScuffedSpeakers()); //13
        effects.add(new Blunderbuss()); //14
        effects.add(new Eviction()); //15
    }

    public ArrayList<Bullet> effectToggled(int id, Map<String, BufferedImage> loadedImages, AudioPlayer ap, SpriteDraw sd, int x, int y) {
        ArrayList<Bullet> shot = new ArrayList<>();

        if (globalCooldown == 0) {
            shot.addAll(effects.get(id).effect(loadedImages, ap, sd, x, y));
        }

        return shot;
    }

    public double[] secondaryEffectToggled(int id, AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        if (globalCooldown == 0) {
            setCoolDown(effects.get(id).getCdVariant());
            return effects.get(id).secondaryEffect(ap, x, y, invuln, damage, attackSpeed, spreadMod, movementSpeed, blockChance);
        }

        return new double[]{invuln, damage, attackSpeed, spreadMod, movementSpeed, blockChance};
    }

    public void cooldownCountdown() {globalCooldown -= (globalCooldown > 0) ? 1 : 0;}
    public int getCDCount() {return globalCooldown;}
    public int getLastMaxCD() {return lastMaxCD;}

    public boolean getHealthCost(int id) {
        for (int activeID : healthCostID) {
            if (activeID == id)
                return true;
        }

        return false;
    }

    private void setCoolDown(int type) {
        final int MICRO_CD = 10,
                  TINY_CD = 90,
                  SHORT_CD = 270,
                  MEDIUM_CD = 420,
                  LONG_CD = 800,
                  EXTREME_CD = 1500,
                  MASSIVE_CD = 3000,
                  GOOGOL_CD = 6000;

        switch (type) {
            case 0: globalCooldown = MICRO_CD; lastMaxCD = MICRO_CD; break;
            case 1: globalCooldown = TINY_CD; lastMaxCD = TINY_CD; break;
            case 2: globalCooldown = SHORT_CD; lastMaxCD = SHORT_CD; break;
            case 3: globalCooldown = MEDIUM_CD; lastMaxCD = MEDIUM_CD; break;
            case 4: globalCooldown = LONG_CD; lastMaxCD = LONG_CD; break;
            case 5: globalCooldown = EXTREME_CD; lastMaxCD = EXTREME_CD; break;
            case 6: globalCooldown = MASSIVE_CD; lastMaxCD = MASSIVE_CD; break;
            case 7: globalCooldown = GOOGOL_CD; lastMaxCD = GOOGOL_CD; break;
        }
    }

    public void forceCoolDown() {
        globalCooldown = 25;
        lastMaxCD = 25;
    }
}
