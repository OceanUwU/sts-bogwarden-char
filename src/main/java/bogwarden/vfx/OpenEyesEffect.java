package bogwarden.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.Arrays;
import java.util.List;

public class OpenEyesEffect extends AbstractGameEffect {
    private static final float DURATION = 1.4f;
    private static final float IMG_SCALE = 8.0f;
    public static final float GAP = 260f * Settings.scale;
    private static final float OPEN_POINT = 0.15f;
    private static final float OPEN_TIME = 0.1f;
    private static List<TextureAtlas.AtlasRegion> EYE_ANIMS = Arrays.asList(ImageMaster.EYE_ANIM_1, ImageMaster.EYE_ANIM_2, ImageMaster.EYE_ANIM_3, ImageMaster.EYE_ANIM_4, ImageMaster.EYE_ANIM_5);

    private TextureAtlas.AtlasRegion img;
    public boolean canGoPastHalf;
    private boolean shake, singleEye;

    public OpenEyesEffect(Color color, boolean singleEye, boolean shake, boolean canGoPastHalf, float scaleModifier) {
        this.color = color.cpy();
        scale *= IMG_SCALE * scaleModifier;
        this.shake = shake;
        this.canGoPastHalf = canGoPastHalf;
        this.singleEye = singleEye;
    }

    public void update() {
        if (duration < 0.5f || canGoPastHalf)
            duration += Gdx.graphics.getDeltaTime();
        float progress = duration / DURATION;
        if (progress >= 1f)
            isDone = true;
        else if (progress < 0.1f)
            color.a = progress / 0.1f;
        else if (progress > 1f - 0.1f)
            color.a = (1f - progress) / 0.1f;
        else
            color.a = 1f;
        if (progress < OPEN_POINT || progress > 1f - OPEN_POINT)
            img = ImageMaster.EYE_ANIM_0;
        else if (progress >= OPEN_POINT + OPEN_TIME && progress <= 1f - OPEN_POINT - OPEN_TIME)
            img = ImageMaster.EYE_ANIM_6;
        else if (progress >= OPEN_POINT && progress < OPEN_POINT + OPEN_TIME)
            setImg((progress - OPEN_POINT) / OPEN_TIME);
        else if (progress >= 1f - OPEN_POINT - OPEN_TIME && progress < 1f - OPEN_POINT)
            setImg((1f - progress - OPEN_POINT) / OPEN_TIME);
    }

    public void setImg(float animProgress) {
        img = EYE_ANIMS.get(Math.min((int)Math.floor(animProgress * EYE_ANIMS.size()), 4));
    }

    public void render(SpriteBatch sb) {
        sb.setColor(color);
        if (singleEye)
            renderEye(sb, 0);
        else for (int i = 0; i < 2; i++)
            renderEye(sb, i*2-1);
    }

    public void renderEye(SpriteBatch sb, float offset) {
        sb.draw(img, Settings.WIDTH / 2f + GAP * offset + (shake ? MathUtils.random(-4f, 4f) * Settings.scale : 0f), Settings.HEIGHT / 2f + (shake ? MathUtils.random(-4f, 4f) * Settings.scale : 0f), img.packedWidth / 2f, img.packedHeight / 2f, img.packedWidth, img.packedHeight, scale, scale, rotation);
    }

    public void dispose() {}
}