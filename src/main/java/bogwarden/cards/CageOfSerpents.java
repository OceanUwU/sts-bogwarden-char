package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class CageOfSerpents extends AbstractTrapCard {
    public final static String ID = makeID("CageOfSerpents");

    public CageOfSerpents() {
        super(ID, CardRarity.UNCOMMON);
        setDamage(3, +1);
        setMagic(3, +1);
        setSecondMagic(1);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            dmgRandomTop(AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        applyToEnemyTop(m, null);
    }
}