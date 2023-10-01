package bogwarden.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Bushwhack extends AbstractBogCard {
    public final static String ID = makeID("Bushwhack");

    public Bushwhack() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setBlock(4, +1);
        setMagic(1, +1);
        setSecondMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new PlatedArmorPower(p, magicNumber));
        atb(new DrawCardAction(secondMagic));
    }
}