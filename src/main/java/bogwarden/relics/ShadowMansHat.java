package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Mojo;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class ShadowMansHat extends AbstractBogRelic {
    public static final String ID = makeID("ShadowMansHat");
    private static final int MOJO = 1;

    public ShadowMansHat() {
        super(ID, RelicTier.COMMON, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    @Override
    public void playLandingSFX() {
        CardCrawlGame.sound.play(BogAudio.MALEDICT);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MOJO + DESCRIPTIONS[1];
    }
  
    public void atBattleStart() {
        flash();
        atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        applyToSelf(new Mojo(adp(), MOJO));
    }
}