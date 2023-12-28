package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.util.BogAudio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BlendingIn extends AbstractBogCard {
    public final static String ID = makeID("BlendingIn");

    public BlendingIn() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(0, +3);
        setSecondMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (magicNumber > 0)
            applyToSelf(new BlendingInBlockPower(p, magicNumber));
        applyToSelf(new BlendingInPower(p, secondMagic));
    }

    public static class BlendingInPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BlendingInPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        private float fogTimer = 0f;
        private boolean shouldActivate = !AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(c -> c.type.equals(AbstractCard.CardType.ATTACK));
    
        public BlendingInPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void atStartOfTurn() {
            shouldActivate = true;
        }
  
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            if (shouldActivate) {
                flash();
                applyToSelf(new BlurPower(owner, amount));
            }
        }
  
        public void onUseCard(AbstractCard card, UseCardAction action) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                if (shouldActivate) {
                    CardCrawlGame.sound.play(BogAudio.WHOOSH);
                    AbstractDungeon.effectList.stream().filter(e -> e instanceof BlendInFog).forEach(e -> ((BlendInFog)e).whooshAway());
                }
                shouldActivate = false;
            } 
        }

        @Override
        public void update(int slot) {
            super.update(slot);
            if (shouldActivate) {
                fogTimer += Gdx.graphics.getDeltaTime();
                while (fogTimer >= BlendInFog.FOG_INTERVAL) {
                    fogTimer -= BlendInFog.FOG_INTERVAL;
                    AbstractDungeon.effectList.add(new BlendInFog(owner.hb.cX, owner.hb.cY));
                }
            }
        }
    }

    public static class BlendingInBlockPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BlendingInBlockPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        
        private boolean shouldActivate = !AbstractDungeon.actionManager.cardsPlayedThisTurn.stream().anyMatch(c -> c.type.equals(AbstractCard.CardType.ATTACK));
    
        public BlendingInBlockPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void atStartOfTurn() {
            shouldActivate = true;
        }
  
        public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
            if (shouldActivate) {
                flash();
                atb(new GainBlockAction(owner, owner, amount));
            }
        }
  
        public void onUseCard(AbstractCard card, UseCardAction action) {
            if (card.type == AbstractCard.CardType.ATTACK)
                shouldActivate = false;
        }
    }

    private static class BlendInFog extends AbstractGameEffect {
        private static final float DURATION = 3.2f,
            SWOOSH_DURATION = 1.3f,
            SWOOSH_MOVEMENT_MULT = 5f,
            FOG_INTERVAL = 0.32f,
            MAX_OPACITY = 0.2f;
        
        private AtlasRegion img;
        private boolean swooshing;
        private float x, y, xVel, swooshPacity, rotationalVel;

        public BlendInFog(float x, float y) {
            img = ImageMaster.EXHAUST_L;
            renderBehind = MathUtils.randomBoolean();
            color = Color.LIGHT_GRAY.cpy();
            color.a = 0f;
            this.x = x  + MathUtils.random(-40f, 40f) * scale;
            this.y = y + MathUtils.random(-50f, 50f) * scale;
            this.xVel = MathUtils.random(25f, 40f) * (MathUtils.random(0, 1) * 2 - 1) * scale;
            scale *= MathUtils.random(1.25f, 1.5f);
            rotation = MathUtils.random(0f, 360f);
            rotationalVel = MathUtils.random(-20f, 20f);
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            x += xVel * Gdx.graphics.getDeltaTime() * (swooshing ? SWOOSH_MOVEMENT_MULT : 1f);
            rotation += rotationalVel * Gdx.graphics.getDeltaTime();
            float progress = duration / (swooshing ? SWOOSH_DURATION : DURATION);
            if (swooshing) {
                color.a = swooshPacity * (1f - progress);
            } else {
                if (progress < 0.2f)
                    color.a = MAX_OPACITY * progress / 0.2f;
                else if (progress > 1f - 0.2f)
                    color.a = MAX_OPACITY * (1f - progress) / 0.2f;
            }
            if (progress > 1f)
                isDone = true;
        }

        public void whooshAway() {
            swooshing = true;
            duration = 0f;
            swooshPacity = color.a;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(img, x - img.packedWidth / 2f, y - img.packedHeight / 2f, img.packedWidth / 2.0F, img.packedHeight / 2.0F, img.packedWidth, img.packedHeight, scale, scale, rotation);
        }

        public void dispose() {}
    }
}