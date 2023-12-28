package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class Friends extends AbstractBogCard {
    public final static String ID = makeID("Friends");

    public Friends() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(1);
        setSecondMagic(3);
        setUpgradedCost(0);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new BuffererEffect(p.hb.cX), BuffererEffect.DURATION / 2f - 0.2f);
        applyToSelf(new BufferPower(p, 1));
        applyToSelf(new NextTurnIntangible(p, magicNumber));
        applyToSelf(new LoseMaxHPLater(p, secondMagic, 2));
    }

    private static class BuffererEffect extends AbstractGameEffect {
        private static final TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/friend1.png")), 0, 0, 207, 233);
        private static final float DURATION = 1.2f;

        private float x, y;
        
        public BuffererEffect(float x) {
            this.x = x + 200f * Settings.scale;
            this.y = AbstractDungeon.floorY;
            color = new Color(1f, 1f, 1f, 0.5f);
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            float progress = duration / DURATION;
            float n = 1f - (float)Math.pow(2f * progress - 1, 2);
            scale = Settings.scale * n;
            rotation = -140f + 140f * n;
            if (progress >= 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(IMG, x - 135f, y, 135, 0, 207, 233, scale, scale, rotation);
        }

        public void dispose() {}
    }

    public static class NextTurnIntangible extends AbstractBogPower {
        public static String POWER_ID = makeID("NextTurnIntangible");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public NextTurnIntangible(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            flash();
            vfx(new IntangibleVisitorEffect(owner.hb.cX, owner.hb.cY), 0.6f);
            applyToSelf(new IntangiblePlayerPower(owner, amount));
            atb(new RemoveSpecificPowerAction(owner, owner, this));
        }

        private static class IntangibleVisitorEffect extends AbstractGameEffect {
            private static final TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/friend2.png")), 0, 0, 156, 257);
            private static final float DURATION = 2.4f,
                UPPIES = 400f * Settings.scale,
                ACROSSIES = 30f * Settings.scale,
                BIGGIES = 3f;

            private float sX, sY, x, y;
            
            public IntangibleVisitorEffect(float sX, float sY) {
                this.sX = sX;
                this.sY = sY;
                color = Color.WHITE.cpy();
            }

            public void update() {
                duration += Gdx.graphics.getDeltaTime();
                float progress = duration / DURATION;
                if (progress < 0.1f)
                    color.a = progress / 0.1f;
                else if (progress > 0.75f)
                    color.a = 1 - (progress - 0.75f) / 0.25f;
                else
                    color.a = 1f;
                color.a *= 0.5f;
                scale = Settings.scale * (0.4f + (BIGGIES - 0.4f) * progress);
                y = sY + UPPIES * progress;
                x = sX + (float)Math.sin(progress * 10f) * ACROSSIES;
                rotation = (float)Math.sin(progress * 10f) * -20f;
                if (progress >= 1f)
                    isDone = true;
            }

            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, x - 107f, y, 107, 0, 156, 257, scale, scale, rotation);
            }

            public void dispose() {}
        }
    }

    public static class LoseMaxHPLater extends AbstractBogPower implements NonStackablePower {
        public static String POWER_ID = makeID("LoseMaxHPLater");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public LoseMaxHPLater(AbstractCreature owner, int amount, int turns) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
            isTwoAmount = true;
            amount2 = turns;
            updateDescription();
        }

        public boolean isStackable(AbstractPower power) {
            return power instanceof LoseMaxHPLater && ((LoseMaxHPLater)power).amount2 == amount2;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount2 + powerStrings.DESCRIPTIONS[amount2 == 1 ? 1 : 2] + amount + powerStrings.DESCRIPTIONS[3];
        }
  
        public void atStartOfTurn() {
            if (--amount2 <= 0) {
                int maxHPToLose = amount;
                vfx(new MaxHPTakerEffect(owner.hb.cX, owner.hb.cY), MaxHPTakerEffect.DURATION / 2f - 0.2f);
                atb(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        CardCrawlGame.sound.play("NECRONOMICON");
                        owner.decreaseMaxHealth(maxHPToLose);
                        AbstractDungeon.effectsQueue.add(new DamageNumberEffect(owner, owner.hb.cX, owner.hb.cY, maxHPToLose));
                        for(int i = 0; i < 50; i++)
                            AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(owner.hb.cX, owner.hb.cY));
                        flash();
                    }
                });
                atb(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                stackPower(0);
                updateDescription();
            }
        }

        private static class MaxHPTakerEffect extends AbstractGameEffect {
            private static final TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/friend3.png")), 0, 0, 165, 263);
            private static final float DURATION = 2.5f;

            private float sX, sY, tX, tY, x, y;
            
            public MaxHPTakerEffect(float x, float y) {
                this.sX = Settings.WIDTH + IMG.packedWidth / 2f;
                this.sY = y + 300f * Settings.scale;
                this.tX = x + 160f * Settings.scale;
                this.tY = y;
                color = new Color(1f, 1f, 1f, 0.5f);
            }

            public void update() {
                duration += Gdx.graphics.getDeltaTime();
                float progress = duration / DURATION;
                float n = 1f - (float)Math.pow(2f * progress - 1, 2);
                x = sX + (tX - sX) * n;
                y = sY + (tY - sY) * n;
                scale = Settings.scale * (0.6f + 0.4f * n);
                rotation = -30f + 30f * n;
                if (progress >= 1f)
                    isDone = true;
            }

            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, x - IMG.packedWidth / 2f, y - IMG.packedHeight / 2f, IMG.packedWidth/2f, IMG.packedHeight/2f, IMG.packedWidth, IMG.packedHeight, scale, scale, rotation);
            }

            public void dispose() {}
        }
    }
}