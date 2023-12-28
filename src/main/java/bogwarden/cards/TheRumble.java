package bogwarden.cards;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TheRumble extends AbstractBogCard {
    public final static String ID = makeID("TheRumble");

    public TheRumble() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        setDamage(6, +2);
        isMultiDamage = true;
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = isEliteOrBoss() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < (isEliteOrBoss() ? 2 : 1); i++)
            allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        forAllMonstersLiving(mo -> vfx(new CreatureFlyEffect(mo)));
    }

    public static class CreatureFlyEffect extends AbstractGameEffect {
        private static final float ACCELERATION = -2000f;
        private static final float SMOKE_GAP = 0.05f;
        public AbstractCreature target;
        private Bone rootBone;
        private float initialRotation;
        private float y = 0f;
        private float vel = MathUtils.random(1200f, 1800f);
        private float wholeDuration = vel * 2f / -ACCELERATION;
        private float timer = 0f;
        private float smokeTimer = SMOKE_GAP;

        public CreatureFlyEffect(AbstractCreature target) {
            this.target = target;
            if (target != null) {
                Skeleton skeleton = ReflectionHacks.getPrivate(target, AbstractCreature.class, "skeleton");
                if (skeleton != null) {
                    rootBone = skeleton.getRootBone();
                }
            }
        }

        public void update() {
            if (target == null) {
                isDone = true;
                return;
            }
            for (AbstractGameEffect effect : AbstractDungeon.effectList) {
                if (effect == this)
                    break;
                else if (effect instanceof CreatureFlyEffect && ((CreatureFlyEffect)effect).target == target)
                    return;
            }
            if (timer == 0f && rootBone != null)
                initialRotation = rootBone.getRotation();
            while (smokeTimer >= SMOKE_GAP) {
                smokeTimer -= SMOKE_GAP;
                AbstractDungeon.effectsQueue.add(new RumbleSmokeEffect(target.hb.cX, target.animY + target.drawY));
            }
            y += vel * Gdx.graphics.getDeltaTime() * Settings.scale;
            vel += ACCELERATION * Gdx.graphics.getDeltaTime();
            timer += Gdx.graphics.getDeltaTime();
            smokeTimer += Gdx.graphics.getDeltaTime();
            if (y <= 0f) {
                target.animY = 0;
                if (rootBone != null)
                    rootBone.setRotation(initialRotation);
                isDone = true;
            } else {
                target.animY = y;
                if (rootBone != null)
                    rootBone.setRotation(initialRotation + Math.min(timer / wholeDuration, 1f) * 360f);
            }
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}

        private static class RumbleSmokeEffect extends AbstractGameEffect {
            private static float DURATION = 0.6f;
            private static float OPACITY = 0.5f;
            private static float FINAL_SCALE = 0.3f;
            private float x, y;
            private float rotationalVel = MathUtils.random(-50f, 50f);
            private float startScale;
            private boolean shook = false;
            private TextureAtlas.AtlasRegion img;

            public RumbleSmokeEffect(float x, float y) {
                this.x = x;
                this.y = y;
                renderBehind = true;
                duration = DURATION;
                rotation = MathUtils.random(0f, 360f);
                scale *= MathUtils.random(0.5f, 1.5f);
                startScale = scale;
                color = new Color(0.7f, 0.7f, 0.7f, OPACITY);
                img = MathUtils.randomBoolean(0.5f) ? ImageMaster.EXHAUST_L : ImageMaster.EXHAUST_S;
            }

            public void update() {
                if (!shook) {
                    shook = true;
                    CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false); 
                }
                color.a = (duration / DURATION) * OPACITY;
                scale = startScale * (FINAL_SCALE + (duration / DURATION) * (1f - FINAL_SCALE));
                rotation += rotationalVel * Gdx.graphics.getDeltaTime();
                duration -= Gdx.graphics.getDeltaTime();
                if (duration < 0f)
                   isDone = true;
            }

            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(img, x, y, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scale, scale, rotation);
            }

            public void dispose() {}
        }
    }
}