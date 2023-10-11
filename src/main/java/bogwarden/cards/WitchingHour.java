package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.vfx.SparkleHelixEffect;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
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
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(5, +2);
        setMagic(3, +1);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new SparkleHelixEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), SparkleHelixEffect.DURATION - 0.2f);
        dmg(m, BLAST_EFFECT);
        applyToEnemy(m, new WitchingHourPower(m, magicNumber));
    }

    public static class WitchingHourPower extends AbstractBogPower implements OnReceivePowerPower {
        public static String POWER_ID = makeID("WitchingHourPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public WitchingHourPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public boolean onReceivePower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power.type == AbstractPower.PowerType.DEBUFF && !power.ID.equals(GainStrengthPower.POWER_ID) && target == owner && source != owner && !target.hasPower(ArtifactPower.POWER_ID)) {
                flash();
                att(new GainBlockAction(adp(), adp(), amount));
            } 
            return true;
        }
    }
}