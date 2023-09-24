package bogwarden.potions;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;
import bogwarden.BogMod;
import bogwarden.actions.TriggerTrapAction;
import bogwarden.cards.AbstractTrapCard;
import bogwarden.util.TexLoader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import java.util.ArrayList;
import java.util.Collections;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class BottleOfTricks extends CustomPotion {
    public static final String POTION_ID = makeID("BottleOfTricks");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BottleOfTricks() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.RARE, PotionSize.BOTTLE, PotionColor.SWIFT);
        ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", TexLoader.getTexture(makeImagePath("potions/BottleOfTricks.png")));
        labOutlineColor = BogMod.characterColor;
    }

    public void initializeData() {
        potency = getPotency();
        description = (potency == 1 ? potionStrings.DESCRIPTIONS[0] : potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2]) + potionStrings.DESCRIPTIONS[3];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(makeID("trap")), BaseMod.getKeywordDescription(makeID("trap"))));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(makeID("trigger")), BaseMod.getKeywordDescription(makeID("trigger"))));
    }

    public void use(AbstractCreature abstractCreature) {
        atb(new DiscoverTrapAction(potency));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                adp().hand.group.stream().filter(c -> c instanceof AbstractTrapCard).forEach(c -> att(new TriggerTrapAction()));
            }
        });
    }

    public int getPotency(int ascensionlevel) {
        return 1;
    }

    public AbstractPotion makeCopy() {
        return new BottleOfTricks();
    }

    public static class DiscoverTrapAction extends AbstractGameAction {
        private boolean retrieveCard = false;
        private int count;

        public DiscoverTrapAction(int count) {
            actionType = ActionType.CARD_MANIPULATION;
            duration = Settings.ACTION_DUR_FAST;
            this.count = count;
        }

        public void update() {
            if (duration == Settings.ACTION_DUR_FAST) {
                AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(), CardRewardScreen.TEXT[1], true);
                tickDuration();
            } else {
                if (!retrieveCard) {
                    if (AbstractDungeon.cardRewardScreen.discoveryCard != null)
                        for (int i = 0; i < count; i++) {
                            AbstractCard disCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
                            disCard.setCostForTurn(0);
                            disCard.current_x = -1000.0F * Settings.scale;
                            if (AbstractDungeon.player.hand.size() < 10)
                                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                            else
                                AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                        }
                    AbstractDungeon.cardRewardScreen.discoveryCard = null;
                    retrieveCard = true;
                }
                tickDuration();
            }
        }

        private ArrayList<AbstractCard> generateCardChoices() {
            ArrayList<AbstractCard> cardsList = new ArrayList<>();
            ArrayList<AbstractCard> selectionsList = new ArrayList<>();
            for (AbstractCard q : CardLibrary.getAllCards())
                if (q instanceof AbstractTrapCard && !q.hasTag(AbstractCard.CardTags.HEALING) && !(q.color == AbstractCard.CardColor.COLORLESS || q.color == AbstractCard.CardColor.CURSE)) {
                    AbstractCard r = q.makeCopy();
                    cardsList.add(r);
                }
            Collections.shuffle(cardsList, AbstractDungeon.cardRandomRng.random);
            for (int i = 0; i < 3; i++)
                selectionsList.add(cardsList.get(i));
            return selectionsList;
        }
    }
}