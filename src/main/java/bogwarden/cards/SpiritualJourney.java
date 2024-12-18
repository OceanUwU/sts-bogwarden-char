package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.MiracleEffect;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class SpiritualJourney extends AbstractBogCard {
    public final static String ID = makeID("SpiritualJourney");

    public SpiritualJourney() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        setMagic(1, +1);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null)
            applyToEnemy(m, new SpiritualJourneyEnemyPower(m, magicNumber));
    }

    public static class SpiritualJourneyEnemyPower extends AbstractBogPower {
        public static String POWER_ID = makeID("SpiritualJourneyEnemyPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        private SpiritualEffect effect;
        private boolean cancelled = false, absorbed = false;
    
        public SpiritualJourneyEnemyPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2];
        }

        @Override
        public void onRemove() {
            effect.removeAllOrbs();
        }

        @Override
        public void onInitialApplication() {
            effect = new SpiritualEffect(owner, amount);
            AbstractDungeon.effectList.add(effect);
            AbstractDungeon.effectList.add(effect.behind);
        }
        
        @Override
        public void stackPower(int amount) {
            super.stackPower(amount);
            effect.addOrbs(amount);
        }

        @Override
        public void onDeath() {
            if (!cancelled && !absorbed) {
                absorbed = true;
                effect.absOrb(adp());
                applyToSelfTop(new SpiritualJourneyPower(adp(), amount));
            }
        }

        public int onAttacked(DamageInfo info, int damageAmount) {
            if (!cancelled && !absorbed && info.type != DamageType.THORNS && info.type != DamageType.HP_LOSS && info.owner != null && info.owner == adp()) {
                cancelled = true;
                flash();
                effect.removeAllOrbs();
                addToTop(new RemoveSpecificPowerAction(owner, owner, this));
            }
            return damageAmount;
        }
    }

    public static class SpiritualJourneyPower extends AbstractBogPower {
        public static String POWER_ID = makeID("SpiritualJourneyPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        private static int left = 0;
        private static int floor = AbstractDungeon.floorNum;
    
        public SpiritualJourneyPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            floor = AbstractDungeon.floorNum;
            left += amount;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2];
        }

        @Override
        public void onRemove() {
            left -= amount;
        }

        @SpirePatch(clz=AbstractDungeon.class, method="getRewardCards")
        public static class SpiritPatch {
            @SpireInsertPatch(rloc=13, localvars={"numCards"})
            public static void Insert(@ByRef int[] numCards) {
                if (SpiritualJourneyPower.floor == AbstractDungeon.floorNum && SpiritualJourneyPower.left > 0) {
                    numCards[0] = numCards[0] + SpiritualJourneyPower.left;
                    SpiritualJourneyPower.left = 0;
                }
            }
        }
    }

    private static class SpiritualEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/gloworb.png")), 0, 0, 200, 200);
        private final float ROTATIONAL_VEL = 150f;
        private float rotation, entireDuration;
        private ArrayList<SpiritualOrb> orbs;
        private AbstractCreature target;
        private SpiritualBehindEffect behind;

        public SpiritualEffect(AbstractCreature target, int amount) {
            this.target = target;
            orbs = new ArrayList<>();
            addOrbs(amount);
            behind = new SpiritualBehindEffect();
            behind.orbs = orbs;
        }

        public void addOrbs(int amount) {
            for (int i = 0; i < amount; i++)
                orbs.add(new SpiritualOrb(target));
        }

        public void removeOrb() {
            if (orbs.size() > 0) {
                SpiritualOrb orb = orbs.get(0);
                orb.timeDir = -1;
                orb.duration = SpiritualOrb.APPEAR_TIME;
            }
        }

        public void removeAllOrbs() {
            for (SpiritualOrb orb : orbs) {
                orb.timeDir = -1;
                orb.duration = SpiritualOrb.APPEAR_TIME;
            }
        }

        public void absOrb(AbstractCreature absorbing) {
            for (SpiritualOrb orb : orbs)
                AbstractDungeon.effectList.add(new SpiritualAbsOrb(orb));
            isDone = true;
            behind.isDone = true;
        }

        public void update() {
            entireDuration -= Gdx.graphics.getDeltaTime();
            int s = orbs.size();
            for (int i = 0; i < s; i++)
                orbs.get(i).update(i, s, rotation, entireDuration);
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
            private ArrayList<SpiritualOrb> orbs;
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
            private static final float APPEAR_TIME = 0.25f;
            private static final float OVAL_EXTRA_RADIUS = 50f * Settings.scale;
            private static final float OVAL_HEIGHT = 75f * Settings.scale;
            private static final float OSCILLATE_HEIGHT = 40f * Settings.scale;
            private static final float SCALE_MULT = 0.4f;

            private float x, y, rotationOffset, targetRotationOffset, scale, duration, timeDir = 1, sinOffset = MathUtils.random(100f), dX, dY;
            private AbstractCreature target;
            private Color color;
            private boolean behind, done;

            public SpiritualOrb(AbstractCreature target) {
                this.target = target;
                color = new Color(MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), MathUtils.random(0.5f, 1f), 0f);
            }

            public void update(int i, int numOrbs, float rotation, float entireDuration) {
                targetRotationOffset = 360f * i / numOrbs;
                rotationOffset = MathUtils.lerpAngleDeg(rotationOffset, targetRotationOffset, 1f - (float)Math.pow(0.2, Gdx.graphics.getDeltaTime()));
                float angle = rotation + rotationOffset;
                behind = angle % 360f < 180f;
                if (duration < APPEAR_TIME)
                    color.a = duration / APPEAR_TIME;
                else
                    color.a = 1f;
                scale = Settings.scale * (1f - (float)Math.pow(color.a - 1, 2) / 2f) * SCALE_MULT;
                float prevX = x, prevY = y;
                x = target.hb.cX + MathUtils.cosDeg(angle) * (target.hb.width / 2f + OVAL_EXTRA_RADIUS);
                y = target.hb.cY + MathUtils.sinDeg(angle) * OVAL_HEIGHT + MathUtils.sin((entireDuration + sinOffset) * 3f) * OSCILLATE_HEIGHT;
                dX = (x - prevX) / Gdx.graphics.getDeltaTime();
                dY = (y - prevY) / Gdx.graphics.getDeltaTime();
                duration += Gdx.graphics.getDeltaTime() * timeDir;
                if (duration <= 0f)
                    done = true;
            }
    
            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, x - 100f, y - 100f, 100, 100, 200, 200, scale, scale, 0f);
            }
        }

        private static class SpiritualAbsOrb extends AbstractGameEffect {
            private float x, y, startX, startY, dX, dY;
            private Color color;

            public SpiritualAbsOrb(SpiritualOrb from) {
                startingDuration = MathUtils.random(0.5f, 2.0f);
                dX = from.dX;
                dY = from.dY;
                x = startX = from.x;
                y = startY = from.y;
                color = from.color;
                renderBehind = from.behind;
                scale *= SpiritualOrb.SCALE_MULT;
                from.done = true;
            }

            @Override
            public void update() {
                duration += Gdx.graphics.getDeltaTime();
                float progress = duration / startingDuration;
                x = MathUtils.lerp(startX + dX * duration, adp().hb.cX, Interpolation.pow3In.apply(progress));
                y = MathUtils.lerp(startY + dY * duration, adp().hb.cY, Interpolation.pow3In.apply(progress));
                if (progress > 0.8f)
                    renderBehind = true;
                if (progress >= 1f) {
                    isDone = true;
                    CardCrawlGame.sound.playA(BogAudio.MOJO, 0.2F);
                    AbstractDungeon.effectsQueue.add(new MiracleEffect(color.cpy(), color.cpy().mul(0.75f), ""));
                }
            }

            @Override
            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, x - 100f, y - 100f, 100, 100, 200, 200, scale, scale, 0f);
            }

            public void dispose() {}
        }
    }
}