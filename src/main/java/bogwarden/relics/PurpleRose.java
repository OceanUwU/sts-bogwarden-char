package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Spines;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class PurpleRose extends AbstractBogRelic {
    public static final String ID = makeID("PurpleRose");
    private static final int SPINES = 8;

    public PurpleRose() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    @Override
    public void playLandingSFX() {
        CardCrawlGame.sound.play(BogAudio.RUSTLE);
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