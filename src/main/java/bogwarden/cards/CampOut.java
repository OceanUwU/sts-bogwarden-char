package bogwarden.cards;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;
import com.megacrit.cardcrawl.vfx.campfire.CampfireEndingBurningEffect;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;

public class CampOut extends AbstractBogCard {
    public final static String ID = makeID("CampOut");

    public CampOut() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        setExhaust(true);
        tags.add(CardTags.HEALING);
        MultiCardPreview.add(this, new Bonfire(), new Forge());
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
        keywords.addAll(new Bonfire().keywords);
        keywords.addAll(new Forge().keywords);
        ArrayList<String> foundKeywords = new ArrayList<>();
        for (String keyword : keywords)
            if (!foundKeywords.contains(keyword))
                foundKeywords.add(keyword);
        keywords = foundKeywords;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> choices = new ArrayList<>();
        choices.add(new Bonfire());
        choices.add(new Forge());
        if (upgraded)
            for (AbstractCard c : choices)
                c.upgrade();
        atb(new FieryChooseOneAction(choices));
    }

    @Override
    public void upp() {
        super.upp();
        for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this))
            c.upgrade();
    }

    public static class FieryChooseOneAction extends AbstractGameAction {
        private static ArrayList<AbstractCard> choicesLmao;
        private ArrayList<AbstractCard> choices;

        public FieryChooseOneAction(ArrayList<AbstractCard> choices) {
            duration = Settings.ACTION_DUR_FAST;
            this.choices = choices;
            choicesLmao = choices;
        }

        public void update() {
            if (duration == Settings.ACTION_DUR_FAST)
                AbstractDungeon.cardRewardScreen.chooseOneOpen(choices);
            tickDuration();
        }

        @SpirePatch(clz=CardRewardScreen.class, method="update")
        public static class ShowFireOnReward {
            private static float fireTimer = 0f;
            
            public static void Postfix(CardRewardScreen __instance) {
                if (__instance.rewardGroup == choicesLmao && !PeekButton.isPeeking) {
                    fireTimer -= Gdx.graphics.getDeltaTime();
                    if (fireTimer <= 0f) {
                        fireTimer += 0.05f;
                        for (int i = 0; i < 4; i++)
                            AbstractDungeon.effectList.add(new CampfireEndingBurningEffect());
                    }
                }
            }
        }
    }
}