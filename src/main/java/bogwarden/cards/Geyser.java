package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.vfx.IncantationEffect;

public class Geyser extends AbstractBogCard {
    public final static String ID = makeID("Geyser");

    public Geyser() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(4, +2);
        setMagic(1);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        vfx(new IncantationEffect());
        makeInHand(cardsToPreview, magicNumber);
    }

    @Override
    public void upp() {
        cardsToPreview.upgrade();
        super.upp();
    }
}