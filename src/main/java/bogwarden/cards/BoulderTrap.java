package bogwarden.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BoulderTrap extends AbstractTrapCard {
    public final static String ID = makeID("BoulderTrap");

    public BoulderTrap() {
        super(ID, CardRarity.RARE);
        setMagic(1);
        setExhaust(true);
        cardsToPreview = new Boulder();
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        att(new MakeTempCardInDrawPileAction(cardsToPreview, 1, true, true));
    }

    @Override
    public void upp() {
        super.upp();
        cardsToPreview.upgrade();
    }
}