package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.LoseMojoPower;
import bogwarden.powers.Mojo;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SwampTalisman extends AbstractBogRelic {
    public static final String ID = makeID("SwampTalisman");
    public static final int DRAW = 1;
    public static final int MOJO = 2;

    public SwampTalisman() {
        super(ID, RelicTier.STARTER, LandingSound.MAGICAL, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DRAW + DESCRIPTIONS[1] + MOJO + DESCRIPTIONS[2];
    }
  
    public void atBattleStart() {
        counter = 0;
    }
    
    public void atTurnStart() {
        if (!grayscale)
            if (++counter == 2) {
                flash();
                atb(new RelicAboveCreatureAction(adp(), this));
                applyToSelf(new Mojo(adp(), MOJO));
                applyToSelf(new LoseMojoPower(adp(), MOJO));
            }
    }
    
    public void atTurnStartPostDraw() {
        if (!grayscale)
            if (counter == 2) {
                atb(new DrawCardAction(DRAW));
                counter = -1;
                grayscale = true;
            }
    }
    
    public void onVictory() {
        counter = -1;
        grayscale = false;
    }
}
