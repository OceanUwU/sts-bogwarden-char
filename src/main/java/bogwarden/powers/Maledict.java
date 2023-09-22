package bogwarden.powers;

import bogwarden.relics.PaperCrokh;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static bogwarden.BogMod.makeID;

public class Maledict extends AbstractBogPower {
    public static final String POWER_ID = makeID("Maledict");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final float MULT = 1.5f;
    public static final float CROKH_MULT = 1.75f;

    public Maledict(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, true, owner, amount);
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + (int)(mult() * 100 - 100) + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 2 : 3];
    }
  
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    public float mult() {
        return increasedByCrokh() ? CROKH_MULT : MULT;
    }

    private boolean increasedByCrokh() {
        return owner != null && !owner.isPlayer && AbstractDungeon.player.hasRelic(PaperCrokh.ID);
    }

    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL)
            return damage * mult();
        return damage;
    }
}