package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class TheRumble extends AbstractBogCard {
    public final static String ID = makeID("TheRumble");

    public TheRumble() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        setDamage(5, +2);
        setMagic(2);
        isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < (isEliteOrBoss() ? 1 + magicNumber : 1); i++)
            allDmg(AbstractGameAction.AttackEffect.LIGHTNING);
    }
}