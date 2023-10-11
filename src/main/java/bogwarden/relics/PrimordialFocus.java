package bogwarden.relics;

import basemod.helpers.CardPowerTip;
import bogwarden.cards.Blast;
import bogwarden.characters.TheBogwarden;
import bogwarden.vfx.IncantationEffect;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class PrimordialFocus extends AbstractBogRelic {
    public static final String ID = makeID("PrimordialFocus");
    private static final int BLASTS = 2;

    public PrimordialFocus() {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
        Blast blast = new Blast();
        blast.upgrade();
        tips.add(new CardPowerTip(blast));
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + BLASTS + DESCRIPTIONS[1];
    }
  
    public void atBattleStartPreDraw() {
        flash();
        atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        vfx(new IncantationEffect());
        Blast blast = new Blast();
        blast.upgrade();
        atb(new MakeTempCardInHandAction(blast, BLASTS, false));
    }
}