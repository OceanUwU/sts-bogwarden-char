package bogwarden.cards;

import bogwarden.powers.Maledict;
import bogwarden.powers.Venom;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpitefulStaff extends AbstractBogCard {
    public final static String ID = makeID("SpitefulStaff");

    public SpitefulStaff() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(13);
        setMagic(1, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
        applyToEnemy(m, new Maledict(m, magicNumber));
        applyToEnemy(m, new Venom(m, magicNumber));
    }
}