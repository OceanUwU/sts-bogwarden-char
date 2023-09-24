package bogwarden.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import bogwarden.BogMod;
import bogwarden.powers.Mojo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class MojoPotion extends CustomPotion {
    public static final String POTION_ID = makeID("MojoPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public MojoPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.SWIFT);
        labOutlineColor = BogMod.characterColor;
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(makeID("mojo")), BaseMod.getKeywordDescription(makeID("mojo"))));
    }

    public void use(AbstractCreature abstractCreature) {
        applyToSelf(new Mojo(abstractCreature, potency));
    }

    public int getPotency(int ascensionlevel) {
        return 2;
    }

    public AbstractPotion makeCopy() {
        return new MojoPotion();
    }
}