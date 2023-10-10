package bogwarden.cardmods;

import basemod.abstracts.AbstractCardModifier;
import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Spines;
import CardAugments.cardmods.AbstractAugment;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpinyMod extends AbstractAugment {
    public static final String ID = makeID("SpinyMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    private static final int EFFECT = 2;

    @Override
    public boolean validCard(AbstractCard card) {
        return isNormalCard(card) && characterCheck(p -> p instanceof TheBogwarden);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        applyToSelf(new Spines(adp(), EFFECT));
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return insertAfterText(rawDescription, String.format(CARD_TEXT[0], EFFECT));
    }

    @Override public AugmentRarity getModRarity() {return AugmentRarity.RARE;}
    @Override public String getPrefix() {return TEXT[0];}
    @Override public String getSuffix() {return TEXT[1];}
    @Override public String getAugmentDescription() {return TEXT[2];}
    @Override public AbstractCardModifier makeCopy() {return new SpinyMod();}
    @Override public String identifier(AbstractCard card) {return ID;}
}