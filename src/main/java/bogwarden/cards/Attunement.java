package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.Mojo;

public class Attunement extends AbstractBogCard {
    public final static String ID = makeID("Attunement");

    public Attunement() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new Mojo(p, magicNumber));
    }
}