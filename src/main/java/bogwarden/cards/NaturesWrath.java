package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class NaturesWrath extends AbstractBogCard {
    public final static String ID = makeID("NaturesWrath");

    public NaturesWrath() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setDamage(6, +2);
        isMultiDamage = true;
    }

    public void applyPowers() {
        super.applyPowers();
        calcBuffs();
    }

    public void calcBuffs() {
        baseMagicNumber = magicNumber = (int)adp().powers.stream().filter(po -> po.type.equals(AbstractPower.PowerType.BUFF)).count();
    }

    public void triggerOnCardPlayed(AbstractCard c) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                calcBuffs();
            }
        });
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        calcBuffs();
        for (int i = 0; i < magicNumber; i++)
            allDmg(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }
}