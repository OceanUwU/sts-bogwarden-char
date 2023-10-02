package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BarbedLasso extends AbstractBogCard {
    public final static String ID = makeID("BarbedLasso");

    public BarbedLasso() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(5, +1);
        setMagic(3, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        applyToEnemy(m, new BarbedLassoPower(m, magicNumber));
    }

    public static class BarbedLassoPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BarbedLassoPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BarbedLassoPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void onUseCard(AbstractCard card, UseCardAction action) {
            if (card.type == AbstractCard.CardType.SKILL) {
                flash();
                addToTop(new DamageAction(owner, new DamageInfo(adp(), amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
            } 
        }
  
        public void atStartOfTurn() {
            atb(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}