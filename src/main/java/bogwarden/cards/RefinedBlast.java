package bogwarden.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class RefinedBlast extends AbstractBogCard {
    public final static String ID = makeID("RefinedBlast");

    public RefinedBlast() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setDamage(10, +2);
        setMagic(1, +1);
        setSecondMagic(0);
        setRetain(true);
        setExhaust(true);
        damageType = damageTypeForTurn = DamageType.HP_LOSS;
    }

    public void applyPowers() {
        super.applyPowers();
        baseSecondMagic = secondMagic = pwrAmt(adp(), PoisonNova.PoisonNovaPower.POWER_ID);
        isSecondMagicModified = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmgRandom(BLAST_EFFECT, secondMagic > 0 ? mo -> {
            p.getPower(PoisonNova.PoisonNovaPower.POWER_ID).flash();
            applyToEnemyTop(mo, new PoisonPower(mo, p, secondMagic));
        } : null, false);
        atb(new DrawCardAction(magicNumber));
    }
}