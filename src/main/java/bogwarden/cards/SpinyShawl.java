package bogwarden.cards;

import bogwarden.powers.Spines;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpinyShawl extends AbstractBogCard {
    public final static String ID = makeID("SpinyShawl");

    public SpinyShawl() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(7, +2);
        setSecondMagic(0, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new Spines(p, magicNumber));
        if (secondMagic > 0)
            applyToSelf(new DexterityPower(p, secondMagic));
    }
}