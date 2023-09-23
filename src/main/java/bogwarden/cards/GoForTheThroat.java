package bogwarden.cards;

import bogwarden.actions.EasyXCostAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class GoForTheThroat extends AbstractBogCard {
    public final static String ID = makeID("GoForTheThroat");

    public GoForTheThroat() {
        super(ID, -1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(6, +2);
        setSecondDamage(15, +5);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new EasyXCostAction(this, (effect, params) -> {
            for (int i = 0; i < effect; i++)
                dmgTop(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            return true;
        }));
        if (isEliteOrBoss())
            altDmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }
}