package bogwarden.cards;

import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class NaturesWrath extends AbstractBogCard {
    public final static String ID = makeID("NaturesWrath");

    public NaturesWrath() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setDamage(6, +2);
        isMultiDamage = true;
    }

    public void applyPowers() {
        super.applyPowers();
        calcBuffs();
    }

    public void calcBuffs() {
        baseMagicNumber = magicNumber = (int)adp().powers.stream().filter(po -> po.type.equals(AbstractPower.PowerType.BUFF)).count();
    }

    public void triggerOnCardPlayed(AbstractCard c) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                calcBuffs();
            }
        });
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        calcBuffs();
        for (int i = 0; i < magicNumber; i++) {
            vfx(new PlantMassacreAllEnemiesEffect());
            allDmg(AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        }
    }

    public static class PlantMassacreAllEnemiesEffect extends AbstractGameEffect {
        private static final float DURATION = 0.09f;

        public PlantMassacreAllEnemiesEffect() {
            duration = DURATION;
        }

        public void update() {
            duration -= Gdx.graphics.getDeltaTime();
            if (duration <= 0f) {
                CardCrawlGame.sound.play(BogAudio.PLANT_PULL);
                forAllMonstersLiving(mo -> AbstractDungeon.effectsQueue.add(new PlantMassacreEffect(mo)));
                isDone = true;
            }
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}
    }

    public static class PlantMassacreEffect extends AbstractGameEffect {
        private static final float DURATION = 0.85f;

        private float x, y, yScale;
        private AbstractCreature target;
        private TextureAtlas.AtlasRegion img;

        public PlantMassacreEffect(AbstractCreature target) {
            this.target = target;
            x = target.hb.cX + MathUtils.random(-15f, 15f) * Settings.scale;
            img = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/natureswrath"+Integer.toString(MathUtils.random(1, 2))+".png")), 0, 0, 247, 156);
            img.flip(MathUtils.randomBoolean(), false);
            y = AbstractDungeon.floorY - (10f + MathUtils.random(-5f, 5f)) * Settings.scale;
        }

        public void update() {
            if (duration == 0f && target.isDead)
                isDone = true;
            duration += Gdx.graphics.getDeltaTime();
            float progress = duration / DURATION;
            if (progress < 0.12f)
                yScale = progress / 0.12f;
            else if (progress > 1f - 0.12f)
                yScale = (1f - progress) / 0.12f;
            else
                yScale = 1f;
            if (progress > 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(Color.WHITE);
            sb.draw(img, x - 123.5f, y - 42f, 123, 40, 247, 156, scale, scale * yScale, rotation);
        }

        public void dispose() {}
    }
}