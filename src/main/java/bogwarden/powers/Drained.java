package bogwarden.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static bogwarden.BogMod.makeID;

public class Drained extends AbstractBogPower {
    public static final String POWER_ID = makeID("Drained");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final float MULT = 0.75f;

    public Drained(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, true, owner, amount);
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + (int)(100 - MULT * 100) + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 2 : 3];
    }
  
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL)
            return damage * MULT;
        return damage;
    }
}