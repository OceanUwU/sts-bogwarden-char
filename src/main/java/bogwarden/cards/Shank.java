package bogwarden.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

import bogwarden.util.TexLoader;

public class Shank extends AbstractBogCard {
    public final static String ID = makeID("Shank");

    public Shank() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(12, +5);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new ShankEffect(m.hb.cX, m.hb.cY));
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                if (p.hand.size() > 0)
                    att(new DiscardSpecificCardAction(p.hand.getTopCard()));
            }
        });
    }

    private static class ShankEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/shank.png")), 0, 0, 284, 284);
        private static final float DURATION = 0.4f;
        private static final float DISTANCE = 320f * Settings.scale;

        private float x, y, sX, sY;

        public ShankEffect(float sX, float sY) {
            color = Color.WHITE.cpy();
            this.sX = sX;
            this.sY = sY;
            scale *= 0.7f;
            updatePos(0f);
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            float progress = duration / DURATION;
            float xBefore = x, yBefore = y;
            updatePos(progress);
            rotation = (float)Math.atan2(x - xBefore, y - yBefore) * 360f / ((float)Math.PI * 2f) + 180f;
            if (progress < 0.1f)
                color.a = progress / 0.1f;
            else if (progress > 0.7f)
                color.a = 1 - (progress - 0.7f) / 0.3f;
            else
                color.a = 1f;
            if (progress >= 1f)
                isDone = true;
        }

        public void updatePos(float progress) {
            x = sX + ((float)Math.sin(0.5f * progress * Math.PI) - 0.5f) * DISTANCE;
            y = sY + (1f - (float)Math.pow(progress, 0.6f) - 0.5f) * DISTANCE;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(IMG, x - 142f, y - 142f, 142, 142, 284, 284, scale, scale, rotation);
        }

        public void dispose() {}
    }
}