package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.WhirlwindEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpinningBlades extends AbstractTrapCard {
    public final static String ID = makeID("SpinningBlades");

    public SpinningBlades() {
        super(ID, CardRarity.UNCOMMON);
        setBlock(3, +1);
        setDamage(3, +1);
        isMultiDamage = true;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i< 2; i++) {
            allDmgTop(AbstractGameAction.AttackEffect.NONE);
            att(new VFXAction(p, new CleaveEffect(), 0.0F));
            blckTop();
        }
        att(new VFXAction(new WhirlwindEffect(), 0.0F));
    }
}