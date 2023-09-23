package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class DeathWard extends AbstractBogCard {
    public final static String ID = makeID("DeathWard");
    private final static int DAMAGE = 3;

    public DeathWard() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.ENEMY);
        setMagic(3, +1);
        setSecondMagic(DAMAGE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DeathWardPower(p, magicNumber));
    }

    public static class DeathWardPower extends AbstractBogPower {
        public static String POWER_ID = makeID("DeathWardPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public DeathWardPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + DAMAGE + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
        }
  
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            flash();
            for (int i = 0; i < amount; i++)
                atb(new DamageRandomEnemyAction(new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        }
    }
}