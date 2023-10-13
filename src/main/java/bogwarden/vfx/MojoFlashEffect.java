package bogwarden.vfx;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeImagePath;

public class MojoFlashEffect extends AbstractGameEffect {
    private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/mojoflash1.png")), 0, 0, 400, 400);
    private static TextureAtlas.AtlasRegion IMG2 = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/mojoflash2.png")), 0, 0, 400, 400);
    private static final float ROTATIONAL_VEL = 60f;
    private static final float DURATION = 1.2f;
    private static final float SWITCH_INTERVAL = 0.4f;

    private float x, y;
    private float switchTimer = SWITCH_INTERVAL;
    private boolean flashing;

    public MojoFlashEffect(float x, float y) {
        duration = DURATION;
        color = Color.WHITE.cpy();
        this.x = x;
        this.y = y;
        renderBehind = true;
        scale *= 0.75f;
    }

    public void update() {
        rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
        float progress = 1f - (duration / DURATION);
        if (progress < 0.25f)
            color.a = progress / 0.25f;
        else if (progress > 0.75f)
            color.a = 1 - (progress - 0.75f) / 0.25f;
        else
            color.a = 1f;
        if (switchTimer <= 0f) {
            flashing = !flashing;
            switchTimer += SWITCH_INTERVAL;
        }
        switchTimer -= Gdx.graphics.getDeltaTime();
        duration -= Gdx.graphics.getDeltaTime();
        if (duration <= 0f)
            isDone = true;
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        sb.draw(flashing ? IMG2 : IMG, x - 200f, y - 200f, 200, 200, 400, 400, scale, scale, rotation);
    }

    public void dispose() {}
}