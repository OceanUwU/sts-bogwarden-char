package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class WitchingHour extends AbstractBogCard {
    public final static String ID = makeID("WitchingHour");

    public WitchingHour() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(3, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new WitchingHourPower(p, magicNumber));
    }

    public static class WitchingHourPower extends AbstractBogPower {
        public static String POWER_ID = makeID("WitchingHourPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public WitchingHourPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power.type == AbstractPower.PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) && source == owner && target != owner && !target.hasPower(ArtifactPower.POWER_ID)) {
                flash();
                atb(new GainBlockAction(owner, owner, amount));
            } 
        }
    }
}