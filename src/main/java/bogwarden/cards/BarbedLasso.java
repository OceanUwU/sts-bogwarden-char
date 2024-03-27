package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.SnaredPower;
import bogwarden.vfx.LassoEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BarbedLasso extends AbstractBogCard {
    public final static String ID = makeID("BarbedLasso");

    public BarbedLasso() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(6, +1);
        setMagic(5, +2);
        setSecondMagic(3, +1);
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = isEliteOrBoss() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new LassoEffect(p.hb.x + p.hb.width, p.hb.cY, m.hb.cX, m.hb.cY, Color.WHITE, "vfx/lasso.png"), LassoEffect.DURATION - 0.3f);
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        applyToEnemy(m, new SnaredPower(m, magicNumber));
        if (isEliteOrBoss())
            applyToEnemy(m, new BarbedLassoPower(m, secondMagic));
    }

    public static class BarbedLassoPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BarbedLassoPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BarbedLassoPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
    }
}