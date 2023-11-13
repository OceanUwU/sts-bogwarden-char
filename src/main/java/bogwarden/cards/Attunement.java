package bogwarden.cards;

import bogwarden.cards.VilePowder.VilePowderEffect;
import bogwarden.powers.Maledict;
import bogwarden.powers.Mojo;
import bogwarden.vfx.MojoFlashEffect;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Attunement extends AbstractBogCard {
    public final static String ID = makeID("Attunement");

    public Attunement() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2);
        setSecondMagic(0, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new MojoFlashEffect(p.hb.cX, p.hb.cY));
        applyToSelf(new Mojo(p, magicNumber));
        if (secondMagic > 0) {
            ArrayList<AbstractMonster> enemies = getEnemies();
            if (enemies.size() > 0)
                vfx(new VilePowderEffect(p.hb, enemies.get(0).hb, enemies.get(enemies.size() - 1).hb), 0.25f);
            forAllMonstersLiving(mo -> applyToEnemy(mo, new Maledict(mo, secondMagic)));
        }
    }
}