package bogwarden.cardmods;

import basemod.ReflectionHacks;
import basemod.abstracts.AbstractCardModifier;
import bogwarden.characters.TheBogwarden;
import bogwarden.relics.GrabbyHand;
import CardAugments.cardmods.AbstractAugment;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import static bogwarden.BogMod.makeID;

public class NonAttackMod extends AbstractAugment {
    public static final String ID = makeID("NonAttackMod");
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    @Override
    public boolean validCard(AbstractCard card) {
        return isNormalCard(card) && characterCheck(p -> p instanceof TheBogwarden && !p.hasRelic(GrabbyHand.ID)) && card.baseDamage >= 0 && card.damageTypeForTurn == DamageType.NORMAL;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        ReflectionHacks.setPrivate(card, AbstractCard.class, "damageType", DamageType.THORNS);
        card.damageTypeForTurn = DamageType.THORNS;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return rawDescription.replace(CARD_TEXT[0], CARD_TEXT[1]);
    }

    @Override public AugmentRarity getModRarity() {return AugmentRarity.COMMON;}
    @Override public String getPrefix() {return TEXT[0];}
    @Override public String getSuffix() {return TEXT[1];}
    @Override public String getAugmentDescription() {return TEXT[2];}
    @Override public AbstractCardModifier makeCopy() {return new NonAttackMod();}
    @Override public String identifier(AbstractCard card) {return ID;}
}