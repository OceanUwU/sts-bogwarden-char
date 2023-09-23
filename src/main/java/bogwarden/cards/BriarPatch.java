package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.Spines;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BriarPatch extends AbstractBogCard {
    public final static String ID = makeID("BriarPatch");

    public BriarPatch() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(6, +2);
        setSecondMagic(50);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new Spines(p, magicNumber));
        applyToSelf(new BriarPatchPower(p, secondMagic));
    }

    public static class BriarPatchPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BriarPatchPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BriarPatchPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

  
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != owner) {
                flash();
                addToTop((AbstractGameAction)new DamageAction(info.owner, new DamageInfo(owner, pwrAmt(owner, Spines.POWER_ID) * amount / 100, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
            } 
            return damageAmount;
        }
    }
}