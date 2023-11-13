package bogwarden.cards;

import basemod.BaseMod;
import bogwarden.actions.TriggerTrapAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.Collections;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Hurl extends AbstractBogCard {
    public final static String ID = makeID("Hurl");

    public Hurl() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setRetain(false, true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new MultiGroupSelectAction(
            cardStrings.EXTENDED_DESCRIPTION[magicNumber == 1 ? 0 : 1],
            (cards, groups) -> {
                Collections.reverse(cards);
                cards.forEach(c -> att(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                            if (groups.get(c) == p.drawPile)
                                p.drawPile.moveToDiscardPile(c);
                            p.createHandIsFullDialog();
                        } else {
                            p.hand.moveToHand(c, groups.get(c));
                            p.hand.removeTopCard();
                            p.hand.addToBottom(c);
                            p.hand.refreshHandLayout();
                            p.hand.applyPowers();
                            att(new TriggerTrapAction(m));
                        }
                    }
                }));
            },
            1, false, c -> c instanceof AbstractTrapCard,
            CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.DISCARD_PILE
        ));
    }
}