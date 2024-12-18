package bogwarden.cards;

import basemod.ReflectionHacks;
import bogwarden.powers.SnaredPower;
import bogwarden.powers.Venom;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.SmokeBlurEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class AcidCloud extends AbstractBogCard {
    public final static String ID = makeID("AcidCloud");

    public AcidCloud() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        setMagic(4, +2);
        setSecondMagic(2, +1);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> vfx(new AcidCloudEffect(mo.hb.cX, mo.hb.cY)));
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new SnaredPower(mo, magicNumber));
            applyToEnemy(mo, new Venom(mo, secondMagic));
        });
    }

    public static class AcidCloudEffect extends AbstractGameEffect {
        private float x, y;
        
        public AcidCloudEffect(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public void update() {
            CardCrawlGame.sound.play("ATTACK_WHIFF_2");
            for (int i = 0; i < 10; i++) {
                SmokeBlurEffect smoke = new SmokeBlurEffect(this.x, this.y);
                ReflectionHacks.setPrivate(smoke, AbstractGameEffect.class, "color", new Color(MathUtils.random(0f, 0.1f), MathUtils.random(0.9f, 1f), MathUtils.random(0f, 0.1f), 1f));
                AbstractDungeon.effectsQueue.add(smoke);
            }
            isDone = true;
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}
    }
}