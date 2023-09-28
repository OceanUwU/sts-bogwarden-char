package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.powers.Venom;

public class ViperStrike extends AbstractBogCard {
    public final static String ID = makeID("ViperStrike");

    public ViperStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(5, +3);
        setMagic(2);
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        applyToEnemy(m, new Venom(m, magicNumber));
    }
}