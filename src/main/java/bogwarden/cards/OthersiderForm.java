package bogwarden.cards;

import basemod.helpers.BaseModCardTags;
import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.Mojo;
import bogwarden.vfx.MojoFlashEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class OthersiderForm extends AbstractBogCard {
    public final static String ID = makeID("OthersiderForm");

    public OthersiderForm() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(2, +1);
        setSecondMagic(1);
        tags.add(BaseModCardTags.FORM);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new OthersiderFormMojoPower(p, magicNumber));
        applyToSelf(new OthersiderFormStrengthPower(p, secondMagic));
    }

    public static class OthersiderFormMojoPower extends AbstractBogPower {
        public static String POWER_ID = makeID("OthersiderFormMojoPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public OthersiderFormMojoPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            priority = 1;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void onInitialApplication() {
            owner.flipHorizontal = !owner.flipHorizontal;
        }

        public void onRemove() {
            owner.flipHorizontal = !owner.flipHorizontal;
        }

        public void onVictory() {
            owner.flipHorizontal = false;
        }
  
        public void atStartOfTurn() {
            flash();
            vfx(new MojoFlashEffect(owner.hb.cX, owner.hb.cY));
            applyToSelf(new Mojo(owner, amount));
        }
    }

    public static class OthersiderFormStrengthPower extends AbstractBogPower {
        public static String POWER_ID = makeID("OthersiderFormStrengthPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public OthersiderFormStrengthPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurnPostDraw() {
            flash();
            applyToSelf(new StrengthPower(owner, -amount));
        }
    }
}