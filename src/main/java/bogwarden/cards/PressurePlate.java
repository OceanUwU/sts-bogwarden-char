package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
        setMagic(5, +2);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        att(new AbstractGameAction() {
            public void update() {
                isDone = true;
                forAllMonstersLivingTop(mo -> {
                    if (mo.hasPower(MarkPower.POWER_ID))
                        att(new DamageAction(mo, new DamageInfo(p, pwrAmt(mo, MarkPower.POWER_ID), DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.FIRE));
                });
            }
        });
        applyToEnemyTop(m, new MarkPower(m, magicNumber));
        if (m != null)
            att(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
    }
}