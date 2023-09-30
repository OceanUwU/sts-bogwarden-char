package bogwarden.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.potions.ExplosivePotion;

import static bogwarden.BogMod.makeID;

public class ExplosivePotionPlus extends ExplosivePotion {
    public static final String POTION_ID = makeID("ExplosivePotionPlus");

    public ExplosivePotionPlus() {
        super();
        ID = POTION_ID;
        name += "+";
        rarity = PotionRarity.PLACEHOLDER;
        spotsColor = new Color(0.79f, 0.34f, 0.90f, 1f);
        initializeData();
    }

    @Override
    public int getPotency(int ascensionlevel) {
        return 2 + super.getPotency(ascensionlevel);
    }
}