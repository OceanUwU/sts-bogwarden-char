package bogwarden.vfx;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.ArrayList;

import static bogwarden.BogMod.makeImagePath;

public class BogwardenVictoryEffect extends AbstractGameEffect {
    private static final float LEAF_INTERVAL = 0.5f;

    private ArrayList<ClimbingVine> vines = new ArrayList<>();
    private ArrayList<FallingLeaf> leaves = new ArrayList<>();
    private float leafTimer;
    private float timer;

    public BogwardenVictoryEffect() {
        for (float x = 0; x < Settings.WIDTH / 2f + 250f * Settings.scale; x += 270f * Settings.scale) {
            vines.add(new ClimbingVine(Settings.WIDTH / 2f + x));
            if (x > 0)
                vines.add(new ClimbingVine(Settings.WIDTH / 2f - x));
        }
    }

    public void update() {
        float delta = Gdx.graphics.getDeltaTime();

        timer += delta;
        leafTimer -= delta;
        if (leafTimer <= 0f) {
            leafTimer += LEAF_INTERVAL;
            leaves.add(new FallingLeaf());
        }
        for (FallingLeaf leaf : leaves)
            leaf.update(delta);
        leaves.removeIf(leaf -> leaf.y < -200f);

        for (ClimbingVine vine : vines)
            vine.update(delta);
    }

    public void render(SpriteBatch sb) {
        sb.setColor(new Color(1f, 1f, 1f, Math.min(timer, 1f)));
        sb.setBlendFunction(770, 771);
        for (FallingLeaf leaf : leaves)
            leaf.render(sb);
        for (ClimbingVine vine : vines)
            vine.render(sb);
        sb.setBlendFunction(770, 1);
    }

    public void dispose() {};

    private static class ClimbingVine {
        private static final Texture IMG = TexLoader.getTexture(makeImagePath("vfx/vine.png"));
        private static final int W = IMG.getWidth();
        private static final int H = IMG.getHeight();
        private static final float FLOWER_CUTOFF = 250f * Settings.scale;

        private ArrayList<BloomingFlower> flowers = new ArrayList<>();
        private float x;
        private float y = MathUtils.random(500f);
        public float climbSpeed = MathUtils.random(25f, 150f) * Settings.scale;

        public ClimbingVine(float x) {
            this.x = x;
            int i = MathUtils.random(0, 1);
            for (float y = 0f; y < Settings.HEIGHT + FLOWER_CUTOFF * 2; y += MathUtils.random(50f, 200f) * Settings.scale)
                flowers.add(new BloomingFlower(this, x, y, i++ % 2 == 0));
        }

        public void update(float delta) {
            y = (y + climbSpeed * delta) % (H * Settings.scale);
            for (BloomingFlower flower : flowers)
                flower.update(delta);
        }

        public void render(SpriteBatch sb) {
            for (float drawY = y - H * Settings.scale; drawY < Settings.HEIGHT + H * Settings.scale; drawY += H * Settings.scale)
                sb.draw(IMG, x - W * Settings.scale / 2f, drawY, W * Settings.scale, H * Settings.scale);
            for (BloomingFlower flower : flowers)
                flower.render(sb);
        }

        private static class BloomingFlower {
            private static final Texture TEXTURE = TexLoader.getTexture(makeImagePath("vfx/flower.png"));
            private static final int W = TEXTURE.getWidth();
            private static final int H = TEXTURE.getHeight();
            private static final float BLOOM_TIME = 0.8f;
            private static ArrayList<TextureAtlas.AtlasRegion> frames;
            private static int numFrames;

            private ClimbingVine vine;
            private float x, y, bloomTimer;
            private float rotation = MathUtils.random(-10f, 10f);
            private float bloomHeight = Settings.HEIGHT / 2f - MathUtils.random(0f, 50f) * Settings.scale;
            private int frame;

            public BloomingFlower(ClimbingVine vine, float oX, float y, boolean flipped) {
                if (frames == null)
                    loadFrames();
                this.vine = vine;
                this.y = y;
                x = oX + ClimbingVine.W * Settings.scale * (flipped ? -1 : 1) / 5f;
                if (flipped)
                    rotation += 90f; //find a better way to flip a texture horizontally?
                if (y > bloomHeight)
                    bloomTimer = BLOOM_TIME;
            }

            private static void loadFrames() {
                frames = new ArrayList<>();
                for (int x = 0; x < W; x += H)
                    frames.add(new TextureAtlas.AtlasRegion(TEXTURE, x, 0, H, H));
                numFrames = frames.size();
            }

            public void update(float delta) {
                y += vine.climbSpeed * delta;
                if (y > Settings.HEIGHT + FLOWER_CUTOFF) {
                    y = -FLOWER_CUTOFF;
                    bloomTimer = 0f;
                    frame = 0;
                } else if (y > bloomHeight) {
                    bloomTimer += delta;
                    if (bloomTimer < BLOOM_TIME)
                        frame = (int)(bloomTimer / BLOOM_TIME * numFrames);
                    else
                        frame = numFrames - 1;
                }
            }

            public void render(SpriteBatch sb) {
                sb.draw(frames.get(frame), x - H/2, y - H/2, H/2, H/2, H, H, Settings.scale, Settings.scale, rotation);
            }
        }
    }

    private static class FallingLeaf {
        private static final int W = 75;
        private static final int H = 75;
        private static final TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/leaf.png")), 0, 0, W, H);
        private static final float START_Y = Settings.HEIGHT + 100f * Settings.scale;
        private static final float MOVEMENT_SCALE = 400f * Settings.scale;
        private static final float TIME_SCALE = 0.35f;
        private static final float ROTATION_SCALE = 30f;

        public float y;
        private float oX = MathUtils.random(0f, Settings.WIDTH);
        private float x, rotation, timer;

        public void update(float delta) {
            timer += delta;
            x = oX + (float)Math.sin(timer * TIME_SCALE * Math.PI) * MOVEMENT_SCALE;
            y = START_Y + (0.6f * (float)Math.pow(Math.sin(timer * TIME_SCALE * Math.PI), 2) - 0.5f * timer * TIME_SCALE) * MOVEMENT_SCALE;
            rotation = (float)Math.sin(timer * TIME_SCALE * Math.PI) * ROTATION_SCALE;
        }

        public void render(SpriteBatch sb) {
            sb.draw(IMG, x - W/2, y - H/2, W/2, H/2, W, H, Settings.scale, Settings.scale, rotation);
        }
    }
}