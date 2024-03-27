package bogwarden.vfx;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeImagePath;

public class LassoEffect extends AbstractGameEffect {
    public static final float DURATION = 0.5f;

    private float x, y, stretch, length;
    private boolean sounded = false;
    private TextureAtlas.AtlasRegion img;

    public LassoEffect(float startX, float startY, float endX, float endY, Color color, String texPath) {
        duration = DURATION;
        this.color = color.cpy();
        x = startX;
        y = startY;
        rotation = -(float)Math.toDegrees(Math.atan2(endX - startX, endY - startY)) + 90f;
        length = (float)Math.sqrt(Math.pow(endX - startX, 2) + Math.pow(endY - startY, 2)) / 800f;
        img = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath(texPath)), 0, 0, 800, 100);
    }

    public void update() {
        float progress = 1f - (duration / DURATION);
        stretch = Math.min(progress / 0.6f, 1f);
        if (progress > 0.6f) {
            color.a = 1 - (progress - 0.6f) / 0.4f;
            if (!sounded) {
                CardCrawlGame.sound.play("POWER_ENTANGLED");
                sounded = true;
            }
        }
        duration -= Gdx.graphics.getDeltaTime();
        if (duration <= 0f)
            isDone = true;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(img, x, y - 50f, 0, 50, 800, 100, length * stretch, scale, rotation);
    }

    public void dispose() {}
}