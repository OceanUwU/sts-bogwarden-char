package bogwarden.cards;

import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class BoulderTrap extends AbstractTrapCard {
    public final static String ID = makeID("BoulderTrap");

    public BoulderTrap() {
        super(ID, CardRarity.RARE);
        setDamage(25, +7);
        setSecondMagic(1);
        isMultiDamage = true;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        if (magicNumber >= secondMagic) {
            allDmgTop(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
            forAllMonstersLiving(mo -> att(new VFXAction(new BoulderEffect(mo), getEnemies().get(0) == mo ? 0.5f : 0f)));
        }
        magicNumber = ++baseMagicNumber;
    }

    public void applyPowers() {
        super.applyPowers();
        magicNumber = baseMagicNumber = Math.max(baseMagicNumber, 0);
    }

    public static class BoulderEffect extends AbstractGameEffect {
        private static final TextureRegion TEX = new TextureRegion(TexLoader.getTexture(makeImagePath("vfx/wreckboulder.png")));
        private static final int W = TEX.getTexture().getWidth();
        private static final int H = TEX.getTexture().getHeight();
        private static final float ACCELERATION = -500f;
        private static final float FADE_RATE = 1.4f;
        private static final float ROTATION_RATE = 300f;

        private float x, y, targetY, yVel, xVel, imgScale;
        private boolean fallen = false;

        public BoulderEffect(AbstractCreature target) {
            super();
            color = Color.WHITE.cpy();
            rotation = MathUtils.random(0f, 360f);
            yVel = -600f * scale;
            y = Settings.HEIGHT;
            targetY = target.hb.cY;
            renderBehind = false;
            x = target.hb.cX;
            xVel = MathUtils.random(100f, 200f) * scale;
            imgScale = MathUtils.random(0.4f, 0.6f);
            if (MathUtils.randomBoolean()) xVel *= -1;
        }

        public void update() {
            y += yVel * Gdx.graphics.getDeltaTime();
            yVel += ACCELERATION * Gdx.graphics.getDeltaTime() * scale;
            if (fallen) {
                x += xVel * Gdx.graphics.getDeltaTime();
                rotation += ROTATION_RATE * Gdx.graphics.getDeltaTime();
                color.a -= FADE_RATE * Gdx.graphics.getDeltaTime();
                isDone = color.a <= 0f;
            } else if (y <= targetY) {
                y = targetY;
                yVel = MathUtils.random(150f, 350f) * scale;
                fallen = true;
                CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.HIGH, ScreenShake.ShakeDur.MED, true); 
            }
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(TEX, x - W / 2f, y - H /2f, (float)W / 2f, (float)H / 2f, W, H, scale * imgScale, scale * imgScale, rotation);
        }

        public void dispose() {}
    }
}