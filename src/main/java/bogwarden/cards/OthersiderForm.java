package bogwarden.cards;

import basemod.helpers.BaseModCardTags;
import bogwarden.characters.TheBogwarden;
import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.Mojo;
import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import bogwarden.vfx.MojoFlashEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.Bone;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class OthersiderForm extends AbstractBogCard {
    public final static String ID = makeID("OthersiderForm");

    public OthersiderForm() {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(2, +1);
        setSecondMagic(1);
        tags.add(BaseModCardTags.FORM);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new OthersiderFormMojoPower(p, magicNumber));
        if (p instanceof TheBogwarden)
            vfx(new OthersiderTransformationEffect((TheBogwarden)p));
        applyToSelf(new OthersiderFormStrengthPower(p, secondMagic));
    }

    public static class OthersiderFormMojoPower extends AbstractBogPower {
        public static String POWER_ID = makeID("OthersiderFormMojoPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public OthersiderFormMojoPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            priority = 1;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            flash();
            vfx(new MojoFlashEffect(owner.hb.cX, owner.hb.cY));
            applyToSelf(new Mojo(owner, amount));
        }
    }

    public static class OthersiderFormStrengthPower extends AbstractBogPower {
        public static String POWER_ID = makeID("OthersiderFormStrengthPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public OthersiderFormStrengthPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurnPostDraw() {
            flash();
            applyToSelf(new StrengthPower(owner, -amount));
        }
    }

    public static class OthersiderTransformationEffect extends AbstractGameEffect {
        public static String OTHERSIDER_SKIN = "othersider";
        private static final float DURATION = 0.8f;

        private TheBogwarden bogwarden;
        private Bone rootBone;
        private boolean transformed;
        private float initialScale;

        public OthersiderTransformationEffect(TheBogwarden bogwarden) {
            this.bogwarden = bogwarden;
            rootBone = bogwarden.getSkeleton().getRootBone();
            initialScale = rootBone.getScaleX();
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            float progress = duration / DURATION;
            if (progress >= 0.75f && !transformed) {
                transformed = true;
                bogwarden.setupAnimation(OTHERSIDER_SKIN);
                rootBone = bogwarden.getSkeleton().getRootBone();
                initialScale = rootBone.getScaleX();
                CardCrawlGame.sound.play(BogAudio.OTHERSIDER);
                for (int i = 0; i < 16; i++)
                    AbstractDungeon.effectsQueue.add(new OthersiderTransformationParticle(bogwarden.hb.cX, bogwarden.hb.cY));
            }
            rootBone.setScaleX((float)Math.cos(Math.PI * progress * 2) * initialScale);
            if (progress >= 1f) {
                rootBone.setScaleX(initialScale);
                isDone = true;
            }
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}

        private static class OthersiderTransformationParticle extends AbstractGameEffect {
            private static ArrayList<Texture> IMAGES;
            private static final float SIZE = 32f;
            private static final float HALFSIZE = SIZE / 2f;

            private float x, y, cX, cY, startDistance, endDistance, targetScale, timer;
            private Texture img;
            private int flickers = MathUtils.random(2, 5);

            public OthersiderTransformationParticle(float cX, float cY) {
                this.cX = cX;
                this.cY = cY;
                duration = MathUtils.random(1.5f, 2.2f);
                rotation = MathUtils.random(0f, (float)Math.PI * 2f);
                startDistance = MathUtils.random(50f, 100f) * Settings.scale;
                endDistance = MathUtils.random(20f, 100f) * Settings.scale;
                targetScale = scale * MathUtils.random(0.75f, 1.25f);
                loadTextures();
                color = Color.WHITE.cpy();
                img = IMAGES.get(MathUtils.random(0, IMAGES.size()-1));
            }

            private static void loadTextures() {
                if (IMAGES == null) {
                    IMAGES = new ArrayList<>();
                    for (int i = 1; i <= 4; i++)
                        IMAGES.add(TexLoader.getTexture(makeImagePath("vfx/othersidervfx"+i+".png")));
                }
            }

            public void update() {
                timer += Gdx.graphics.getDeltaTime();
                float progress = timer / duration;
                scale = ((float)-Math.pow(2f * progress - 1f, 2) + 1f) * targetScale;
                color.a = 0.25f + Math.abs((float)Math.sin((progress * flickers) * Math.PI * 2) * 0.5f);
                float distance = startDistance + endDistance * progress;
                x = cX + (float)Math.cos(rotation) * distance;
                y = cY + (float)Math.sin(rotation) * distance;
                if (progress >= 1f)
                    isDone = true;
            }

            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(img, x - HALFSIZE * scale, y - HALFSIZE * scale, SIZE * scale, SIZE * scale);
            }

            public void dispose() {}
        }

        @SpirePatch(clz=AbstractDungeon.class, method="nextRoomTransition", paramtypez={SaveFile.class})
        public static class ResetOthersiderSkin {
            public static void Postfix() {
                if (AbstractDungeon.player instanceof TheBogwarden) {
                    if (((TheBogwarden)AbstractDungeon.player).currentSkin.equals(OTHERSIDER_SKIN))
                        ((TheBogwarden)AbstractDungeon.player).setupAnimation(((TheBogwarden)AbstractDungeon.player).defaultSkin);
                }
            }
        }
    }
}