package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Tripwire extends AbstractTrapCard {
    public final static String ID = makeID("Tripwire");

    public Tripwire() {
        super(ID, CardRarity.COMMON);
        setBlock(6, +3);
        setMagic(1);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLivingTop(mo -> {
            if (!mo.hasPower(ArtifactPower.POWER_ID))
                applyToEnemyTop(mo, new GainStrengthPower(mo, magicNumber));
            applyToEnemyTop(mo, new StrengthPower(mo, -magicNumber));
        });
        blckTop();
    }
}