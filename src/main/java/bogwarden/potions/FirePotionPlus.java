package bogwarden.potions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.FirePotion;

import static bogwarden.BogMod.makeID;

public class FirePotionPlus extends FirePotion {
    public static final String POTION_ID = makeID("FirePotionPlus");

    public FirePotionPlus() {
        super();
        ID = POTION_ID;
        name += "+";
        rarity = PotionRarity.PLACEHOLDER;
        spotsColor = new Color(0.79f, 0.34f, 0.90f, 1f);
        initializeData();
    }

    @Override
    public int getPotency(int ascensionlevel) {
        return 4 + super.getPotency(ascensionlevel);
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FirePotionPlus();
    }

    @SpirePatch(clz=PotionHelper.class, method="initialize")
    public static class RemoveFromPool {
        public static void Postfix() {
            PotionHelper.potions.remove(POTION_ID);
        }
    }
}