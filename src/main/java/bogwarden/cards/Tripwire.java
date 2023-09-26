package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Tripwire extends AbstractTrapCard {
    public final static String ID = makeID("Tripwire");

    public Tripwire() {
        super(ID, CardRarity.COMMON);
        setBlock(6, +2);
        setMagic(1, +1);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelfTop(new DrawCardNextTurnPower(p, magicNumber));
        blckTop();
    }
}