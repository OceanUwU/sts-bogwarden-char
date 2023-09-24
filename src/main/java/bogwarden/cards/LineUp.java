package bogwarden.cards;

import bogwarden.actions.TriggerTrapAction;
import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.EnergizedBogPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class LineUp extends AbstractBogCard {
    public final static String ID = makeID("LineUp");

    public LineUp() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new EnergizedBogPower(p, magicNumber));
        applyToSelf(new NextTurnTriggerTwice(p, 1));
    }

    public static class NextTurnTriggerTwice extends AbstractBogPower {
        public static String POWER_ID = makeID("NextTurnTriggerTwice");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public NextTurnTriggerTwice(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2];
        }
  
        public void atStartOfTurnPostDraw() {
            flash();
            for (int i = 0; i < amount; i++)
                atb(new TriggerTrapAction(2));
            atb(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}