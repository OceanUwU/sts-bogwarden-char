package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SludgeBomb extends AbstractTrapCard {
    public final static String ID = makeID("SludgeBomb");

    public SludgeBomb() {
        super(ID, CardRarity.RARE);
        setExhaust(true);
        setDamage(6, +2);
        setMagic(1, +1);
        isMultiDamage = true;
        damageType = damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLivingTop(mo -> applyToEnemyTop(mo, new StrengthPower(mo, -magicNumber)));
        allDmgTop(AbstractGameAction.AttackEffect.POISON);
    }
}