package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.SlowPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SludgeBomb extends AbstractTrapCard {
    public final static String ID = makeID("SludgeBomb");

    public SludgeBomb() {
        super(ID, CardRarity.RARE);
        setExhaust(true);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> applyToEnemy(m, new SlowPower(m, 0)));
        if (damage > 0)
            dmg(m, AbstractGameAction.AttackEffect.POISON);
    }

    public void upp() {
        super.upp();
        baseDamage = 6;
        damageType = damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
    }
}