package bogwarden.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SludgeBomb extends AbstractTrapCard {
    public final static String ID = makeID("SludgeBomb");

    public SludgeBomb() {
        super(ID, CardRarity.RARE);
        setExhaust(true);
        setDamage(6, +2);
        setMagic(1, +1);
        isMultiDamage = true;
        damageType = damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLivingTop(mo -> applyToEnemyTop(mo, new StrengthPower(mo, -magicNumber)));
        allDmgTop(AbstractGameAction.AttackEffect.POISON);
        forAllMonstersLiving(mo -> vfxTop(new ExplosionSmallEffect(mo.hb.cX, mo.hb.cY)));
        forAllMonstersLiving(mo -> vfxTop(new SludgeBombEffect(p.hb.cX, p.hb.cY, mo.hb.cX, mo.hb.cY), getEnemies().get(0) == mo ? SludgeBombEffect.DURATION - 0.2f : 0f));
    }

    private static class SludgeBombEffect extends AbstractGameEffect {
        private static float DURATION = 1.2f;
        private static float UPPIES = 300f * Settings.scale;
        private static float SMOKE_GAP = 0.03f;

        private float x, y, startX, startY, endX, endY, smokeTimer;

        public SludgeBombEffect(float startX, float startY, float endX, float endY) {
            duration = DURATION;
            color = Color.WHITE.cpy();
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public void update() {
            float progress = 1f - (duration / DURATION);
            x = startX + (endX - startX) * progress;
            y = startY + (endY - startY) * progress + (float)Math.sin(Math.PI * progress) * UPPIES;
            while (smokeTimer >= SMOKE_GAP) {
                smokeTimer -= SMOKE_GAP;
                AbstractDungeon.effectsQueue.add(new SludgeEffect(x, y));
            }
            smokeTimer += Gdx.graphics.getDeltaTime();
            if (progress < 0.1f)
                color.a = progress / 0.1f;
            else if (progress > 0.9f)
                color.a = 1 - (progress - 0.9f) / 0.1f;
            else
                color.a = 1f;
            duration -= Gdx.graphics.getDeltaTime();
            if (duration <= 0f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}

        private static class SludgeEffect extends AbstractGameEffect {
            private static final float DURATION = 0.6f;
            private static final float OPACITY = 0.8f;
            private static final float FINAL_SCALE = 0.3f;
            private float x, y;
            private float rotationalVel = MathUtils.random(-50f, 50f);
            private float startScale;
            private boolean shook = false;
            private TextureAtlas.AtlasRegion img;

            public SludgeEffect(float x, float y) {
                this.x = x;
                this.y = y;
                duration = DURATION;
                rotation = MathUtils.random(0f, 360f);
                scale *= MathUtils.random(0.5f, 1.5f);
                startScale = scale;
                color = new Color(MathUtils.random(0.1f, 0.2f), 0f, MathUtils.random(0.2f, 0.4f), OPACITY);
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