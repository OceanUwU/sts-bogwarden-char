package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Sap extends AbstractBogCard {
    public final static String ID = makeID("Sap");

    public Sap() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setUpgradedCost(0);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                att(new GainEnergyAction((int)m.powers.stream().filter(po -> po.type.equals(AbstractPower.PowerType.DEBUFF)).count()));
            }
        });
    }
}