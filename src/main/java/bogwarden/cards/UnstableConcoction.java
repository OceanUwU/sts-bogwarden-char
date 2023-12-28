package bogwarden.cards;

import bogwarden.potions.ExplosivePotionPlus;
import bogwarden.potions.FirePotionPlus;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import basemod.ReflectionHacks;

public class UnstableConcoction extends AbstractBogCard {
    public final static String ID = makeID("UnstableConcoction");

    public UnstableConcoction() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(4);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = isEliteOrBoss() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPotion potion = isEliteOrBoss() ? (upgraded ? new FirePotionPlus() : new FirePotion()) : (upgraded ? new ExplosivePotionPlus() : new ExplosivePotion());
        ThrowPotionAndFillItEffect effect = new ThrowPotionAndFillItEffect(potion, p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY);
        vfx(effect, 0.5f);
        vfx(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.2f);
        dmg(m, AbstractGameAction.AttackEffect.NONE);
    }

    private static class ThrowPotionAndFillItEffect extends AbstractGameEffect {
        private static final float ROTATIONAL_VEL = 580f;
        private static final float DURATION = 1.5f;
        private static final float UPPIES = 300f * Settings.scale;
        private static final float SCALE_MULT = 2.5f;

        private float x, y;
        private float startX, startY, endX, endY;
        private AbstractPotion emptyPotion, filledPotion, potionToGive;

        public ThrowPotionAndFillItEffect(AbstractPotion potion, float startX, float startY, float endX, float endY) {
            duration = DURATION;
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            potionToGive = potion;
            filledPotion = potionToGive.makeCopy();
            Color transparent = new Color(0f, 0f, 0f, 0f);
            emptyPotion = filledPotion.makeCopy();
            emptyPotion.liquidColor = transparent;
            emptyPotion.hybridColor = transparent;
            emptyPotion.spotsColor = transparent;
        }

        public void update() {
            rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
            float progress = (1f - (duration / DURATION)) * 2f;
            if (progress > 1f)
                progress = 2f - progress;
            x = startX + (endX - startX) * progress;
            y = startY + (endY - startY) * progress + (float)Math.sin(Math.PI * progress) * UPPIES;
            if (progress < 0.3f)
                scale = Settings.scale * SCALE_MULT * (progress / 0.3f);
            else
                scale = Settings.scale * SCALE_MULT;
            duration -= Gdx.graphics.getDeltaTime();
            if (duration <= 0f) {
                if (AbstractDungeon.player.hasRelic(Sozu.ID))
                    AbstractDungeon.player.getRelic(Sozu.ID).flash();
                else
                    AbstractDungeon.player.obtainPotion(potionToGive.makeCopy());
                isDone = true;
            }
        }

        public void render(SpriteBatch sb) {
            AbstractPotion potion = duration < DURATION / 2f ? filledPotion : emptyPotion;
            potion.posX = x;
            potion.posY = y;
            ReflectionHacks.setPrivate(potion, AbstractPotion.class, "angle", rotation);
            potion.scale = scale;
            potion.render(sb);
        }

        public void dispose() {}
    }
}