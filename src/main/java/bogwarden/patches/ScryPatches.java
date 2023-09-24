package bogwarden.patches;

import basemod.BaseMod;
import basemod.interfaces.OnPlayerTurnStartSubscriber;
import bogwarden.cards.AbstractBogCard;
import bogwarden.cards.WildMagic;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.CardGroup;
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

    @SpirePatch(clz=ScryAction.class, method=SpirePatch.CLASS)
    public static class ActionFields {
        public static SpireField<Boolean> fromWildMagic = new SpireField<>(() -> false);
        public static SpireField<Boolean> upgraded = new SpireField<>(() -> false);
        public static SpireField<CardGroup> group = new SpireField<>(() -> null);
    }

    @SpirePatch(clz=ScryAction.class, method="update")
    public static class WildMagicTransform {
        @SpireInsertPatch(rloc=24, localvars={"tmpGroup"})
        public static void Insert(ScryAction __instance, CardGroup tmpGroup) {
            ActionFields.group.set(__instance, tmpGroup);
        }

        public static void Postfix(ScryAction __instance) {
            if (__instance.isDone && ActionFields.fromWildMagic.get(__instance)) {
                if (ActionFields.group.get(__instance) != null)
                    WildMagic.transform(ActionFields.group.get(__instance), ActionFields.upgraded.get(__instance));
            }
        }
    }
}