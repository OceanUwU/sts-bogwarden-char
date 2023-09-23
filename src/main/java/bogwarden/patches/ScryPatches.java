package bogwarden.patches;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerTurnStartSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.ScryAction;

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
}