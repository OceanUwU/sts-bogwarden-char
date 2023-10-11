package bogwarden.cards;

import bogwarden.powers.Venom;
import bogwarden.vfx.SnakeEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class CageOfSerpents extends AbstractTrapCard {
    public final static String ID = makeID("CageOfSerpents");

    public CageOfSerpents() {
        super(ID, CardRarity.UNCOMMON);
        setDamage(1);
        setMagic(3, +1);
        setSecondMagic(1);
        damageType = damageTypeForTurn = DamageType.HP_LOSS;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            for (int i = 0; i < magicNumber; i++)
                dmgTop(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
            applyToEnemyTop(m, new Venom(m, secondMagic));
            for (int i = 0; i < magicNumber; i++)
                vfxTop(new SnakeEffect(p.hb.x + p.hb.width, p.hb.y, m.hb.cX, m.hb.y), 0.1f);
        }
    }
}