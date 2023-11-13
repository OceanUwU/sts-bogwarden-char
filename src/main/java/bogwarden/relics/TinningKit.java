package bogwarden.relics;

import basemod.helpers.CardPowerTip;
import bogwarden.cards.TinShield;
import bogwarden.characters.TheBogwarden;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TinningKit extends AbstractBogRelic {
    public static final String ID = makeID("TinningKit");

    public TinningKit() {
        super(ID, RelicTier.SHOP, LandingSound.SOLID, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(new TinShield()));
    }

    public void onCardDraw(AbstractCard drawnCard) {
        if (!grayscale && (drawnCard.type == AbstractCard.CardType.CURSE || drawnCard.type == AbstractCard.CardType.STATUS)) {
            flash();
            atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            atb(new ExhaustSpecificCardAction(drawnCard, adp().hand, false));
            makeInHand(new TinShield());
            grayscale = true;
        }
    }
  
    public void justEnteredRoom(AbstractRoom room) {
        grayscale = false;
    }
}
