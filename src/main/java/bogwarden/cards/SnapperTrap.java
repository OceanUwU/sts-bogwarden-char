package bogwarden.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SnapperTrap extends AbstractTrapCard {
    public final static String ID = makeID("SnapperTrap");

    public SnapperTrap() {
        super(ID, CardRarity.COMMON);
        setDamage(9, +3);
        damageType = damageTypeForTurn = DamageType.HP_LOSS;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        calculateCardDamage(m);
        thornDmgTop(m, damage);
        att(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40f * Settings.scale, Color.GRAY.cpy()), 0.1f));
    }
}