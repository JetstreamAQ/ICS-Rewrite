package umbral.characters.items.Effects.IndividualFX;

import umbral.audio.AudioPlayer;
import umbral.characters.items.Effects.Effect;

public class RogueCore extends Effect {
    public RogueCore() {
        cdVariant = 4;
        damage = 0;
    }

    @Override
    public double[] secondaryEffect(AudioPlayer ap, int x, int y, double invuln, double damage, double attackSpeed, double spreadMod, double movementSpeed, double blockChance) {
        ap.play("calculating");
        return new double[]{invuln, damage, 5, -50, movementSpeed, 25};
    }
}
