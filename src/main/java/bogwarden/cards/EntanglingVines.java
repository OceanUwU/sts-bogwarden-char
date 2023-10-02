package bogwarden.cards;

import bogwarden.powers.LoseSpinesPower;
import bogwarden.powers.Spines;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class EntanglingVines extends AbstractBogCard {
    public final static String ID = makeID("EntanglingVines");

    public EntanglingVines() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        setMagic(6, +2);
        setSecondMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new WeakPower(m, secondMagic, false));
        applyToSelf(new Spines(p, magicNumber));
        applyToSelf(new LoseSpinesPower(p, magicNumber));
    }
}