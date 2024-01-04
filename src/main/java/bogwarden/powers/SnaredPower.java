package bogwarden.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.cards.BarbedLasso.BarbedLassoPower;

public class SnaredPower extends AbstractBogPower implements NonStackablePower {
    public static String POWER_ID = makeID("SnaredPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SnaredPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        amount2 = 1;
    }

    public boolean isStackable(AbstractPower power) {
        return power instanceof SnaredPower && ((SnaredPower)power).amount2 == amount2;
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        if (type == DamageInfo.DamageType.NORMAL)
            return damage - amount; 
        return damage;
    }

    public void atStartOfTurn() {
        if (owner instanceof AbstractMonster)
            if (((AbstractMonster)owner).getIntentBaseDmg() > 0)
                amount2--;
    }

    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer || amount2 <= 0) {
            atb(new RemoveSpecificPowerAction(owner, owner, this));
            if (owner.hasPower(BarbedLassoPower.POWER_ID)) {
                atb(new ApplyPowerAction(owner, adp(), new SnaredPower(owner, pwrAmt(owner, BarbedLassoPower.POWER_ID))));
                atb(new RemoveSpecificPowerAction(owner, owner, BarbedLassoPower.POWER_ID));
            }
        }
    }
}