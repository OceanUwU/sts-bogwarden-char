package bogwarden.patches;

import bogwarden.actions.TriggerTrapAction;
import com.evacipated.cardcrawl.mod.stslib.patches.tempHp.PlayerDamage;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CtBehavior;

import static bogwarden.util.Wiz.*;

public class TrapPatches {
    private static boolean triggeredThisDamage = false;

    @SpirePatch(clz=AbstractPlayer.class, method="damage")
    public static class TriggerOnDamage {
        public static void Prefix() {
            triggeredThisDamage = false;
        }

        @SpireInsertPatch(rloc=101)
        public static void Insert(AbstractPlayer __instance, DamageInfo info) {
            if (!triggeredThisDamage) {
                triggeredThisDamage = true;
                att(new TriggerTrapAction(info.owner));
            }
        }
    }

    
    @SpirePatch(clz=PlayerDamage.class, method="Insert", paramtypez={AbstractCreature.class, DamageInfo.class, int[].class, boolean[].class})
    public static class TriggerOnTempHPLoss {
        @SpireInsertPatch(locator=Locator.class)
        public static void Insert(AbstractCreature __instance, DamageInfo info) {
            if (!triggeredThisDamage) {
                triggeredThisDamage = true;
                att(new TriggerTrapAction(info.owner));
            }
        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                return LineFinder.findInOrder(ctMethodToPatch, new Matcher.FieldAccessMatcher(AbstractCreature.class, "powers"));
            }
        }
    }
}