package bogwarden.cards;

import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class WardingTotem extends AbstractTrapCard {
    public final static String ID = makeID("WardingTotem");

    public WardingTotem() {
        super(ID, CardRarity.UNCOMMON);
        setMagic(3, +1);
        setSecondMagic(1);
        setExhaust(true);
        sfx = BogAudio.TOTEM_TRIGGER;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new PlatedArmorPower(p, magicNumber));
        applyToSelf(new ArtifactPower(p, secondMagic));
    }
}