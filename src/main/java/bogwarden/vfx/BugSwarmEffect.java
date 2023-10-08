package bogwarden.vfx;

import bogwarden.util.BogAudio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class BugSwarmEffect extends AbstractGameEffect {
    private static final float RADIUS = 50f;
    
    private float timer = 0f;
    private int numBugs, released;
    private float x, y;
    private boolean playSound;
    private Texture bugImg;

    public BugSwarmEffect(int numBugs, float x, float y, Texture bugImg, boolean playSound) {
        this.numBugs = numBugs;
        this.playSound = playSound;
        this.x = x;
        this.y = y;
        this.bugImg = bugImg;
        duration = 0.15f;
    }

    public void update() {
        if (timer == 0f && playSound)
            CardCrawlGame.sound.play(BogAudio.BUGS);
        timer += Gdx.graphics.getDeltaTime();
        if (timer >= duration)
            isDone = true;
        timer = Math.min(duration, timer);
        int toRelease = (int)Math.ceil(timer / duration * numBugs) - released;
        for (int i = 0; i < toRelease; i++)
            AbstractDungeon.effectsQueue.add(new BugEffect(x + MathUtils.random(-RADIUS, RADIUS), y + MathUtils.random(-RADIUS, RADIUS), bugImg));
        released += toRelease;
    }

    public void render(SpriteBatch sb) {}
    public void dispose() {}

    public static class BugEffect extends AbstractGameEffect {
        private static final float VELOCITY = 400f * Settings.scale;
        private static final float ROTATIONAL_VEL = 4f;
        private static final float FADE_PERCENTAGE = 0.1f;
        private static final float DURATION = 1.5f;

        private float x, y, speed, size, timer, startRotation, timerOffset;
        private Texture img;

        public BugEffect(float x, float y, Texture img) {
            this.x = x;
            this.y = y;
            this.img = img;
            size = img.getWidth();
            startRotation = MathUtils.random((float)Math.PI * 2);
            timerOffset = MathUtils.random(100f);
            speed = MathUtils.random(0.75f, 1.25f);
            scale *= MathUtils.random(0.75f, 1.25f);
            color = Color.WHITE.cpy();
        }

        public void update() {
            timer += Gdx.graphics.getDeltaTime();
            float progress = timer / DURATION;
            if (timer >= DURATION) {
                isDone = true;
                return;
            } if (progress < FADE_PERCENTAGE)
                color.a = progress / FADE_PERCENTAGE;
            else if (progress > 1f - FADE_PERCENTAGE)
                color.a = (1f - progress) / FADE_PERCENTAGE;
            else
                color.a = 1f;
            float t = timer + timerOffset;
            rotation = startRotation + (float)(Math.sin(t) + Math.cos(t * 2) + Math.sin(t * 3) + Math.sin(t * 4)) / 3f * (float)Math.PI * 2f * ROTATIONAL_VEL;
            x += Math.cos(rotation) * VELOCITY * speed * Gdx.graphics.getDeltaTime();
            y += Math.sin(rotation) * VELOCITY * speed * Gdx.graphics.getDeltaTime();
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(img, x - (size / 2f) * scale, y - (size / 2f) * scale, size * scale, size * scale);
        }

        public void dispose() {}
    }
}