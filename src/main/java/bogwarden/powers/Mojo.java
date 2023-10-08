package bogwarden.powers;

import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static bogwarden.BogMod.makeID;

public class Mojo extends AbstractBogPower {
    public static final String POWER_ID = makeID("Mojo");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public Mojo(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
    }
  
    @Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play(BogAudio.MOJO, 0.05F);
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL)
            return damage + amount;
        return damage;
    }
}