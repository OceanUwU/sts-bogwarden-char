package bogwarden.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ArtifactPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class LoseMojoPower extends AbstractBogPower {
    public static final String POWER_ID = makeID("LoseMojoPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public LoseMojoPower(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        loadRegion("flex");
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }

    public void atEndOfRound() {
        flash();
        int toLose = amount;
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                if (owner.hasPower(ArtifactPower.POWER_ID))
                    applyToSelfTop(new LoseMojoPower(owner, toLose));
                else
                    att(new ReducePowerAction(owner, owner, Mojo.POWER_ID, toLose));
            }
        });
        atb(new RemoveSpecificPowerAction(owner, owner, this));
    }
}