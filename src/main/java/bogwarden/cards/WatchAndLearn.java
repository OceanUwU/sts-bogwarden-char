package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.actions.TriggerTrapAction;

public class WatchAndLearn extends AbstractBogCard {
    public final static String ID = makeID("WatchAndLearn");

    public WatchAndLearn() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(10, +4);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                p.hand.group.stream().filter(c -> c instanceof AbstractTrapCard).forEach(c -> att(new TriggerTrapAction()));
            }
        });
    }
}