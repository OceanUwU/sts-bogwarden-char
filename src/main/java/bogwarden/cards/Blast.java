package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Blast extends AbstractBogCard {
    public final static String ID = makeID("Blast");

    public Blast() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ALL_ENEMY, CardColor.COLORLESS);
        setDamage(7, +2);
        setSecondMagic(0);
        setRetain(true);
        setExhaust(true);
        damageType = damageTypeForTurn = DamageType.HP_LOSS;
    }

    public void applyPowers() {
        super.applyPowers();
        secondMagic = baseSecondMagic;
        secondMagic += pwrAmt(adp(), PoisonNova.PoisonNovaPower.POWER_ID);
        isMagicNumberModified = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmgRandom(AbstractGameAction.AttackEffect.FIRE, secondMagic > 0 ? mo -> {
            p.getPower(PoisonNova.PoisonNovaPower.POWER_ID).flash();
            applyToEnemyTop(mo, new PoisonPower(mo, p, secondMagic));
        } : null);
    }
}