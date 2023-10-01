package bogwarden.cards;

import bogwarden.actions.TriggerTrapAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Snip extends AbstractBogCard {
    public final static String ID = makeID("Snip");

    public Snip() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        setRetain(false, true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new TriggerTrapAction());
    }
}