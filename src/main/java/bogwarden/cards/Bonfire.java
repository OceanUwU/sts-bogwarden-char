package bogwarden.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Bonfire extends AbstractBogCard {
    public final static String ID = makeID("Bonfire");

    public Bonfire() {
        super(ID, -2, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        setMagic(15, +3);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        vfx(new InflameEffect(adp()));
        atb(new AddTemporaryHPAction(adp(), adp(), magicNumber));
    }
}