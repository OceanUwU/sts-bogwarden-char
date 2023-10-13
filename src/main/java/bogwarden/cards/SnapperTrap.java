package bogwarden.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SnapperTrap extends AbstractTrapCard {
    public final static String ID = makeID("SnapperTrap");

    public SnapperTrap() {
        super(ID, CardRarity.BASIC);
        setDamage(8, +3);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            dmgTop(m, AbstractGameAction.AttackEffect.NONE);
            att(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40f * Settings.scale, Color.GRAY.cpy()), 0.1f));
        }
    }
}