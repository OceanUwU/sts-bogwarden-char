package bogwarden.cards;


import bogwarden.powers.AbstractBogPower;
import bogwarden.util.TexLoader;
import bogwarden.vfx.IncantationEffect;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class RestlessSpirits extends AbstractBogCard {
    public final static String ID = makeID("RestlessSpirits");

    public RestlessSpirits() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setInnate(false, true);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new RestlessSpiritsPower(p, 1));
    }

    public static class RestlessSpiritsPower extends AbstractBogPower {
        public static String POWER_ID = makeID("RestlessSpiritsPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public RestlessSpiritsPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2];
        }
  
        public void atStartOfTurn() {
            flash();
            vfx(new IncantationEffect());
            vfx(new FlyingSpiritEffect("2", (float)Math.PI * 0.2f, 1f));
            vfx(new FlyingSpiritEffect("3", (float)Math.PI * -0.2f, 0.8f));
            vfx(new FlyingSpiritEffect("1", 0f, 1.5f));
            atb(new MakeTempCardInHandAction(new Blast(), amount, false));
        }
    }

    public static class FlyingSpiritEffect extends AbstractGameEffect {
        private static final float DURATION = 1.8f;
        private static final float ACROSSIES = 100f * Settings.scale;
        private static final float UPPIES = 320f * Settings.scale;

        private float x, y, turnt, yMult;
        private TextureAtlas.AtlasRegion img;

        public FlyingSpiritEffect(String spirit, float turnt, float yMult) {
            this.yMult = yMult;
            color = Color.WHITE.cpy();
            this.turnt = turnt;
            img = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/spirit"+spirit+".png")), 0, 0, 450, 450);
            scale *= 0.5f;
            renderBehind = true;
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            float progress = duration / DURATION;
            if (progress < 0.2f)
                color.a = progress / 0.2f;
            else if (progress > 0.8f)
                color.a = 1 - (progress - 0.8f) / 0.2f;
            else
                color.a = 1f;
            color.a *= 0.5f;
            float oX = (float)Math.sin(progress * 13f) * (0.2f + progress) * ACROSSIES,
                oY = progress * UPPIES * yMult;
            x = adp().hb.cX + oX * (float)Math.cos(turnt) - oY * (float)Math.sin(turnt);
            y = adp().hb.cY + oY * (float)Math.cos(turnt) + oX * (float)Math.sin(turnt);
            rotation = (float)Math.sin(progress * 13f) * (0.2f + progress) * -20f;
            if (progress >= 1f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(img, x - 275f, y - 275f, 275, 275, 450, 450, scale, scale, rotation);
        }

        public void dispose() {}
    }
}