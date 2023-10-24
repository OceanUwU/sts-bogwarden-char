package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BlendingIn extends AbstractBogCard {
    public final static String ID = makeID("BlendingIn");
    private static boolean trigger;

    public BlendingIn() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(0, +3);
        setSecondMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new BlendingInAction(p, magicNumber, secondMagic, null));
        if (magicNumber > 0)
            applyToSelf(new BlendingInBlockPower(p, magicNumber));
        applyToSelf(new BlendingInPower(p, secondMagic));
    }

    private static boolean shouldTrigger() {
        trigger = false;
        forAllMonstersLiving(mo -> {if (mo.getIntentBaseDmg() <= 0) trigger = true;});
        return trigger;
    }

    public static class BlendingInPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BlendingInPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BlendingInPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            atb(new BlendingInAction(owner, 0, amount, this));
        }
    }

    public static class BlendingInBlockPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BlendingInBlockPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BlendingInBlockPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            atb(new BlendingInAction(owner, amount, 0, this));
        }
    }

    private static class BlendingInAction extends AbstractGameAction {
        public BlendingInAction(AbstractCreature target, int block, int blur, AbstractPower powerToFlash) {
            this.target = target;
            this.block = block;
            this.blur = blur;
            this.powerToFlash = powerToFlash;
        }

        private int block, blur; 
        private AbstractPower powerToFlash;

        public void update() {
            isDone = true;
            if (!shouldTrigger())
                return;
            if (powerToFlash != null)
                powerToFlash.flash();
            if (blur > 0)
                applyToSelfTop(new BlurPower(target, blur));
            if (block > 0)
                att(new GainBlockAction(target, target, block));
            trigger = false;
        }
    }
}