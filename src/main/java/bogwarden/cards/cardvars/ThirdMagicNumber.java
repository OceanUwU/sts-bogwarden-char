package bogwarden.cards.cardvars;

import static bogwarden.BogMod.makeID;

import basemod.abstracts.DynamicVariable;
import bogwarden.cards.AbstractBogCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class ThirdMagicNumber extends DynamicVariable {

    @Override
    public String key() {
        return makeID("m3");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).isThirdMagicModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).thirdMagic;
        }
        return -1;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractBogCard) {
            ((AbstractBogCard) card).isThirdMagicModified = v;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).baseThirdMagic;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).upgradedThirdMagic;
        }
        return false;
    }
}