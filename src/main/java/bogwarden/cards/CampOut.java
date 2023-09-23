package bogwarden.cards;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class CampOut extends AbstractBogCard {
    public final static String ID = makeID("CampOut");

    public CampOut() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        setMagic(4, +1);
        setSecondMagic(2, +1);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
        stanceChoices.add(new Bonfire());
        stanceChoices.add(new Forge());
        if (upgraded)
            for (AbstractCard c : stanceChoices)
                c.upgrade();
        stanceChoices.get(0).baseMagicNumber = magicNumber;
        stanceChoices.get(0).magicNumber = stanceChoices.get(0).baseMagicNumber;
        stanceChoices.get(1).baseMagicNumber = secondMagic;
        stanceChoices.get(1).magicNumber = stanceChoices.get(1).baseMagicNumber;
        atb(new ChooseOneAction(stanceChoices));
    }
}