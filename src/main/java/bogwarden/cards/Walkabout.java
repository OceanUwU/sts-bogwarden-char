package bogwarden.cards;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Walkabout extends AbstractBogCard {
    public final static String ID = makeID("Walkabout");

    public Walkabout() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        setMagic(5);
        setSecondMagic(5, -1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(magicNumber));
        atb(new DiscardAction(p, p, secondMagic, false));
    }
}