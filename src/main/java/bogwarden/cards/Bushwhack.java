package bogwarden.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;

public class Bushwhack extends AbstractBogCard {
    public final static String ID = makeID("Bushwhack");
    private final static Bushwhack dummyCard = new Bushwhack();

    public Bushwhack() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setBlock(6, +1);
        setSecondMagic(2, +1);
        setThirdMagic(18, +2);
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        AbstractDungeon.actionManager.cardsPlayedThisCombat.add(dummyCard);
        applyPowers();
        AbstractDungeon.actionManager.cardsPlayedThisCombat.remove(dummyCard);
    }

    @Override
    public void applyPowers() {
        baseMagicNumber = magicNumber = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        super.applyPowers();
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() < thirdMagic) ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1 < thirdMagic) {
                    applyToSelfTop(new PlatedArmorPower(p, secondMagic));
                    CardCrawlGame.sound.play(BogAudio.RUSTLE);
                    for (int i = 0; i < 18; i++)
                        AbstractDungeon.effectList.add(new LeafSprayEffect(p.hb.cX, p.hb.cY));
                } else for (int i = 0; i < 4; i++)
                    AbstractDungeon.effectList.add(new LeafSprayEffect(p.hb.cX, p.hb.cY));
            }
        });
    }

    

    public static class LeafSprayEffect extends AbstractGameEffect {
        private static final int W = 75;
        private static final int H = 75;
        private static final TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/leaf.png")), 0, 0, W, H);

        private float x, y, oX, oY, rotation, shootOutTime, shootOutVel, shootOutDirection, timeScale, rotationScale, movementScale;
        private int xDir = MathUtils.random(0, 1) * 2 - 1;
        private boolean shooting = true;

        public LeafSprayEffect(float x, float y) {
            this.x = x;
            this.y = y;
            scale *= MathUtils.random(0.5f, 0.85f);
            shootOutTime = MathUtils.random(0.2f, 0.8f);
            shootOutVel = MathUtils.random(100f, 750f) * Settings.scale;
            shootOutDirection = MathUtils.random(0f, 2f * (float)Math.PI);
            timeScale = MathUtils.random(0.85f, 1.15f);
            rotationScale = MathUtils.random(20f, 40f);
            movementScale = MathUtils.random(150f, 300f) * Settings.scale;
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            if (shooting) {
                float vel = shootOutVel * (float)Math.cos(Math.PI * duration / shootOutTime / 2f);
                x += Math.cos(shootOutDirection) * Gdx.graphics.getDeltaTime() * vel;
                y += Math.sin(shootOutDirection) * Gdx.graphics.getDeltaTime() * vel;
                if (duration >= shootOutTime) {
                    shooting = false;
                    oX = x;
                    oY = y;
                    duration = 0f;
                }
            } else {
                x = oX + (float)Math.sin(duration * timeScale * Math.PI) * movementScale * xDir;
                y = oY + (0.6f * (float)Math.pow(Math.sin(duration * timeScale * Math.PI), 2) - 0.5f * duration * timeScale) * movementScale;
                rotation = (float)Math.sin(duration * timeScale * Math.PI) * rotationScale * xDir;
            }
            if (y < -100f * scale)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(Color.WHITE);
            sb.draw(IMG, x - W/2, y - H/2, W/2, H/2, W, H, scale, scale, rotation);
        }

        public void dispose() {}
    }
}