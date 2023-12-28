package bogwarden.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class RabbitPunch extends AbstractBogCard {
    public final static String ID = makeID("RabbitPunch");

    public RabbitPunch() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(15, +5);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new RabbitPunchEffect(m.hb.cX - p.hb.cX - 150f * Settings.scale), RabbitPunchEffect.MOVE_TIME - 0.1f);
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        vfx(new TheRumble.CreatureFlyEffect(m));
    }

    private static class RabbitPunchEffect extends AbstractGameEffect {
        public static final float MOVE_TIME = 0.3f;
        private static final float STAY_TIME = 0.2f;

        float moveX;

        public RabbitPunchEffect(float moveX) {
            this.moveX = moveX;
            duration = 0f;
        }

        public void update() {
            if (adp() == null) {
                isDone = true;
                return;
            }
            if (duration < MOVE_TIME)
                adp().animX = duration / MOVE_TIME * moveX;
            else if (duration < MOVE_TIME + STAY_TIME)
                adp().animX = moveX;
            else if (duration < MOVE_TIME * 2 + STAY_TIME)
                adp().animX = (1f - (duration - MOVE_TIME - STAY_TIME) / MOVE_TIME) * moveX;
            else {
                adp().animX = 0f;
                isDone = true;
            }
            duration += Gdx.graphics.getDeltaTime();
        }

        public void render(SpriteBatch sb) {}
        public void dispose() {}
    }
}