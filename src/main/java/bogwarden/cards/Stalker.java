package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Stalker extends AbstractBogCard {
    public final static String ID = makeID("Stalker");

    public Stalker() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(9, +3);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    public void onDiscardedViaScry() {
        AbstractCard self = this;
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                if (adp().discardPile.contains(self)) {
                    adp().discardPile.removeCard(self);
                    att(new NewQueueCardAction(self, true, false, true));
                }
            }
        });
    }
}