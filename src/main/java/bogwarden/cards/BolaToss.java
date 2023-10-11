package bogwarden.cards;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class BolaToss extends AbstractBogCard {
    public final static String ID = makeID("BolaToss");

    public BolaToss() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(2, +1);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new BolaTossEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), BolaTossEffect.DURATION - 0.2f);
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        applyToEnemy(m, new ConstrictedPower(m, p, magicNumber));
    }

    private static class BolaTossEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/bola.png")), 0, 0, 84, 84);
        private static final float ROTATIONAL_VEL = 580f;
        private static final float DURATION = 0.8f;
        private static final float UPPIES = 200f * Settings.scale;

        private float x, y;
        private float startX, startY, endX, endY;

        public BolaTossEffect(float startX, float startY, float endX, float endY) {
            duration = DURATION;
            color = Color.WHITE.cpy();
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

        public void update() {
            rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
            float progress = 1f - (duration / DURATION);
            x = startX + (endX - startX) * progress;
            y = startY + (endY - startY) * progress + (float)Math.sin(Math.PI * progress) * UPPIES;
            if (progress < 0.1f)
                color.a = progress / 0.1f;
            else if (progress > 0.9f)
                color.a = 1 - (progress - 0.9f) / 0.1f;
            else
                color.a = 1f;
            duration -= Gdx.graphics.getDeltaTime();
            if (duration <= 0f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(IMG, x - 42f, y - 42f, 42, 42, 84, 84, scale, scale, rotation);
        }

        public void dispose() {}
    }
}