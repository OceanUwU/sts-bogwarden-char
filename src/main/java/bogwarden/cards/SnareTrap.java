package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SnareTrap extends AbstractTrapCard {
    public final static String ID = makeID("SnareTrap");

    public SnareTrap() {
        super(ID, CardRarity.COMMON);
        setMagic(2);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        if (upgraded)
            forAllMonstersLiving(mo -> applyToEnemyTop(mo, new WeakPower(mo, magicNumber, false)));
        else
            applyToEnemyTop(m, new WeakPower(m, magicNumber, false));
    }
}