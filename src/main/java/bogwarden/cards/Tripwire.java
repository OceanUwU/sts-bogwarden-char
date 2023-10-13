package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Tripwire extends AbstractTrapCard {
    public final static String ID = makeID("Tripwire");

    public Tripwire() {
        super(ID, CardRarity.COMMON);
        setMagic(1, +1);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelfTop(new NextTurnAgile(p, 1));
        applyToSelfTop(new DrawCardNextTurnPower(p, magicNumber));
    }

    public static class NextTurnAgile extends AbstractBogPower {
        public static String POWER_ID = makeID("NextTurnAgile");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public NextTurnAgile(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            priority = 4;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            flash();
            applyToSelf(new Agile(owner, amount));
            atb(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    public static class Agile extends AbstractBogPower {
        public static final String POWER_ID = makeID("Agile");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public Agile(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, true, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2];
        }
    
        public void atEndOfRound() {
            addToBot(new ReducePowerAction(owner, owner, this, 1));
        }
  
        public float modifyBlock(float blockAmount) {
            return blockAmount * 1.5F;
        }
    }
}