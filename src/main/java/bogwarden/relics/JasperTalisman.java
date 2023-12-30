package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Mojo;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.apache.commons.lang3.StringUtils;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class JasperTalisman extends AbstractBogRelic {
    public static final String ID = makeID("JasperTalisman");
    private static final int TRIGGERS = 3;
    private static final int DRAW = 1;
    private static final int MOJO = 1;

    public JasperTalisman() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    @Override
    public void playLandingSFX() {
        CardCrawlGame.sound.play(BogAudio.MOJO);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TRIGGERS + DESCRIPTIONS[1] + DRAW + DESCRIPTIONS[2] + MOJO + DESCRIPTIONS[3];
    }
  
    public void atBattleStart() {
        counter = TRIGGERS;
    }

    public void onCardDraw(AbstractCard drawnCard) {
        if (counter > 0 && drawnCard.cost == -2) {
            flash();
            atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            atb(new DrawCardAction(DRAW));
            applyToSelf(new Mojo(adp(), MOJO));
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
    
    public void obtain() {
        if (AbstractDungeon.player.hasRelic(SwampTalisman.ID)) {
            for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i)
                if (StringUtils.equals(AbstractDungeon.player.relics.get(i).relicId, SwampTalisman.ID)) {
                    instantObtain(AbstractDungeon.player, i, true);
                    break;
                }
        } else
            super.obtain();
    }
  
    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic(SwampTalisman.ID);
    }
}
