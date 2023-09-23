package bogwarden.cards;


import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class PoisonNova extends AbstractBogCard {
    public final static String ID = makeID("PoisonNova");

    public PoisonNova() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(1, +1);
        cardsToPreview = new Blast();
        ((Blast)cardsToPreview).baseSecondMagic = magicNumber;
        ((Blast)cardsToPreview).secondMagic = magicNumber;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new PoisonNovaPower(p, magicNumber));
    }

    public void upp() {
        super.upp();
        ((Blast)cardsToPreview).baseSecondMagic = magicNumber;
        ((Blast)cardsToPreview).secondMagic = magicNumber;
    }

    public static class PoisonNovaPower extends AbstractBogPower {
        public static String POWER_ID = makeID("PoisonNovaPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public PoisonNovaPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
    }
}