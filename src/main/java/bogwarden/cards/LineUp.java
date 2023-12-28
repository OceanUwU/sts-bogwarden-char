package bogwarden.cards;

import bogwarden.powers.EnergizedBogPower;
import bogwarden.vfx.OpenEyesEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class LineUp extends AbstractBogCard {
    public final static String ID = makeID("LineUp");

    public LineUp() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(4, +1);
        setSecondMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        OpenEyesEffect eyes = new OpenEyesEffect(new Color(0.73f, 0.94f, 0.36f, 1f), false, false, false, 1f);
        vfx(eyes);
        atb(new ScryAction(magicNumber));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                eyes.canGoPastHalf = true;
            }
        });
        applyToSelf(new EnergizedBogPower(p, secondMagic));
    }

    /*public static class NextTurnTriggerTwice extends AbstractBogPower {
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
    }*/
}