package bogwarden.cards;

import bogwarden.powers.Maledict;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class VilePowder extends AbstractBogCard {
    public final static String ID = makeID("VilePowder");

    public VilePowder() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);
        setBlock(5, +3);
        setMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        forAllMonstersLiving(mo -> applyToEnemy(mo, new Maledict(mo, magicNumber)));
    }
}