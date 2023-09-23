package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BlendingIn extends AbstractBogCard {
    public final static String ID = makeID("BlendingIn");

    public BlendingIn() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(1);
        setUpgradedCost(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BlendingInPower(p, secondMagic));
    }

    public static class BlendingInPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BlendingInPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        private static boolean trigger = false;
    
        public BlendingInPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atEndOfTurn(boolean isPlayer) {
            forAllMonstersLiving(mo -> {if (mo.getIntentBaseDmg() <= 0) trigger = true;});
            if (trigger) {
                flash();
                applyToSelf(new BlurPower(owner, amount));
                trigger = false;
            }
        }
    }
}