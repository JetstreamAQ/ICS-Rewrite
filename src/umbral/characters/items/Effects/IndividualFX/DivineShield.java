package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.items.Effects.Effect;

public class DivineShield extends Effect{
    public DivineShield() {
        cdVariant = 2;
        damage = 0;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("shieldActivate");
        return new double[]{1, damage, 15, spreadMod, movementSpeed, 100};
    }
}
