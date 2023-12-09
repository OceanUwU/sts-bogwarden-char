package bogwarden.charbosses.bosses;

import bogwarden.cards.DeathWard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import downfall.downfallMod;
import downfall.powers.neowpowers.TrueNeowPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class NeowBogPower {
    private static boolean facedBogwarden;
    private static final String DESCRIPTIONS[] = CardCrawlGame.languagePack.getPowerStrings(makeID("NeowSpiritBogwarden")).DESCRIPTIONS;
    private static final int AMOUNT = 2;

    @SpirePatch(clz=TrueNeowPower.class, method=SpirePatch.CONSTRUCTOR, requiredModId="downfall")
    public static class OnCreate {
        public static void Postfix(TrueNeowPower __instance) {
            facedBogwarden = downfallMod.Act1BossFaced.equals(CharBossBogwarden.ID) || downfallMod.Act2BossFaced.equals(CharBossBogwarden.ID) || downfallMod.Act3BossFaced.equals(CharBossBogwarden.ID);
            __instance.updateDescription();
        }
    }

    @SpirePatch(clz=TrueNeowPower.class, method="onSpecificTrigger", requiredModId="downfall")
    public static class DoEffect {
        public static void Prefix(TrueNeowPower __instance) {
            if (facedBogwarden)
                atb(new ApplyPowerAction(__instance.owner, __instance.owner, new DeathWard.DeathWardPower(__instance.owner, AMOUNT)));
        }
    }

    @SpirePatch(clz=TrueNeowPower.class, method="updateDescription", requiredModId="downfall")
    public static class AddDescription {
        public static void Postfix(TrueNeowPower __instance) {
            if (facedBogwarden)
                __instance.description += DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
        }
    }
}