package bogwarden.cards;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Bonfire extends AbstractBogCard {
    public final static String ID = makeID("Bonfire");

    public Bonfire() {
        super(ID, -2, CardType.POWER, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        setMagic(4, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        atb(new VFXAction(adp(), new InflameEffect(adp()), 1f));
        applyToSelf(new RegenPower(adp(), magicNumber));
    }
}