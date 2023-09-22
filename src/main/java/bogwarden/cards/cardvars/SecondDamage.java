package bogwarden.cards.cardvars;

import static bogwarden.BogMod.makeID;

import basemod.abstracts.DynamicVariable;
import bogwarden.cards.AbstractBogCard;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class SecondDamage extends DynamicVariable {

    @Override
    public String key() {
        return makeID("sd");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).isSecondDamageModified;
        }
        return false;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractBogCard) {
            ((AbstractBogCard) card).isSecondDamageModified = v;
        }
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).secondDamage;
        }
        return -1;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).baseSecondDamage;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractBogCard) {
            return ((AbstractBogCard) card).upgradedSecondDamage;
        }
        return false;
    }
}