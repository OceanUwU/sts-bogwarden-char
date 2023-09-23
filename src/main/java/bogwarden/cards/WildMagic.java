package bogwarden.cards;

import bogwarden.patches.ScryPatches;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class WildMagic extends AbstractBogCard {
    public final static String ID = makeID("WildMagic");

    public WildMagic() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(11);
        setMagic(3);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.LIGHTNING);
        ScryAction action = new ScryAction(magicNumber);
        ScryPatches.ActionFields.fromWildMagic.set(action, true);
        ScryPatches.ActionFields.upgraded.set(action, upgraded);
        atb(action);
    }

    public static void transform(CardGroup group, boolean upgraded) {
        for (AbstractCard c : group.group)
            if (adp().drawPile.contains(c)) {
                AbstractCard.CardRarity cardRarity;
                int roll = AbstractDungeon.cardRandomRng.random(99);
                if (roll < 55)
                    cardRarity = AbstractCard.CardRarity.COMMON;
                else if (roll < 85)
                    cardRarity = AbstractCard.CardRarity.UNCOMMON;
                else
                    cardRarity = AbstractCard.CardRarity.RARE;
                AbstractCard generated = CardLibrary.getAnyColorCard(AbstractCard.CardType.SKILL, cardRarity);
                att(new MakeTempCardInDrawPileAction(generated, 1, false, true));
                att(new ExhaustSpecificCardAction(c, adp().drawPile, true));
            }
    }
}