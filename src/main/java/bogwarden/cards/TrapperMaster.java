package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TrapperMaster extends AbstractBogCard {
    public final static String ID = makeID("TrapperMaster");

    public TrapperMaster() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        setMagic(2, +1);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(magicNumber));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                att(new GainEnergyAction((int)p.hand.group.stream().filter(c -> c instanceof AbstractTrapCard).count()));
            }
        });
    }

    /*public static class TrapperMasterPower extends AbstractBogPower {
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
    }*/
}