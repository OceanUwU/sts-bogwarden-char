package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.LoseSpinesPower;
import bogwarden.powers.Spines;

public class EntanglingVines extends AbstractBogCard {
    public final static String ID = makeID("EntanglingVines");

    public EntanglingVines() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setMagic(11, +3);
        setRetain(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new Spines(p, magicNumber));
        applyToSelf(new LoseSpinesPower(p, magicNumber));
    }
}