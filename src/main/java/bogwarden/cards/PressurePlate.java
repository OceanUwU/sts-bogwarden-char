package bogwarden.cards;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.watcher.TriggerMarksAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class PressurePlate extends AbstractTrapCard {
    public final static String ID = makeID("PressurePlate");

    public PressurePlate() {
        super(ID, CardRarity.COMMON);
        setMagic(6, +3);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        att(new TriggerMarksAction(this));
        applyToEnemyTop(m, new MarkPower(m, magicNumber));
        if (m != null)
            att(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
    }
}