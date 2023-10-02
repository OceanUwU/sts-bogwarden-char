package bogwarden.powers;

import bogwarden.patches.NonAttackDamagePatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Venom extends AbstractBogPower {
    public static final String POWER_ID = makeID("Venom");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int HP_LOSS = 2;

    public Venom(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, true, owner, amount);
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2] + HP_LOSS + powerStrings.DESCRIPTIONS[3];
    }
  
    public void atEndOfRound() {
        addToBot(new ReducePowerAction(owner, owner, this, 1));
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        if (!NonAttackDamagePatches.DamageInfoFields.fromVenom.get(info)) {
            flash();
            DamageInfo venomInfo = new DamageInfo(info.owner == null ? adp() : info.owner, HP_LOSS, DamageType.HP_LOSS);
            NonAttackDamagePatches.DamageInfoFields.fromVenom.set(venomInfo, true);
            addToTop(new DamageAction(owner, venomInfo, AbstractGameAction.AttackEffect.POISON));
        }
        return damageAmount;
    }
}