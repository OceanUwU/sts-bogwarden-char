package bogwarden.cards;

import bogwarden.util.BogAudio;
import bogwarden.vfx.SparkleHelixEffect.SparkleParticle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Implode extends AbstractBogCard {
    public final static String ID = makeID("Implode");

    public Implode() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);
        setBlock(12, +2);
        setMagic(1);
        setSecondMagic(14, -4);
        setThirdMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SFXAction(BogAudio.IMPLODE));
        vfx(new ImplodeEffect(p.hb.cX, p.hb.cY));
        blck();
        applyToSelf(new BlurPower(p, magicNumber));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                AbstractMonster mo = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (mo != null) {
                    applyToEnemyTop(mo, new BlurPower(mo, thirdMagic));
                    att(new GainBlockAction(mo, p, secondMagic));
                    vfxTop(new ExplodeEffect(mo.hb.cX, mo.hb.cY));
                }
            } 
        });
    }
    
    public static class ImplodeEffect extends AbstractGameEffect {
        private static final float ROTATIONAL_VEL = 1.38f;
        public static final float DURATION = 0.5f;
        private static final float WAVE_HEIGHT = 100f * Settings.scale;
        private static final int SWISHES = 4;
        private static final float SPARKLE_INTERVAL = 0.05f;
        private static final int STRANDS = 5;
        private static final float RADIUS = 85f * Settings.scale;
        private static final int BEHIND_SPARKLES = 4;
        private static final float BEHIND_SPARKLE_DISTANCE = 6f * Settings.scale;

        private float cX, cY, sparkleTimer;

        public ImplodeEffect(float x, float y) {
            duration = 0f;
            cX = x;
            cY = y;
            rotation = MathUtils.random(0f, (float)Math.PI * 2f);
        }

        public void update() {
            float progress = duration / DURATION;
            while (sparkleTimer >= SPARKLE_INTERVAL) {
                sparkleTimer -= SPARKLE_INTERVAL;
                for (int i = 0; i < STRANDS; i++) {
                    float angle = rotation + ((float)i / (float)STRANDS * (float)Math.PI * 2f);
                    float x = cX + (float)Math.cos(angle) * RADIUS * (1f - progress);
                    float y = cY + (float)Math.sin(angle) * RADIUS * (1f - progress);
                    for (int j = 0; j < BEHIND_SPARKLES; j++) {
                        float angle2 = angle + (j / BEHIND_SPARKLES * (float)Math.PI * 2f);
                        float x2 = x + (float)Math.cos(angle2) * BEHIND_SPARKLE_DISTANCE;
                        float y2 = y + (float)Math.sin(angle2) * BEHIND_SPARKLE_DISTANCE;
                        AbstractDungeon.effectsQueue.add(new SparkleParticle(x2, y2, new Color(MathUtils.random(0.9f, 1f), MathUtils.random(0f, 0.1f), MathUtils.random(0.7f, 1f), 1f)));
                    }
                    AbstractDungeon.effectsQueue.add(new SparkleParticle(x, y, new Color(MathUtils.random(0.1f, 0.3f), MathUtils.random(0.8f, 1f), 0f, 1f)));
                }
                System.out.println("-");
            }
            rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
            sparkleTimer += Gdx.graphics.getDeltaTime();
            duration += Gdx.graphics.getDeltaTime();
            if (progress >= 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}
    }
    
    public static class ExplodeEffect extends AbstractGameEffect {
        private static final float ROTATIONAL_VEL = 1.38f;
        public static final float DURATION = 0.5f;
        private static final float RADIUS = 175f * Settings.scale;
        private static final int DOTS_PER_CIRCLE = 15;
        private static final float CIRCLE_INTERVAL = 0.08f;

        private float cX, cY, sparkleTimer;
        private boolean started = false;

        public ExplodeEffect(float x, float y) {
            duration = 0f;
            cX = x;
            cY = y;
            rotation = MathUtils.random(0f, (float)Math.PI * 2f);
        }

        public void update() {
            float progress = duration / DURATION;
            while (sparkleTimer >= CIRCLE_INTERVAL) {
                sparkleTimer -= CIRCLE_INTERVAL;
                for (int i = 0; i < DOTS_PER_CIRCLE; i++) {
                    float angle = rotation + ((float)i / (float)DOTS_PER_CIRCLE * (float)Math.PI * 2f);
                    float dist = RADIUS * ((float)Math.pow(progress - 1f, 3) + 1f);
                    float x = cX + (float)Math.cos(angle) * dist;
                    float y = cY + (float)Math.sin(angle) * dist;
                    AbstractDungeon.effectsQueue.add(new SparkleParticle(x, y, MathUtils.randomBoolean() ? new Color(MathUtils.random(0.1f, 0.3f), MathUtils.random(0.8f, 1f), 0f, 1f) : new Color(MathUtils.random(0.1f, 0.3f), MathUtils.random(0.8f, 1f), 0f, 1f)));
                }
            }
            rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
            sparkleTimer += Gdx.graphics.getDeltaTime();
            duration += Gdx.graphics.getDeltaTime();
            if (progress >= 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}
    }

    @SpirePatch(clz=MonsterGroup.class, method="applyPreTurnLogic")
    public static class MakeEnemiesUseBlurPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractMonster.class.getName()) && methodCall.getMethodName().equals("hasPower"))
                        methodCall.replace("$_ = ($proceed($$) || $proceed(\"" + BlurPower.POWER_ID + "\"));");
                }
            };
        }
    }
}