package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class VengefulTotem extends AbstractTrapCard {
    public final static String ID = makeID("VengefulTotem");

    public VengefulTotem() {
        super(ID, CardRarity.UNCOMMON);
        setMagic(2, +1);
        setSecondMagic(3, +1);
        setExhaust(true);
        sfx = BogAudio.TOTEM_TRIGGER;
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = isEliteOrBoss() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToEnemyTop(m, new VengeancePower(m, isEliteOrBoss() ? secondMagic : magicNumber));
    }

    public static class VengeancePower extends AbstractBogPower {
        public static final String POWER_ID = makeID("VengeancePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public VengeancePower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (damageAmount > 0) {
                flash();
                att(new GainBlockAction(adp(), amount, true));
            } 
        }
    }
}