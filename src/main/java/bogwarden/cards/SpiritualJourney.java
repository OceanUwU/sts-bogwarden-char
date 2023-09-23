package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpiritualJourney extends AbstractBogCard {
    public final static String ID = makeID("SpiritualJourney");

    public SpiritualJourney() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(4, +1);
        setInnate(true);
        setEthereal(true, false);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new SpiritualJourneyPower(p, magicNumber));
    }

    public static class SpiritualJourneyPower extends AbstractBogPower {
        public static String POWER_ID = makeID("SpiritualJourneyPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public SpiritualJourneyPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                flash();
                att(new ReducePowerAction(owner, owner, this, 1));
            }
        }

        public void onVictory() {
            AbstractDungeon.player.increaseMaxHp(amount, true);
        }
    }
}