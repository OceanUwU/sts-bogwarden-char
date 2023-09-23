package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Minefield extends AbstractTrapCard {
    public final static String ID = makeID("Minefield");
  
    public Minefield() {
        this(0);
    }

    public Minefield(int upgrades) {
        super(ID, CardRarity.UNCOMMON);
        baseDamage = 7;
        timesUpgraded = upgrades;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        dmgTop(m, AbstractGameAction.AttackEffect.NONE);
        att(new VFXAction(new ExplosionSmallEffect(m.hb.cX, m.hb.cY), 0.1F));
    }
  
    public void upgrade() {
        upgradeDamage(4 + timesUpgraded);
        timesUpgraded++;
        upgraded = true;
        name = cardStrings.NAME + "+" + timesUpgraded;
        initializeTitle();
    }
  
    public AbstractCard makeCopy() {
        return new Minefield(this.timesUpgraded);
    }
}