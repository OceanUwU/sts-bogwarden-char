package bogwarden.cards;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import bogwarden.actions.TriggerTrapAction;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;
import java.util.Collections;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Hurl extends AbstractBogCard {
    public final static String ID = makeID("Hurl");

    public Hurl() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        boolean upped = upgraded;
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
                            if (upped) {
                                c.returnToHand = true;
                                OnlyReturnOncePatch.cardsToOnlyReturnOnce.add(c);
                            }
                            att(new TriggerTrapAction(m));
                        }
                    }
                }));
            },
            1, false, c -> c instanceof AbstractTrapCard,
            CardGroup.CardGroupType.HAND, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.DISCARD_PILE
        ));
    }

    @SpirePatch(clz=UseCardAction.class, method="update")
    public static class OnlyReturnOncePatch {
        public static ArrayList<AbstractCard> cardsToOnlyReturnOnce = new ArrayList<>();

        @SpireInsertPatch(rloc=57)
        public static void Insert(UseCardAction __instance) {
            AbstractCard targetCard = ReflectionHacks.getPrivate(__instance, UseCardAction.class, "targetCard");
            if (cardsToOnlyReturnOnce.contains(targetCard)) {
                targetCard.returnToHand = false;
                cardsToOnlyReturnOnce.remove(targetCard);
            }
        }
    }
}