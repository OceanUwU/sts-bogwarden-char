package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Maledict;

import static bogwarden.BogMod.makeID;

public class PaperCrokh extends AbstractBogRelic {
    public static final String ID = makeID("PaperCrokh");

    public PaperCrokh() {
        super(ID, RelicTier.RARE, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + (int)(Maledict.CROKH_MULT * 100 - 100) + DESCRIPTIONS[1] + (int)(Maledict.MULT * 100 - 100) + DESCRIPTIONS[2];
    }
}