package bogwarden.potions;

import basemod.abstracts.CustomPotion;
import basemod.helpers.CardPowerTip;
import bogwarden.BogMod;
import bogwarden.cards.AbstractBogCard;
import bogwarden.cards.Blast;
import bogwarden.vfx.IncantationEffect;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.PotionFlavorFields;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BogWater extends CustomPotion {
    public static final String POTION_ID = makeID("BogWater");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public BogWater() {
        super(potionStrings.NAME, POTION_ID, PotionRarity.RARE, PotionSize.M, PotionColor.SWIFT);
        labOutlineColor = BogMod.characterColor;
        PotionFlavorFields.boxColor.set(this, AbstractBogCard.FLAVOUR_BOX_COLOR);
        PotionFlavorFields.textColor.set(this, Color.WHITE);
    }

    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        Blast blast = new Blast();
        blast.upgrade();
        tips.add(new CardPowerTip(blast));
    }

    public void use(AbstractCreature abstractCreature) {
        vfx(new IncantationEffect());
        Blast blast = new Blast();
        blast.upgrade();
        makeInHand(blast, potency);
    }

    public int getPotency(int ascensionlevel) {
        return 3;
    }

    public AbstractPotion makeCopy() {
        return new BogWater();
    }
}