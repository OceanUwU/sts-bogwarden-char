package bogwarden.patches;

import bogwarden.powers.Maledict;
import bogwarden.powers.Mojo;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

public class NonAttackDamagePatches {
    @SpirePatch(clz=DamageInfo.class, method=SpirePatch.CLASS)
    public static class DamageInfoFields {
        public static SpireField<Boolean> fromCard = new SpireField<>(() -> false);
        public static SpireField<Boolean> fromVenom = new SpireField<>(() -> false);
    }

    @SpirePatch(clz=DamageAllEnemiesAction.class, method=SpirePatch.CLASS)
    public static class DamageAllFields {
        public static SpireField<Boolean> fromCard = new SpireField<>(() -> false);
    }

    @SpirePatch(clz=AbstractMonster.class, method="damage")
    public static class Increase {
        public static void Prefix(AbstractMonster m, DamageInfo info) {
            if (AbstractDungeon.player != null
            && (info.owner == null || info.owner == AbstractDungeon.player)
            && !DamageInfoFields.fromCard.get(info)
            && !m.hasPower(IntangiblePower.POWER_ID) && !m.hasPower(IntangiblePlayerPower.POWER_ID)
            && info.type != DamageInfo.DamageType.NORMAL) {
                AbstractPower mojo = AbstractDungeon.player.getPower(Mojo.POWER_ID);
                if (mojo != null)
                    info.output += mojo.amount;
                AbstractPower maledict = m.getPower(Maledict.POWER_ID);
                if (maledict != null)
                    info.output *= ((Maledict)maledict).mult();
            }
        }
    }

    private static boolean fromCard = false;
    @SpirePatch(clz=DamageAllEnemiesAction.class, method="update")
    public static class AllEnemiesFromCardPatch {
        @SpireInsertPatch(rloc=48)
        public static void Insert(DamageAllEnemiesAction __instance) {
            if (DamageAllFields.fromCard.get(__instance))
                fromCard = true;
        }
    }

    @SpirePatch(clz=DamageInfo.class, method=SpirePatch.CONSTRUCTOR, paramtypez={AbstractCreature.class, int.class, DamageInfo.DamageType.class})
    public static class DamageInfoAllEnemiesFromCardPatch {
        public static void Postfix(DamageInfo __instance) {
            if (fromCard) {
                DamageInfoFields.fromCard.set(__instance, true);
                fromCard = false;
            }
        }
    }

    @SpirePatch(clz=ExplosivePotion.class, method="use")
    public static class ExplosivePotionUsesThornsDamage {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(NewExpr e) throws CannotCompileException {
                    if (e.getClassName().equals(DamageAllEnemiesAction.class.getName()))
                        e.replace("$_ = new "+DamageAllEnemiesAction.class.getName()+"($1, $2, "+DamageInfo.DamageType.class.getName()+".THORNS, $4);");
                }
            };
        }
    }
}