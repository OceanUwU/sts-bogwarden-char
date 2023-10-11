package bogwarden.vfx;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeImagePath;

public class SparkleHelixEffect extends AbstractGameEffect {
    private static final float ROTATIONAL_VEL = 580f;
    public static final float DURATION = 0.5f;
    private static final float WAVE_HEIGHT = 100f * Settings.scale;
    private static final int SWISHES = 4;
    private static final float SPARKLE_INTERVAL = 0.02f;

    private float x, y;
    private float startX, startY, endX, endY, sparkleTimer;

    public SparkleHelixEffect(float startX, float startY, float endX, float endY) {
        duration = DURATION;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        sparkleTimer = 0f;
    }

    public void update() {
        rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
        float progress = 1f - (duration / DURATION);
        x = startX + (endX - startX) * progress;
        y = startY + (endY - startY) * progress;
        while (sparkleTimer >= SPARKLE_INTERVAL) {
            sparkleTimer -= SPARKLE_INTERVAL;
            float yOffset = (float)Math.cos(Math.PI * progress * SWISHES) * WAVE_HEIGHT;
            AbstractDungeon.effectsQueue.add(new SparkleParticle(x, y + yOffset, new Color(MathUtils.random(0.1f, 0.3f), MathUtils.random(0.8f, 1f), 0f, 1f)));
            AbstractDungeon.effectsQueue.add(new SparkleParticle(x, y - yOffset, new Color(MathUtils.random(0.9f, 1f), MathUtils.random(0f, 0.1f), MathUtils.random(0.7f, 1f), 1f)));
        }
        sparkleTimer += Gdx.graphics.getDeltaTime();
        duration -= Gdx.graphics.getDeltaTime();
        if (duration <= 0f)
            isDone = true;
    }

    public void render(SpriteBatch sb) {}
    public void dispose() {}

    public static class SparkleParticle extends AbstractGameEffect {
        private static final Texture TEXTURE = TexLoader.getTexture(makeImagePath("vfx/vileparticle.png"));
        private static final float SIZE = TEXTURE.getWidth();
        private static final float HALFSIZE = TEXTURE.getWidth() / 2f;
        private int flickers = MathUtils.random(2, 7);
        private float timer, x, y, origScale;
        private float dX = MathUtils.random(-40f, 40f) * Settings.scale;
        private float dY = MathUtils.random(-40f, 40f) * Settings.scale;

        public SparkleParticle(float x, float y, Color color) {
            this.color = color;
            origScale = scale * MathUtils.random(1.2f, 1.8f);
            duration = MathUtils.random(0.3f, 0.5f);
            this.x = x;
            this.y = y;
        }
    
        public void update() {
            timer += Gdx.graphics.getDeltaTime();
            float progress = timer / duration;
            color.a = 0.25f + Math.abs((float)Math.sin((progress * flickers) * Math.PI * 2) * 0.5f);
            scale = origScale * (1f - progress);
            x += Gdx.graphics.getDeltaTime() * dX;
            y += Gdx.graphics.getDeltaTime() * dY;
            if (progress >= 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(TEXTURE, x - HALFSIZE * scale, y - HALFSIZE * scale, SIZE * scale, SIZE * scale);
        }

        public void dispose() {}
    }
}