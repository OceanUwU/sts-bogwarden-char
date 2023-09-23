package bogwarden.cards;

import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class EagleEyes extends AbstractBogCard {
    public final static String ID = makeID("EagleEyes");
    private static int scryAmt = 0;

    public EagleEyes() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(8, +3);
        setMagic(2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        scryAmt = 0;
        forAllMonstersLiving(mo -> scryAmt += magicNumber);
        if (scryAmt > 0)
            atb(new ScryAction(scryAmt));
    }
}