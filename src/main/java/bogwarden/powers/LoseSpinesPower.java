package bogwarden.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class LoseSpinesPower extends AbstractBogPower {
    public static final String POWER_ID = makeID("LoseSpinesPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LoseSpinesPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        loadRegion("flex");
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }

    public void atEndOfTurn(boolean isPlayer) {
        flash();
        applyToSelf(new Spines(owner, -amount));
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }
}