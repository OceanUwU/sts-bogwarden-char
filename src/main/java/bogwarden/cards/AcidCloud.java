package bogwarden.cards;

import bogwarden.powers.Venom;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class AcidCloud extends AbstractBogCard {
    public final static String ID = makeID("AcidCloud");

    public AcidCloud() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        setMagic(3, +2);
        setDamage(1);
        setExhaust(true);
        damageType = damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
        isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> applyToEnemy(mo, new Venom(mo, magicNumber)));
        allDmg(AbstractGameAction.AttackEffect.POISON);
    }
}