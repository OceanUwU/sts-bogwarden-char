package bogwarden.cards;

import bogwarden.powers.Venom;
import bogwarden.vfx.SnakeEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class ViperStrike extends AbstractBogCard {
    public final static String ID = makeID("ViperStrike");

    public ViperStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(2, +2);
        setMagic(1);
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new SnakeEffect(p.hb.x + p.hb.width, p.hb.y, m.hb.cX, m.hb.y), SnakeEffect.DURATION - 0.2f);
        applyToEnemy(m, new Venom(m, magicNumber));
        for (int i = 0; i < 2; i++)
            dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }
}