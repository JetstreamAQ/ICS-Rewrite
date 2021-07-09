package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.items.Effects.Effect;

public class LWSG extends Effect {
    public LWSG() {
        cdVariant = 5;
        damage = 0;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("shieldActivate");
        return new double[]{0.5, damage, attackSpeed, spreadMod, movementSpeed, 10};
    }
}
