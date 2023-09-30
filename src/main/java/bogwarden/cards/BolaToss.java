package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BolaToss extends AbstractBogCard {
    public final static String ID = makeID("BolaToss");

    public BolaToss() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(2, +1);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        applyToEnemy(m, new ConstrictedPower(m, p, magicNumber));
    }
}