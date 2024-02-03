package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.Spines;
import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

import bogwarden.cards.Bushwhack.LeafSprayEffect;

public class BriarPatch extends AbstractBogCard {
    public final static String ID = makeID("BriarPatch");

    public BriarPatch() {
        super(ID, 2, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(6, +4);
        setSecondMagic(50);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                CardCrawlGame.sound.play(BogAudio.RUSTLE);
                for (int i = 0; i < 10; i++)
                    AbstractDungeon.effectList.add(new LeafSprayEffect(p.hb.cX, p.hb.cY));
                for (AbstractGameEffect e : AbstractDungeon.effectList)
                    if (e instanceof BriarEffect)
                        return;
                vfxTop(new BriarEffect.BriarEffectBehind());
                vfxTop(new BriarEffect());
            }
        });
        applyToSelf(new Spines(p, magicNumber));
        applyToSelf(new BriarPatchPower(p, secondMagic));
    }

    public static class BriarPatchPower extends AbstractBogPower {
        public static String POWER_ID = makeID("BriarPatchPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BriarPatchPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

  
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS && info.owner != null && info.owner != owner) {
                flash();
                addToTop((AbstractGameAction)new DamageAction(info.owner, new DamageInfo(owner, pwrAmt(owner, Spines.POWER_ID) * amount / 100, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
            } 
            return damageAmount;
        }
    }

    private static class BriarEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/briarfront.png")), 0, 0, 400, 400);

        public BriarEffect() {
            color = Color.WHITE.cpy();
        }

        public void update() {
            duration += Gdx.graphics.getDeltaTime();
            color.a = 1f;
            if (duration < 0.2f)
                color.a = duration / 0.2f;
        }
  
        public void render(SpriteBatch sb) {
            sb.draw(IMG, adp().hb.cX - 200f, adp().hb.cY - 200f, 200, 200, 400, 400, scale, scale, 0f);
        }

        public void dispose() {}

        private static class BriarEffectBehind extends AbstractGameEffect {
            private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/briarback.png")), 0, 0, 400, 400);

            public BriarEffectBehind() {
                color = Color.WHITE.cpy();
                renderBehind = true;
            }

            public void update() {
                duration += Gdx.graphics.getDeltaTime();
                color.a = 1f;
                if (duration < 0.2f)
                    color.a = duration / 0.2f;
            }
    
            public void render(SpriteBatch sb) {
                sb.setColor(color);
                sb.draw(IMG, adp().hb.cX - 200f, adp().hb.cY - 200f, 200, 200, 400, 400, scale, scale, 0f);
            }

            public void dispose() {}
        }
    }
}