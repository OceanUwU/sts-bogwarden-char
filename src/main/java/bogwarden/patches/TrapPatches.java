package bogwarden.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

import static bogwarden.util.Wiz.*;

import bogwarden.actions.TriggerTrapAction;

public class TrapPatches {
    @SpirePatch(clz=AbstractPlayer.class, method="damage")
    public static class TriggerOnDamage {
        @SpireInsertPatch(rloc=101)
        public static void Insert(AbstractPlayer __instance, DamageInfo info) {
            att(new TriggerTrapAction(info.owner));
        }
    }
}