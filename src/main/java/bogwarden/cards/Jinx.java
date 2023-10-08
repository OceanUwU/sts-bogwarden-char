package bogwarden.cards;

import bogwarden.powers.Maledict;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Jinx extends AbstractBogCard {
    public final static String ID = makeID("Jinx");

    public Jinx() {
        super(ID, 0, CardType.ATTACK, CardRarity.BASIC, CardTarget.ENEMY);
        setDamage(3, +1);
        setMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot((AbstractGameAction)new SFXAction("THUNDERCLAP", 0.05F));
        if (upgraded) {
            forAllMonstersLiving(mo -> vfx(new LightningEffect(mo.hb.cX, mo.hb.cY), 0.05f));
            allDmg(AbstractGameAction.AttackEffect.NONE);
            forAllMonstersLiving(mo -> applyToEnemy(mo, new Maledict(mo, magicNumber)));
        } else {
            vfx(new LightningEffect(m.hb.cX, m.hb.cY), 0.05f);
            dmg(m, AbstractGameAction.AttackEffect.NONE);
            applyToEnemy(m, new Maledict(m, magicNumber));
        }
    }

    @Override
    public void upp() {
        super.upp();
        isMultiDamage = true;
        target = CardTarget.ALL_ENEMY;
    }
}