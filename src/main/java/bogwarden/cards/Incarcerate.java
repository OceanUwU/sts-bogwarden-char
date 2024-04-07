package bogwarden.cards;

import bogwarden.actions.EasyXCostAction;
import bogwarden.actions.TriggerTrapAction;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class Incarcerate extends AbstractBogCard {
    public final static String ID = makeID("Incarcerate");

    public Incarcerate() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(5);
        setMagic(0, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            if (effect + params[0] > 0)
                att(new TriggerTrapAction(effect + params[0]));
            for (int i = 0; i < effect; i++) {
                dmgTop(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                vfxTop(new SpearStabEffect(m.hb.cX, m.hb.cY));
            }
            return true;
        }, magicNumber));
    }

    private static class SpearStabEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/spear.png")), 0, 0, 438, 82);
        private static final float DURATION = 0.6f;
        private static final float START_DIST = 140f * Settings.scale;
        private static final float END_DIST = 20f * Settings.scale;

        private float x, y, sX, sY, distance;
        private float rotationalVel = MathUtils.random(40f, 120f);

        public SpearStabEffect(float sX, float sY) {
            color = Color.WHITE.cpy();
            this.sX = sX;
            this.sY = sY;
            scale *= 0.6f;
            rotation = MathUtils.random(0f, 360f);
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            rotation += Gdx.graphics.getDeltaTime() * rotationalVel;
            float progress = duration / DURATION;
            if (progress < 0.2f)
                color.a = progress / 0.2f;
            else if (progress > 0.8f)
                color.a = 1 - (progress - 0.8f) / 0.2f;
            else
                color.a = 1f;
            distance = IMG.packedWidth * scale + START_DIST + (END_DIST - START_DIST) * (1f - (float)Math.pow(2f * progress - 1f, 2f));
            x = sX + MathUtils.cosDeg(rotation + 180f) * distance;
            y = sY + MathUtils.sinDeg(rotation + 180f) * distance;
            if (progress > 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(IMG, x - 219f, y - 41f, 219, 41, 438, 82, scale, scale, rotation);
        }

        public void dispose() {}
    }
}