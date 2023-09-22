package bogwarden.patches;

import bogwarden.powers.Maledict;
import bogwarden.powers.Mojo;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;

public class NonAttackDamagePatches {
    @SpirePatch(clz=DamageInfo.class, method=SpirePatch.CLASS)
    public static class DamageInfoFields {
        public static SpireField<Boolean> fromCard = new SpireField<>(() -> false);
        public static SpireField<Boolean> fromVenom = new SpireField<>(() -> false);
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
}