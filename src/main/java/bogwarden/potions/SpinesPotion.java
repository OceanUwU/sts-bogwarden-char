package bogwarden.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import bogwarden.BogMod;
import bogwarden.cards.AbstractBogCard;
import bogwarden.powers.Spines;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.PotionFlavorFields;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpinesPotion extends CustomPotion {
    public static final String POTION_ID = makeID("SpinesPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public SpinesPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPIKY, PotionColor.WEAK);
        labOutlineColor = BogMod.characterColor;
        PotionFlavorFields.boxColor.set(this, AbstractBogCard.FLAVOUR_BOX_COLOR);
        PotionFlavorFields.textColor.set(this, Color.WHITE);
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(makeID("spines")), BaseMod.getKeywordDescription(makeID("spines"))));
    }

    public void use(AbstractCreature abstractCreature) {
        applyToSelf(new Spines(adp(), potency));
    }

    public int getPotency(int ascensionlevel) {
        return 12;
    }

    public AbstractPotion makeCopy() {
        return new SpinesPotion();
    }
}