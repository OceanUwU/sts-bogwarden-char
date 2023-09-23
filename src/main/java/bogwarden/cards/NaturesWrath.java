package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class NaturesWrath extends AbstractBogCard {
    public final static String ID = makeID("NaturesWrath");
    private static boolean redo;

    public NaturesWrath() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setDamage(6, +2);
        isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                use();
            }
        });
    }

    private void use() {
        att(new AbstractGameAction() {
            public void update() {
                isDone = true;
                redo = false;
                forAllMonstersLiving(mo -> {if (mo.currentBlock <= 0) redo = true;});
                if (redo)
                    use();
            }
        });
        for (int i = 0; i < 2; i++)
            allDmgTop(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }
}