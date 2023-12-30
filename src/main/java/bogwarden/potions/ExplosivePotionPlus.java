package bogwarden.potions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.PotionFlavorFields;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.ExplosivePotion;

import static bogwarden.BogMod.makeID;

import bogwarden.cards.AbstractBogCard;

public class ExplosivePotionPlus extends ExplosivePotion {
    public static final String POTION_ID = makeID("ExplosivePotionPlus");
    public static final String FLAVOR = CardCrawlGame.languagePack.getUIString(POTION_ID+"FlavorText").TEXT[0];

    public ExplosivePotionPlus() {
        super();
        ID = POTION_ID;
        name += "+";
        rarity = PotionRarity.PLACEHOLDER;
        spotsColor = new Color(0.79f, 0.34f, 0.90f, 1f);
        initializeData();
        PotionFlavorFields.flavor.set(this, FLAVOR);
        PotionFlavorFields.boxColor.set(this, AbstractBogCard.FLAVOUR_BOX_COLOR);
        PotionFlavorFields.textColor.set(this, Color.WHITE);
    }

    @Override
    public int getPotency(int ascensionlevel) {
        return 2 + super.getPotency(ascensionlevel);
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ExplosivePotionPlus();
    }

    @SpirePatch(clz=PotionHelper.class, method="initialize")
    public static class RemoveFromPool {
        public static void Postfix() {
            PotionHelper.potions.remove(POTION_ID);
        }
    }
}