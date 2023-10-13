package bogwarden.cards;

import bogwarden.powers.Maledict;
import bogwarden.powers.Venom;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class SpitefulStaff extends AbstractBogCard {
    public final static String ID = makeID("SpitefulStaff");

    public SpitefulStaff() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(13);
        setMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new SpiteEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), SpiteEffect.DURATION - 0.2f);
        dmg(m, BLAST_EFFECT);
        applyToEnemy(m, new Maledict(m, magicNumber));
        applyToEnemy(m, new Venom(m, magicNumber));
    }

    private static class SpiteEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/spite.png")), 0, 0, 304, 304);
        private static final float DURATION = 0.5f;

        private float x, y;
        private float startX, startY, endX, endY;

        public SpiteEffect(float startX, float startY, float endX, float endY) {
            duration = DURATION;
            color = Color.WHITE.cpy();
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            rotation = -(float)Math.toDegrees(Math.atan2(endX - startX, endY - startY)) + 90f;
        }

        public void update() {
            if (duration == DURATION)
                CardCrawlGame.sound.play("NECRONOMICON");
            float progress = 1f - (duration / DURATION);
            x = startX + (endX - startX) * progress;
            y = startY + (endY - startY) * progress;
            if (progress < 0.2f)
                color.a = progress / 0.2f;
            else if (progress > 0.8f)
                color.a = 1 - (progress - 0.8f) / 0.2f;
            else
                color.a = 1f;
            duration -= Gdx.graphics.getDeltaTime();
            if (duration <= 0f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(IMG, x - 152f, y - 152f, 152, 152, 304, 304, scale, scale, rotation);
        }

        public void dispose() {}
    }
}