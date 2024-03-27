package bogwarden.cards;

import bogwarden.powers.SnaredPower;
import bogwarden.vfx.LassoEffect;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class EntanglingVines extends AbstractBogCard {
    public final static String ID = makeID("EntanglingVines");

    public EntanglingVines() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new LassoEffect(p.hb.x + p.hb.width, p.hb.cY, m.hb.cX, m.hb.cY, new Color(0.15f, 0.4f, 0.2f, 1f), "vfx/vine2.png"), LassoEffect.DURATION - 0.3f);
        applyToEnemy(m, new WeakPower(m, magicNumber, false));
        applyToEnemy(m, new SnaredPower(m, magicNumber));
    }
}