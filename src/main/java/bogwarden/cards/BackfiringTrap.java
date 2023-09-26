package bogwarden.cards;

import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BackfiringTrap extends AbstractTrapCard {
    public final static String ID = makeID("BackfiringTrap");

    public BackfiringTrap() {
        super(ID, CardRarity.SPECIAL);
        type = CardType.CURSE;
        color = CardColor.CURSE;
        setMagic(6);
        sfx = BogAudio.BACKFIRE_TRIGGER;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        att(new LoseHPAction(p, p, magicNumber));
    }
}