package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Forge extends AbstractBogCard {
    public final static String ID = makeID("Forge");

    public Forge() {
        super(ID, -2, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new ForgePower(p, magicNumber));
    }

    public static class ForgePower extends AbstractBogPower {
        public static String POWER_ID = makeID("ForgePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public ForgePower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = amount == 1 ? powerStrings.DESCRIPTIONS[0] : (powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2]);
        }

        public void atStartOfTurnPostDraw() {
            flash();
            atb(new ArmamentsAction(true));
            atb(new ReducePowerAction(owner, owner, this, 1));
        }
    }
}