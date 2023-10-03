package bogwarden.cards;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class DarkRitual extends AbstractBogCard {
    public final static String ID = makeID("DarkRitual");

    public DarkRitual() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(3);    
        setSecondMagic(3);
        setUpgradedCost(0);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new LoseHPAction(p, p, secondMagic));
        applyToSelf(new RegenPower(p, magicNumber));
    }

    /*public static class TakeDamagePower extends AbstractBogPower {
        public static String POWER_ID = makeID("TakeDamagePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public TakeDamagePower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurnPostDraw() {
            flash();
            atb(new DamageAction(owner, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }*/
}