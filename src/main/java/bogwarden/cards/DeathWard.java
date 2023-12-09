package bogwarden.cards;

import basemod.ReflectionHacks;
import bogwarden.powers.AbstractBogPower;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisParticle;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class DeathWard extends AbstractBogCard {
    public final static String ID = makeID("DeathWard");
    private final static int DAMAGE = 3;

    public DeathWard() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(3, +1);
        setSecondMagic(DAMAGE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DeathWardPower(p, magicNumber));
    }

    public static class DeathWardPower extends AbstractBogPower {
        public static String POWER_ID = makeID("DeathWardPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public DeathWardPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            isTwoAmount = true;
            amount2 = DAMAGE;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[owner instanceof AbstractPlayer ? 0 : 3] + DAMAGE + powerStrings.DESCRIPTIONS[owner instanceof AbstractPlayer ? 1 : 4] + amount + powerStrings.DESCRIPTIONS[2];
        }

        private void trigger(boolean isPlayer) {
            flash();
            for (int i = 0; i < amount; i++)
                atb(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        AbstractCreature target = isPlayer ? AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng) : adp();
                        if (target != null) {
                            att(new DamageAction(target, new DamageInfo(owner, DAMAGE, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                            HemokinesisParticle particle = new HemokinesisParticle(owner.hb.cX + MathUtils.random(-60f, 60f), owner.hb.cY + MathUtils.random(-60f, 60f), target.hb.cX, target.hb.cY, target.hb.cX < owner.hb.cX);
                            ReflectionHacks.setPrivate(particle, AbstractGameEffect.class, "color", new Color(1f, 0.13f, 0.96f, 0.6f));
                            particle.renderBehind = false;
                            att(new VFXAction(particle, 0.2f));
                        }
                    }
                });
        }
  
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            if (isPlayer)
                trigger(isPlayer);
        }

        public void atStartOfTurn() {
            if (owner instanceof AbstractMonster)
                trigger(false);
        }
    }
}