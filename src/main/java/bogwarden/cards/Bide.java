package bogwarden.cards;

import bogwarden.actions.EasyXCostAction;
import bogwarden.powers.AbstractBogPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Bide extends AbstractBogCard {
    public final static String ID = makeID("Bide");

    public Bide() {
        super(ID, -1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(9, +2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            if (effect > 0)
                applyToSelfTop(new BidePower(p, params[0], effect));
            return true;
        }, block));
    }

    public static class BidePower extends AbstractBogPower implements NonStackablePower {
        public static String POWER_ID = makeID("BidePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BidePower(AbstractCreature owner, int amount, int turns) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            isTwoAmount = true;
            amount2 = turns;
            updateDescription();
        }

        public boolean isStackable(AbstractPower power) {
            return power instanceof BidePower && ((BidePower)power).amount2 == amount2;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount2 + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2] + amount + powerStrings.DESCRIPTIONS[3];
        }
  
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            flash();
            atb(new GainBlockAction(owner, owner, amount));
            if (--amount2 <= 0)
                atb(new RemoveSpecificPowerAction(owner, owner, this));
            else {
                stackPower(0);
                updateDescription();
            }
        }
    }
}