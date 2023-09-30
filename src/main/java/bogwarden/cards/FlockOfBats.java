package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class FlockOfBats extends AbstractBogCard {
    public final static String ID = makeID("FlockOfBats");

    public FlockOfBats() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(6, +3);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new FlockOfBatsPower(p, magicNumber));
    }

    public static class FlockOfBatsPower extends AbstractBogPower {
        public static final String POWER_ID = makeID("FlockOfBatsPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public FlockOfBatsPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
    
        public void atEndOfRound() {
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }

        public int onAttacked(DamageInfo info, int damageAmount) {
            flash();
            addToTop(new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(amount, true), DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            return damageAmount;
        }
    }
}