package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BadMedicine extends AbstractBogCard {
    public final static String ID = makeID("BadMedicine");

    public BadMedicine() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(4, +2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BadMedicinePower(p, 2));
        atb(new HealAction(p, p, magicNumber));
    }

    public static class BadMedicinePower extends AbstractBogPower {
        public static String POWER_ID = makeID("BadMedicinePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BadMedicinePower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public int onHeal(int healAmount) {
            if (healAmount > 0f) {
                flash();
                addToBot(new DamageRandomEnemyAction(new DamageInfo(owner, healAmount * amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.POISON));
            }
           return healAmount;
        }
    }
}