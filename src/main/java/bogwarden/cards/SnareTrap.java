package bogwarden.cards;

import bogwarden.powers.SnaredPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SnareTrap extends AbstractTrapCard {
    public final static String ID = makeID("SnareTrap");

    public SnareTrap() {
        super(ID, CardRarity.COMMON);
        setMagic(9, +4);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToEnemyTop(m, new SnaredPower(m, magicNumber));
    }
}