package bogwarden.cards;

import basemod.BaseMod;
import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.Collections;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class Blowpipe extends AbstractBogCard {
    public final static String ID = makeID("Blowpipe");

    public Blowpipe() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(8, +1);
        setMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new DartEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY));
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        atb(new MultiGroupSelectAction(
            cardStrings.EXTENDED_DESCRIPTION[magicNumber == 1 ? 0 : 1],
            (cards, groups) -> {
                Collections.reverse(cards);
                cards.forEach(c -> att(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                            if (groups.get(c) == p.drawPile)
                                p.drawPile.moveToDiscardPile(c);
                            p.createHandIsFullDialog();
                        } else
                            p.hand.moveToHand(c, groups.get(c));
                    }
                }));
            },
            magicNumber, false, c -> c.cost == -2, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.DISCARD_PILE
        ));
    }

    private static class DartEffect extends AbstractGameEffect {
        private static TextureAtlas.AtlasRegion IMG = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/dart.png")), 0, 0, 128, 128);
        private static final float DURATION = 0.2f;

        private float x, y;
        private float startX, startY, endX, endY;

        public DartEffect(float startX, float startY, float endX, float endY) {
            duration = DURATION;
            color = Color.WHITE.cpy();
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
            rotation = -(float)Math.toDegrees(Math.atan2(endX - startX, endY - startY)) + 90f;
        }

        public void update() {
            if (duration == DURATION)
                CardCrawlGame.sound.play(BogAudio.BLOW_DART);
            float progress = 1f - (duration / DURATION);
            x = startX + (endX - startX) * progress;
            y = startY + (endY - startY) * progress;
            if (progress < 0.1f)
                color.a = progress / 0.1f;
            else if (progress > 0.9f)
                color.a = 1 - (progress - 0.9f) / 0.1f;
            else
                color.a = 1f;
            duration -= Gdx.graphics.getDeltaTime();
            if (duration <= 0f)
                isDone = true;
        }

        public void render(SpriteBatch sb) {
            sb.setColor(color);
            sb.draw(IMG, x - 64f, y - 64f, 64, 64, 128, 128, scale, scale, rotation);
        }

        public void dispose() {}
    }
}