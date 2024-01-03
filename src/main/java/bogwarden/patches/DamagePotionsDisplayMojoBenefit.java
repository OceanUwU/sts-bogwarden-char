package bogwarden.patches;

import bogwarden.powers.Mojo;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase;

import static bogwarden.util.Wiz.*;

@SpirePatch(clz=FirePotion.class, method="initializeData")
@SpirePatch(clz=ExplosivePotion.class, method="initializeData")
public class DamagePotionsDisplayMojoBenefit {
    public static void Postfix(AbstractPotion __instance) {
        int mojo = pwrAmt(adp(), Mojo.POWER_ID);
        if (mojo > 0 && AbstractDungeon.isPlayerInDungeon() && AbstractDungeon.getCurrRoom().phase.equals(RoomPhase.COMBAT)) {
            __instance.description = __instance.description.replace(Integer.toString(__instance.getPotency()), Integer.toString(__instance.getPotency() + mojo)).replace("#b", "#g");
            __instance.tips.clear();
            __instance.tips.add(new PowerTip(__instance.name, __instance.description));
        }
    }
}
