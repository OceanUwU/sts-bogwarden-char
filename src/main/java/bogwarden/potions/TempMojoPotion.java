package bogwarden.potions;

import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import bogwarden.BogMod;
import bogwarden.powers.LoseMojoPower;
import bogwarden.powers.Mojo;
import bogwarden.vfx.MojoFlashEffect;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TempMojoPotion extends CustomPotion {
    public static final String POTION_ID = makeID("TempMojoPotion");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public TempMojoPotion() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.COMMON, PotionSize.FAIRY, PotionColor.STEROID);
        labOutlineColor = BogMod.characterColor;
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(makeID("mojo")), BaseMod.getKeywordDescription(makeID("mojo"))));
    }

    public void use(AbstractCreature abstractCreature) {
        vfx(new MojoFlashEffect(adp().hb.cX, adp().hb.cY));
        applyToSelf(new Mojo(adp(), potency));
        applyToSelf(new LoseMojoPower(adp(), potency));
    }

    public int getPotency(int ascensionlevel) {
        return 5;
    }

    public AbstractPotion makeCopy() {
        return new TempMojoPotion();
    }
}