package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.LoseMojoPower;
import bogwarden.powers.Maledict;
import bogwarden.powers.Mojo;

public class HexingTotem extends AbstractTrapCard {
    public final static String ID = makeID("HexingTotem");

    public HexingTotem() {
        super(ID, CardRarity.UNCOMMON);
        setMagic(2, +1);
        setSecondMagic(2, +2);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelfTop(new LoseMojoPower(p, secondMagic));
        applyToSelfTop(new Mojo(p, secondMagic));
        applyToEnemyTop(m, new Maledict(m, magicNumber));
    }
}