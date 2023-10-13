package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.LoseMojoPower;
import bogwarden.powers.Mojo;
import bogwarden.vfx.MojoFlashEffect;

public class DarkPact extends AbstractBogCard {
    public final static String ID = makeID("DarkPact");

    public DarkPact() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setMagic(2, +2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new MojoFlashEffect(p.hb.cX, p.hb.cY));
        applyToSelf(new Mojo(p, magicNumber));
        applyToSelf(new LoseMojoPower(p, magicNumber));
    }
}