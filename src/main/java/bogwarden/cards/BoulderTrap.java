package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class BoulderTrap extends AbstractTrapCard {
    public final static String ID = makeID("BoulderTrap");

    public BoulderTrap() {
        super(ID, CardRarity.RARE);
        setDamage(40);
        setSecondMagic(5, -1);
        setMagic(0);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        magicNumber = ++baseMagicNumber;
        isMagicNumberModified = magicNumber >= secondMagic;
    }

    @Override
    public void upp() {
        super.upp();
        isMagicNumberModified = magicNumber >= secondMagic;
    }
}