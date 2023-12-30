package bogwarden.relics;

import basemod.helpers.CardPowerTip;
import bogwarden.cards.BackfiringTrap;
import bogwarden.characters.TheBogwarden;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;

public class RustyOlClunker extends AbstractBogRelic {
    public static final String ID = makeID("RustyOlClunker");

    public RustyOlClunker() {
        super(ID, RelicTier.BOSS, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
        tips.add(new CardPowerTip(new BackfiringTrap()));
    }

    @Override
    public void playLandingSFX() {
        CardCrawlGame.sound.play(BogAudio.TRAP_TRIGGER);
    }
  
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new MakeTempCardInDrawPileAction(new BackfiringTrap(), 1, true, true));
    }
    
    public void onEquip() {
        AbstractDungeon.player.energy.energyMaster++;
    }
    
    public void onUnequip() {
        AbstractDungeon.player.energy.energyMaster--;
    }
}