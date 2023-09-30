package bogwarden.cards;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import bogwarden.powers.Maledict;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class VengefulTotem extends AbstractTrapCard {
    public final static String ID = makeID("VengefulTotem");

    public VengefulTotem() {
        super(ID, CardRarity.UNCOMMON);
        setMagic(2);
        setSecondMagic(1, +1);
        setExhaust(true);
        Blast blastUp = new Blast();
        blastUp.upgrade();
        MultiCardPreview.add(this, new Blast(), blastUp);
        sfx = BogAudio.TOTEM_TRIGGER;
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        AbstractCard c = new Blast();
        if (isEliteOrBoss())
            c.upgrade();
        att(new MakeTempCardInHandAction(c, secondMagic));
        applyToEnemyTop(m, new Maledict(m, magicNumber));
    }
}