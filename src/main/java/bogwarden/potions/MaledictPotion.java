package bogwarden.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import bogwarden.BogMod;
import bogwarden.cards.AbstractBogCard;
import bogwarden.powers.Maledict;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.PotionFlavorFields;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class MaledictPotion extends CustomPotion {
    public static final String POTION_ID = makeID("MaledictPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public MaledictPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.H, PotionColor.WEAK);
        labOutlineColor = BogMod.characterColor;
        isThrown = true;
        this.targetRequired = true;
        PotionFlavorFields.boxColor.set(this, AbstractBogCard.FLAVOUR_BOX_COLOR);
        PotionFlavorFields.textColor.set(this, Color.WHITE);
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(makeID("maledict")), BaseMod.getKeywordDescription(makeID("maledict"))));
    }

    public void use(AbstractCreature target) {
        atb(new ApplyPowerAction(target, adp(), new Maledict(target, potency), potency));
    }

    public int getPotency(int ascensionlevel) {
        return 3;
    }

    public AbstractPotion makeCopy() {
        return new MaledictPotion();
    }
}