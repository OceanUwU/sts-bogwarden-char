package bogwarden.relics;

import basemod.ReflectionHacks;
import bogwarden.characters.TheBogwarden;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static bogwarden.BogMod.makeID;

public class GrabbyHand extends AbstractBogRelic {
    public static final String ID = makeID("GrabbyHand");

    public GrabbyHand() {
        super(ID, RelicTier.BOSS, LandingSound.MAGICAL, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    @Override
    public void playLandingSFX() {
        CardCrawlGame.sound.play("ORB_LIGHTNING_PASSIVE");
    }

    @SpirePatch(clz=AbstractCard.class, method="applyPowers")
    @SpirePatch(clz=AbstractCard.class, method="calculateCardDamage")
    public static class SetAttacksToNonAttackDamage {
        public static void Prefix(AbstractCard __instance) {
            if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasRelic(ID)
                && __instance.type == AbstractCard.CardType.ATTACK
                && (ReflectionHacks.getPrivate(__instance, AbstractCard.class, "damageType") == DamageInfo.DamageType.NORMAL
                || __instance.damageTypeForTurn == DamageInfo.DamageType.NORMAL)) {
                    ReflectionHacks.setPrivate(__instance, AbstractCard.class, "damageType", DamageInfo.DamageType.THORNS);
                    __instance.damageTypeForTurn = DamageInfo.DamageType.THORNS;
                }
        }
    }
}