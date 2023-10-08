package bogwarden.cards;

import bogwarden.powers.Maledict;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class VilePowder extends AbstractBogCard {
    public final static String ID = makeID("VilePowder");

    public VilePowder() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL);
        setBlock(5, +3);
        setMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        ArrayList<AbstractMonster> enemies = getEnemies();
        if (enemies.size() > 0)
            vfx(new VilePowderEffect(p.hb, enemies.get(0).hb, enemies.get(enemies.size() - 1).hb), 0.25f);
        forAllMonstersLiving(mo -> applyToEnemy(mo, new Maledict(mo, magicNumber)));
    }

    public static class VilePowderEffect extends AbstractGameEffect {
        private ArrayList<VileParticle> particles = new ArrayList<>();
        
        public VilePowderEffect(Hitbox origin, Hitbox start, Hitbox end) {
            for (int i = 0; i < 50; i++)
                particles.add(new VileParticle(origin.cX, origin.cY, MathUtils.random(start.x, end.x + end.width), MathUtils.random(start.y, end.y + end.height)));
            isDone = true;
        }
        
        public void update() {
            AbstractDungeon.effectsQueue.addAll(particles);
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}

        public static class VileParticle extends AbstractGameEffect {
            private static final Texture TEXTURE = TexLoader.getTexture(makeImagePath("vfx/vileparticle.png"));
            private static final float SIZE = TEXTURE.getWidth();
            private static final float HALFSIZE = TEXTURE.getWidth() / 2f;
            private static final float FLY_TIME_PERCENTAGE = 0.25f;
            private static final float FLY_X_PERCENTAGE = 0.85f;
            private int flickers = MathUtils.random(2, 7);
            private float flyHeight = MathUtils.random(50f, 200f) * Settings.scale;
            private float timer;
            private float startX, startY, endX, endY, x, y;
            private float targetScale;

            public VileParticle(float startX, float startY, float endX, float endY) {
                float r = MathUtils.random(0.68f, 1.0f);
                color = new Color(r, 3 * r - 2.0f, 1.0f, 0f);
                targetScale = scale * MathUtils.random(0.4f, 1f);
                duration = MathUtils.random(2.0f, 4.5f);
                this.startX = startX;
                this.startY = startY;
                this.endX = endX;
                this.endY = endY;
            }
        
            public void update() {
                timer += Gdx.graphics.getDeltaTime();
                float progress = timer / duration;
                color.a = 0.25f + Math.abs((float)Math.sin((progress * flickers) * Math.PI * 2) * 0.5f);
                if (progress < FLY_TIME_PERCENTAGE) {
                    progress /= FLY_TIME_PERCENTAGE;
                    scale = targetScale * progress;
                    x = startX + (endX - startX) * FLY_X_PERCENTAGE * (float)Math.sin(progress * Math.PI / 2f);
                    y = startY + flyHeight * progress;
                } else if (progress >= 1f)
                    isDone = true;
                else {
                    progress = (progress - FLY_TIME_PERCENTAGE) / (1f - FLY_TIME_PERCENTAGE); 
                    scale = targetScale * (1f - progress);
                    x = startX + (endX - startX) * (FLY_X_PERCENTAGE + (1f - FLY_X_PERCENTAGE) * progress);
                    y = startY + flyHeight - (startY + flyHeight - endY) * progress;
                }
            }

            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(TEXTURE, x - HALFSIZE * scale, y - HALFSIZE * scale, SIZE * scale, SIZE * scale);
            }

            public void dispose() {}
        }
    }
}