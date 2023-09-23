package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class NightTerror extends AbstractBogCard {
    public final static String ID = makeID("NightTerror");

    public NightTerror() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setMagic(99);
        setUpgradedCost(1);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> applyToEnemy(m, new VulnerablePower(m, magicNumber, false)));
    }
}