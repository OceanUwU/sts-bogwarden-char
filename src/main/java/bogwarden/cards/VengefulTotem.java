package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class VengefulTotem extends AbstractTrapCard {
    public final static String ID = makeID("VengefulTotem");

    public VengefulTotem() {
        super(ID, CardRarity.UNCOMMON);
        setDamage(7);
        setMagic(1, +1);
        setExhaust(true);
        cardsToPreview = new Blast();
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < (isEliteOrBoss() ? 1 + magicNumber : 1); i++) {
            att(new MakeTempCardInHandAction(cardsToPreview, 1));
            dmgTop(m, AbstractGameAction.AttackEffect.LIGHTNING);
        }
    }
}