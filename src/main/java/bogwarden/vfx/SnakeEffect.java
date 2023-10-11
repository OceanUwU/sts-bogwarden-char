package bogwarden.vfx;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeImagePath;

public class SnakeEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/snake.png")), 0, 0, 256, 256);
    public static final float DURATION = 0.6f;
    private static final float WAVE_HEIGHT = 20f * Settings.scale;
    private static final int SWISHES = 3;

    private float x, y;
    private float startX, startY, endX, endY;

    public SnakeEffect(float startX, float startY, float endX, float endY) {
        duration = DURATION;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        color = Color.WHITE.cpy();
    }

    public void update() {
        float progress = 1f - (duration / DURATION);
        float lastX = x;
        float lastY = y;
        x = startX + (endX - startX) * progress;
        y = startY + (endY - startY) * progress + (float)Math.sin(Math.PI * progress * SWISHES) * WAVE_HEIGHT;
        rotation = -(float)Math.toDegrees(Math.atan2(x - lastX, y - lastY)) + 90f;
        duration -= Gdx.graphics.getDeltaTime();
        if (progress < 0.1f)
            color.a = progress / 0.1f;
        else if (progress > 0.9f)
            color.a = 1 - (progress - 0.9f) / 0.1f;
        else
            color.a = 1f;
        if (duration <= 0f)
            isDone = true;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(IMG, x - 128f, y - 128f, 128, 128, 256, 256, scale, scale, rotation);
    }

    public void dispose() {}
}