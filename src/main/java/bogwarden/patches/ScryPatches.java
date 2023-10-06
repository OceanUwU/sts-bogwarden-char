package bogwarden.patches;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerTurnStartSubscriber;
import bogwarden.cards.AbstractBogCard;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpireInitializer
public class ScryPatches {
    public static void initialize() {
        BaseMod.subscribe(new ScriedThisTurn());
    }

    @SpirePatch(clz=ScryAction.class, method="update")
    public static class ScriedThisTurn implements OnPlayerTurnStartSubscriber {
        public static boolean yep = false;

        public static void Prefix() {
            yep = true;
        }

        @Override
        public void receiveOnPlayerTurnStart() {
            yep = false;
        }
    }

    @SpirePatch(clz=ScryAction.class, method="update")
    public static class OnDiscarded {
        @SpireInsertPatch(rloc=27)
        public static void Insert() {
            AbstractDungeon.gridSelectScreen.selectedCards.stream().filter(c -> c instanceof AbstractBogCard).forEach(c -> ((AbstractBogCard)c).onDiscardedViaScry());
        }
    }
}