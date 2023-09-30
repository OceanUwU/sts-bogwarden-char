package bogwarden.cards;

import bogwarden.actions.TriggerTrapAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class WatchAndLearn extends AbstractBogCard {
    public final static String ID = makeID("WatchAndLearn");

    public WatchAndLearn() {
        super(ID, 2, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setBlock(10, +2);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        for (int i = 0; i < magicNumber; i++)
            atb(new TriggerTrapAction());
    }
}