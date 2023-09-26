package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.Maledict;

public class Jinx extends AbstractBogCard {
    public final static String ID = makeID("Jinx");

    public Jinx() {
        super(ID, 0, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        setDamage(3, +1);
        setMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (upgraded) {
            allDmg(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            forAllMonstersLiving(mo -> applyToEnemy(mo, new Maledict(mo, magicNumber)));
        } else {
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            applyToEnemy(m, new Maledict(m, magicNumber));
        }
    }

    @Override
    public void upp() {
        super.upp();
        isMultiDamage = true;
        target = CardTarget.ALL_ENEMY;
    }
}