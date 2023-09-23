package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Incantation extends AbstractBogCard {
    public final static String ID = makeID("Incantation");

    public Incantation() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        setMagic(2);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            makeInHand(cardsToPreview);
    }

    @Override
    public void upp() {
        super.upp();
        cardsToPreview.upgrade();
    }
}