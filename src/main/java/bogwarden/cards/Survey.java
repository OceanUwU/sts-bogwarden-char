package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Survey extends AbstractBogCard {
    public final static String ID = makeID("Survey");

    public Survey() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2);
        setBlock(5, +2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new DrawCardAction(magicNumber, new AbstractGameAction() {
            public void update() {
                isDone = true;
                for (AbstractCard c : DrawCardAction.drawnCards)
                    if (c.type.equals(AbstractCard.CardType.SKILL))
                        blckTop();
            }
        }));
    }
}