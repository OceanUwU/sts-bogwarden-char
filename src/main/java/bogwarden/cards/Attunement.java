package bogwarden.cards;

import bogwarden.powers.Mojo;
import bogwarden.vfx.MojoFlashEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Attunement extends AbstractBogCard {
    public final static String ID = makeID("Attunement");

    public Attunement() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new MojoFlashEffect(p.hb.cX, p.hb.cY));
        applyToSelf(new Mojo(p, magicNumber));
    }
}