package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class IllIntendedSchemes extends AbstractBogCard {
    public final static String ID = makeID("IllIntendedSchemes");

    public IllIntendedSchemes() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new IllIntendedSchemesPower(p, magicNumber));
    }

    public static class IllIntendedSchemesPower extends AbstractBogPower {
        public static String POWER_ID = makeID("IllIntendedSchemesPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public IllIntendedSchemesPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void onCardDraw(AbstractCard card) {
            if (card instanceof AbstractTrapCard) {
                flash();
                att(new GainBlockAction(owner, owner, amount, true));
            }
        }
  
        public void onTriggerTrap(AbstractTrapCard c) {
            flash();
            att(new GainBlockAction(owner, owner, amount, true));
        }
    }
}