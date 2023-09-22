package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SwampTalisman extends AbstractBogRelic {
    public static final String ID = makeID("SwampTalisman");
    public static final int TRIGGERS = 3;
    public static final int DRAW = 1;

    public SwampTalisman() {
        super(ID, RelicTier.STARTER, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TRIGGERS + DESCRIPTIONS[1] + DRAW + DESCRIPTIONS[2];
    }

    public void atBattleStart() {
        counter = TRIGGERS;
    }

    public void onCardDraw(AbstractCard drawnCard) {
        if (counter > 0 && drawnCard.cost == -2) {
            flash();
            atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            atb(new DrawCardAction(DRAW));
            if (--counter <= 0) {
                counter = -1;
                grayscale = true;
            }
        }
    }

    public void onVictory() {
        counter = -1;
        grayscale = false;
    }
}
