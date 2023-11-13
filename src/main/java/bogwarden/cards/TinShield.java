package bogwarden.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TinShield extends AbstractTrapCard {
    public final static String ID = makeID("TinShield");

    public TinShield() {
        super(ID, CardRarity.SPECIAL);
        color = CardColor.COLORLESS;
        setBlock(30, +10);
        setExhaust(true);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelfTop(new NextTurnBlockPower(p, block));
    }
}