package bogwarden.cards.cardvars;

import static bogwarden.BogMod.makeID;

import basemod.abstracts.DynamicVariable;
import bogwarden.cards.AbstractBogCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("m2");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).isSecondMagicModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).secondMagic;
        }
        return -1;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractBogCard) {
            ((AbstractBogCard) card).isSecondMagicModified = v;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).baseSecondMagic;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).upgradedSecondMagic;
        }
        return false;
    }
}