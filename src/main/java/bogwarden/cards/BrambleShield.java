package bogwarden.cards;

import bogwarden.powers.Spines;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BrambleShield extends AbstractBogCard {
    public final static String ID = makeID("BrambleShield");

    public BrambleShield() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(4, +3);
    }
    
    @Override
    public void applyPowers() {
        int realBaseBlock = baseBlock;
        baseBlock += pwrAmt(adp(), Spines.POWER_ID);
        super.applyPowers();
        baseBlock = realBaseBlock;
        isBlockModified = block != baseBlock;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        blck();
    }
}