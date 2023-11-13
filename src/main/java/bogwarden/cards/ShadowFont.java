package bogwarden.cards;


import bogwarden.vfx.IncantationEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class ShadowFont extends AbstractBogCard {
    public final static String ID = makeID("ShadowFont");

    public ShadowFont() {
        super(ID, 1, CardType.SKILL, CardRarity.BASIC, CardTarget.SELF);
        setBlock(4, +2);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        vfx(new IncantationEffect());
        makeInHand(cardsToPreview);
    }

    @Override
    public void upp() {
        super.upp();
        cardsToPreview.upgrade();
    }
}