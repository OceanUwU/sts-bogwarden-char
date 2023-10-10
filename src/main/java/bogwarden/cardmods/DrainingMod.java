package bogwarden.cardmods;

import basemod.abstracts.AbstractCardModifier;
import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Drained;
import CardAugments.cardmods.AbstractAugment;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class DrainingMod extends AbstractAugment {
    public static final String ID = makeID("DrainingMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    private static final int EFFECT = 2;

    @Override
    public boolean validCard(AbstractCard card) {
        return characterCheck(p -> p instanceof TheBogwarden) && card.cost > 0 && cardCheck(card, c -> doesntUpgradeCost());
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        card.cost = card.costForTurn = Math.max(card.cost - 1, 0);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        applyToSelf(new Drained(adp(), EFFECT));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return insertAfterText(rawDescription, String.format(CARD_TEXT[0], EFFECT));
    }

    @Override public AugmentRarity getModRarity() {return AugmentRarity.UNCOMMON;}
    @Override public String getPrefix() {return TEXT[0];}
    @Override public String getSuffix() {return TEXT[1];}
    @Override public String getAugmentDescription() {return TEXT[2];}
    @Override public AbstractCardModifier makeCopy() {return new DrainingMod();}
    @Override public String identifier(AbstractCard card) {return ID;}
}