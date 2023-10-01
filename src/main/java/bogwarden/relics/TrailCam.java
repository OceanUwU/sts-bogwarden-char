package bogwarden.relics;

import bogwarden.actions.TriggerTrapAction;
import bogwarden.cards.AbstractTrapCard;
import bogwarden.characters.TheBogwarden;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class TrailCam extends AbstractBogRelic {
    public static final String ID = makeID("TrailCam");
    private static int DRAWN = 1;

    public TrailCam() {
        super(ID, RelicTier.UNCOMMON, LandingSound.HEAVY, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAWN + DESCRIPTIONS[1];
    }

    public void onCardDraw(AbstractCard drawnCard) {
        if (!grayscale && (drawnCard instanceof AbstractTrapCard)) {
            flash();
            atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            atb(new TriggerTrapAction());
            atb(new DrawCardAction(DRAWN));
            grayscale = true;
        }
    }
    
    public void onVictory() {
        grayscale = false;
    }
}