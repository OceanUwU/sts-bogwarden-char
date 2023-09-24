package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Spines;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class PurpleRose extends AbstractBogRelic {
    public static final String ID = makeID("PurpleRose");
    private static final int SPINES = 8;

    public PurpleRose() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + SPINES + DESCRIPTIONS[1];
    }
  
    public void atBattleStart() {
        flash();
        atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        applyToSelf(new Spines(adp(), SPINES));
    }
}