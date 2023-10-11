package bogwarden.cards;

import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class PlagueOfToads extends AbstractBogCard {
    public final static String ID = makeID("PlagueOfToads");

    public PlagueOfToads() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        setDamage(2);
        setMagic(4, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            vfx(new ToadEffect(p.hb.cX, p.hb.cY, Settings.PLAYTESTER_ART_MODE || UnlockTracker.betaCardPref.getBoolean(cardID, false), i == 0), i == magicNumber - 1 ? 0.1f : MathUtils.random(0.04f, 0.08f));
        for (int i = 0; i < magicNumber; i++)
            dmgRandom(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    private static class ToadEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/toad.png")), 0, 0, 128, 128);
        private static TextureAtlas.AtlasRegion BETA_IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/realtoad.png")), 0, 0, 128, 128);
        private static final float DURATION = 1.2f;

        private float x, y, startX, startY, endX;
        private float uppies = MathUtils.random(50f, 350f) * Settings.scale;
        private boolean beta, playSound;

        public ToadEffect(float startX, float startY, boolean beta, boolean playSound) {
            duration = DURATION;
            color = Color.WHITE.cpy();
            this.startX = startX;
            this.startY = startY;
            x = startX;
            y = startY;
            this.endX = Settings.WIDTH + MathUtils.random(128f, 500f) * Settings.scale;
            this.beta = beta;
            this.playSound = playSound;
            if (beta)
                scale *= MathUtils.random(0.8f, 1.2f);
            else
                scale *= MathUtils.random(0.4f, 0.7f);
        }

        public void update() {
            if (duration == DURATION && beta)
                CardCrawlGame.sound.play(BogAudio.TOAD, 0.05f);
            else if (duration == DURATION && playSound)
                CardCrawlGame.sound.play(BogAudio.FROGS);
            duration -= Gdx.graphics.getDeltaTime();
            float progress = 1f - (duration / DURATION);
            float lastX = x;
            float lastY = y;
            x = startX + (endX - startX) * progress;
            y = startY + progress + (float)Math.sin(Math.PI * progress) * uppies;
            rotation = -(float)Math.toDegrees(Math.atan2(x - lastX, y - lastY)) + 90f;
            if (progress < 0.1f)
                color.a = progress / 0.1f;
            else
                color.a = 1f;
            if (duration <= 0f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(beta ? BETA_IMG : IMG, x - 64f, y - 64f, 64, 64, 128, 128, scale, scale, rotation);
        }

        public void dispose() {}
    }
}