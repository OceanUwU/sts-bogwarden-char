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
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        setMagic(15, +3);
        setRetain(true);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new InflameEffect(adp()));
        atb(new AddTemporaryHPAction(adp(), adp(), magicNumber));
    }

    public void onChoseThisOption() {
        makeInHand(this);
    }
}