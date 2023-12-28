package bogwarden.cards;

import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class Blunderbuss extends AbstractBogCard {
    public final static String ID = makeID("Blunderbuss");

    public Blunderbuss() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(12);
        setMagic(3, +2);
    }
  
    public int getDamage() {
        return (int)adp().hand.group.stream().filter(c -> c != Blunderbuss.this && (c.retain || c.selfRetain)).count() * magicNumber;
    }
  
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = baseDamage;
        baseDamage += getDamage();
        super.calculateCardDamage(mo);
        baseDamage = realBase;
        isDamageModified = damage != realBase;
    }
    
    public void applyPowers() {
        int realBase = baseDamage;
        baseDamage += getDamage();
        super.applyPowers();
        baseDamage = realBase;
        isDamageModified = damage != realBase;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        vfx(new BlunderbussEffect(m), BlunderbussEffect.DURATION / 2f - 0.05f);
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        for (int i = 0; i < 3; i++) {
            atb(new SFXAction("BLUNT_FAST", 0.02f));
            vfx(new FlashAtkImgEffect(m.hb.cX + MathUtils.random(-100f, 100f) * Settings.scale, m.hb.cY + MathUtils.random(-100f, 100f) * Settings.scale, AbstractGameAction.AttackEffect.BLUNT_LIGHT, true), 0.02f);
        }
    }

    private static class BlunderbussEffect extends AbstractGameEffect {
        private static float DURATION = 0.8f;
        private static float JUMP_HEIGHT = 250f * Settings.scale;

        private boolean fired;
        private AbstractMonster target;

        public BlunderbussEffect(AbstractMonster target) {
            this.target = target;
            duration = DURATION;
        }

        public void update() {
            duration -= Gdx.graphics.getDeltaTime();
            float progress = 1f - duration / DURATION;
            if (duration <= 0f) {
                adp().animY = 0f;
                isDone = true;
                return;
            }
            adp().animY = (float)Math.sin(progress * Math.PI) * JUMP_HEIGHT;
            if (progress >= 0.5f && !fired) {
                fired = true;
                CardCrawlGame.sound.play(BogAudio.SHOTGUN);
                float x = adp().hb.x + adp().hb.width;
                float y = adp().animY + adp().drawY;
                if (target != null)
                    for (int i = 0; i < 10; i++)
                        AbstractDungeon.effectsQueue.add(new ShotgunPelletEffect(x, y, target.hb.cY));
                for (int i = 0; i < 15; i++)
                    AbstractDungeon.effectsQueue.add(new ShotgunSmokeEffect(x, y));
            }
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}
        

        private static class ShotgunPelletEffect extends AbstractGameEffect {
            private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/shotgunpellet.png")), 0, 0, 64, 64);
            private static final float DURATION = 0.2f;

            private float x, y, startX, startY, endX, endY;

            public ShotgunPelletEffect(float startX, float startY, float endY) {
                duration = DURATION;
                color = Color.WHITE.cpy();
                this.startX = startX;
                this.startY = startY;
                x = startX;
                y = startY;
                this.endX = Settings.WIDTH + MathUtils.random(128f, 500f) * Settings.scale;
                this.endY = endY + MathUtils.random(-400f, 400f) * Settings.scale;
            }

            public void update() {
                duration -= Gdx.graphics.getDeltaTime();
                float progress = 1f - duration / DURATION;
                x = startX + (endX - startX) * progress;
                y = startY + (endY - startY) * progress;
                if (duration <= 0f)
                    isDone = true;
            }

            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, x - 32f, y - 32f, 64f * scale, 64f * scale);
            }

            public void dispose() {}
        }

        private static class ShotgunSmokeEffect extends AbstractGameEffect {
            private static final float DURATION = 0.45f;
            private static final float OPACITY = 0.9f;
            private static final float FINAL_SCALE = 0.3f;
            private float x, y;
            private float xVel = MathUtils.random(0f, 200f) * Settings.scale;
            private float yVel = MathUtils.random(-100f, 100f) * Settings.scale;
            private float rotationalVel = MathUtils.random(-50f, 50f);
            private float startScale;
            private boolean shook = false;
            private TextureAtlas.AtlasRegion img;

            public ShotgunSmokeEffect(float x, float y) {
                this.x = x;
                this.y = y;
                duration = DURATION;
                rotation = MathUtils.random(0f, 360f);
                scale *= MathUtils.random(0.75f, 1.25f);
                startScale = scale;
                color = new Color(0.95f, 0.95f, 0.95f, OPACITY);
                
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
                x += xVel * Gdx.graphics.getDeltaTime();
                y += yVel * Gdx.graphics.getDeltaTime();
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