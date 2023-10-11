package bogwarden.vfx;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.stance.WrathStanceChangeParticle;

public class IncantationEffect extends AbstractGameEffect {
    public void update() {
        isDone = true;
        CardCrawlGame.sound.play("ATTACK_FIRE");
        for (int i = 0; i < 10; i++) {
            WrathStanceChangeParticle particle = new WrathStanceChangeParticle(0);
            ReflectionHacks.setPrivate(particle, WrathStanceChangeParticle.class, "x", (float)ReflectionHacks.getPrivate(particle, WrathStanceChangeParticle.class, "x") + 55f * Settings.scale);
            ReflectionHacks.setPrivate(particle, AbstractGameEffect.class, "color",
                i % 3 > 0
                ? new Color(MathUtils.random(0f, 0.3f), 1f, 0.2f, 0f)
                : new Color(MathUtils.random(0.8f, 1f), 0.2f, MathUtils.random(0.8f, 1f), 0f));
            AbstractDungeon.effectsQueue.add(particle);
        }
    }

    public void render(SpriteBatch sb) {}
    public void dispose() {}
}
