package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.Mojo;
import bogwarden.powers.Spines;

public class BrambleShield extends AbstractBogCard {
    public final static String ID = makeID("BrambleShield");

    public BrambleShield() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(0);
        setMagic(3, +3);
    }
    
    public void applyPowers() {
        baseBlock = pwrAmt(adp(), Spines.POWER_ID) + pwrAmt(adp(), Mojo.POWER_ID) + magicNumber;
        super.applyPowers();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }
}