package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class SpiritualJourney extends AbstractBogCard {
    public final static String ID = makeID("SpiritualJourney");

    public SpiritualJourney() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        setMagic(4, +2);
        setSecondMagic(2, +2);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                for (AbstractGameEffect e : AbstractDungeon.effectList)
                    if (e instanceof SpiritualEffect) {
                        ((SpiritualEffect)e).addOrbs(secondMagic);
                        return;
                    }
                vfxTop(new SpiritualEffect.SpiritualBehindEffect());
                vfxTop(new SpiritualEffect(secondMagic));
            } 
        });
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                p.increaseMaxHp(magicNumber, true);
            } 
        });
        applyToSelf(new SpiritualJourneyPower(p, secondMagic));
    }

    public static class SpiritualJourneyPower extends AbstractBogPower {
        public static String POWER_ID = makeID("SpiritualJourneyPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public SpiritualJourneyPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                flash();
                att(new ReducePowerAction(owner, owner, this, 1));
                att(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        owner.decreaseMaxHealth(1);
                        for (AbstractGameEffect e : AbstractDungeon.effectList)
                            if (e instanceof SpiritualEffect) {
                                ((SpiritualEffect)e).removeOrb();
                                return;
                            }
                        AbstractDungeon.effectsQueue.add(new DamageNumberEffect(owner, owner.hb.cX, owner.hb.cY, 1));
                        for(int i = 0; i < 50; i++)
                            AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(owner.hb.cX, owner.hb.cY));
                    }
                });
            }
        }
    }

    private static class SpiritualEffect extends AbstractGameEffect {
        private static final float ROTATIONAL_VEL = 150f;
        private static float rotation, entireDuration;
        private static ArrayList<SpiritualOrb> orbs;

        public SpiritualEffect(int amount) {
            orbs = new ArrayList<>();
            addOrbs(amount);
        }

        public void addOrbs(int amount) {
            for (int i = 0; i < amount; i++)
                orbs.add(new SpiritualOrb());
        }

        public void removeOrb() {
            orbs.get(0).timeDir = -1;
            orbs.get(0).duration = SpiritualOrb.APPEAR_TIME;
        }

        public void update() {
            entireDuration -= Gdx.graphics.getDeltaTime();
            int s = orbs.size();
            for (int i = 0; i < s; i++)
                orbs.get(i).update(i, s);
            for (int i = 0; i < s; i++)
                if (orbs.get(i).done) {
                    orbs.remove(i);
                    break;
                }
        }
  
        public void render(SpriteBatch sb) {
            rotation += Gdx.graphics.getDeltaTime() * ROTATIONAL_VEL;
            for (SpiritualOrb orb : orbs)
                if (!orb.behind)
                    orb.render(sb);
        }

        public void dispose() {}

        private static class SpiritualBehindEffect extends AbstractGameEffect {
            public SpiritualBehindEffect() {
                renderBehind = true;
            }

            public void render(SpriteBatch sb) {
                for (SpiritualOrb orb : orbs)
                    if (orb.behind)
                        orb.render(sb);
            }
    
            public void update() {}
            public void dispose() {}
        }

        private static class SpiritualOrb {
            private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/gloworb.png")), 0, 0, 200, 200);
            private static final float APPEAR_TIME = 0.25f;
            private static final float OVAL_WIDTH = 200f * Settings.scale;
            private static final float OVAL_HEIGHT = 75f * Settings.scale;
            private static final float OSCILLATE_HEIGHT = 40f * Settings.scale;
            private static final float SCALE_MULT = 0.4f;

            private float x, y, rotationOffset, targetRotationOffset, scale, duration, timeDir = 1, sinOffset = MathUtils.random(100f);
            private Color color;
            private boolean behind, done;

            public SpiritualOrb() {
                color = new Color(MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), 0f);
            }

            public void update(int i, int numOrbs) {
                targetRotationOffset = 360f * i / numOrbs;
                rotationOffset = MathUtils.lerpAngleDeg(rotationOffset, targetRotationOffset, 1f - (float)Math.pow(0.2, Gdx.graphics.getDeltaTime()));
                float angle = rotation + rotationOffset;
                behind = angle % 360f < 180f;
                if (duration < APPEAR_TIME)
                    color.a = duration / APPEAR_TIME;
                else
                    color.a = 1f;
                scale = Settings.scale * (1f - (float)Math.pow(color.a - 1, 2) / 2f) * SCALE_MULT;
                x = adp().hb.cX + MathUtils.cosDeg(angle) * OVAL_WIDTH;
                y = adp().hb.cY + MathUtils.sinDeg(angle) * OVAL_HEIGHT + MathUtils.sin((entireDuration + sinOffset) * 3f) * OSCILLATE_HEIGHT;
                duration += Gdx.graphics.getDeltaTime() * timeDir;
                if (duration <= 0f)
                    done = true;
            }
    
            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, x - 100f, y - 100f, 100, 100, 200, 200, scale, scale, 0f);
            }
        }
    }
}