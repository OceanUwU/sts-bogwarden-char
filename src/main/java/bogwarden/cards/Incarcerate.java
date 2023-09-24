package bogwarden.cards;

import bogwarden.actions.EasyXCostAction;
import bogwarden.actions.TriggerTrapAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Incarcerate extends AbstractBogCard {
    public final static String ID = makeID("Incarcerate");

    public Incarcerate() {
        super(ID, -1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(5);
        setMagic(0, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            if (effect > 0)
                att(new TriggerTrapAction(effect));
            for (int i = 0; i < effect + magicNumber; i++)
                dmgTop(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            return true;
        }));
    }
}