package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class PlagueOfToads extends AbstractBogCard {
    public final static String ID = makeID("PlagueOfToads");

    public PlagueOfToads() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        setDamage(2);
        setMagic(4, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            dmgRandom(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}