package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.TheBombPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class FirebombTrap extends AbstractTrapCard {
    public final static String ID = makeID("FirebombTrap");

    public FirebombTrap() {
        super(ID, CardRarity.UNCOMMON);
        setMagic(12, +3);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelfTop(new TheBombPower(p, 2, magicNumber));
    }
}