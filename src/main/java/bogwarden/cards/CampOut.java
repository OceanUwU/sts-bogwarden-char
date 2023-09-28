package bogwarden.cards;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;

public class CampOut extends AbstractBogCard {
    public final static String ID = makeID("CampOut");

    public CampOut() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        setExhaust(true);
        tags.add(CardTags.HEALING);
        MultiCardPreview.add(this, new Bonfire(), new Forge());
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> stanceChoices = new ArrayList<>();
        stanceChoices.add(new Bonfire());
        stanceChoices.add(new Forge());
        if (upgraded)
            for (AbstractCard c : stanceChoices)
                c.upgrade();
        atb(new ChooseOneAction(stanceChoices));
    }

    @Override
    public void upp() {
        super.upp();
        for (AbstractCard c : MultiCardPreview.multiCardPreview.get(this))
            c.upgrade();
    }
}