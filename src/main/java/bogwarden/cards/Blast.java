package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class Blast extends AbstractBogCard {
    public final static String ID = makeID("Blast");

    public Blast() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.ALL_ENEMY, CardColor.COLORLESS);
        setDamage(7, +2);
        setRetain(true);
        damageType = damageTypeForTurn = DamageType.HP_LOSS;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmgRandom(AbstractGameAction.AttackEffect.FIRE);
    }
}