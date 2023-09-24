package bogwarden.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Blowpipe extends AbstractBogCard {
    public final static String ID = makeID("Blowpipe");

    public Blowpipe() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(6, +3);
        setMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                p.drawPile.group.stream().filter(c -> c.cost == -2).forEach(c -> cards.addToRandomSpot(c));
                cards.shuffle(AbstractDungeon.cardRandomRng);
                for (int i = 0; i < magicNumber; i++)
                    if (cards.size() > 0) {
                        att(new MoveCardsAction(p.hand, p.drawPile, c -> c == cards.getTopCard()));
                        cards.removeTopCard();
                    }
            }
        });
    }
}