package bogwarden.cards;

import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BackfiringTrap extends AbstractTrapCard {
    public final static String ID = makeID("BackfiringTrap");

    public BackfiringTrap() {
        super(ID, CardRarity.SPECIAL);
        type = CardType.STATUS;
        color = CardColor.COLORLESS;
        setMagic(3);
        setExhaust(true);
        sfx = BogAudio.BACKFIRE_TRIGGER;
    }

    public boolean canUpgrade() {
        return false;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        att(new LoseHPAction(p, p, magicNumber));
        vfxTop(new ExplosionSmallEffect(Settings.WIDTH /2f, Settings.HEIGHT / 2f), 0.1f);
    }

    public void upgrade() {}
}