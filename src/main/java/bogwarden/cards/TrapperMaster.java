package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TrapperMaster extends AbstractBogCard {
    public final static String ID = makeID("TrapperMaster");

    public TrapperMaster() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(1);
        setInnate(false, true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new TrapperMasterPower(p, magicNumber));
    }

    public static class TrapperMasterPower extends AbstractBogPower {
        public static String POWER_ID = makeID("TrapperMasterPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        private boolean triggeredYet = false;
    
        public TrapperMasterPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2] + amount + powerStrings.DESCRIPTIONS[3] + powerStrings.DESCRIPTIONS[triggeredYet ? 4 : 5];
        }

        public void atStartOfTurn() {
            triggeredYet = false;
            updateDescription();
        }
  
        public void onTriggerTrap(AbstractTrapCard c) {
            if (!triggeredYet) {
                triggeredYet = true;
                updateDescription();
                flash();
                atb(new GainEnergyAction(amount));
                atb(new DrawCardAction(amount));
            }
        }
    }
}