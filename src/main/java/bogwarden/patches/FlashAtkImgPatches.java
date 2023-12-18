package bogwarden.patches;

import basemod.ReflectionHacks;
import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.DamageHeartEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static bogwarden.BogMod.makeImagePath;

public class FlashAtkImgPatches {
    @SpireEnum
    public static AbstractGameAction.AttackEffect BOGWARDEN_BLAST_EFFECT;
    private static TextureAtlas.AtlasRegion BLAST_IMAGE;

    @SpireEnum
    public static AbstractGameAction.AttackEffect BOGWARDEN_REFINED_BLAST_EFFECT;
    private static TextureAtlas.AtlasRegion REFINED_BLAST_IMAGE;

    @SpireEnum
    public static AbstractGameAction.AttackEffect BOGWARDEN_WILD_MAGIC_EFFECT;
    private static TextureAtlas.AtlasRegion WILD_MAGIC_IMAGE;

    private static boolean effectIs(Object obj, AbstractGameAction.AttackEffect ...effects) {
        AbstractGameAction.AttackEffect effect = ((AbstractGameAction.AttackEffect)ReflectionHacks.getPrivate(obj, obj instanceof DamageHeartEffect ? DamageHeartEffect.class : FlashAtkImgEffect.class, "effect"));
        for (AbstractGameAction.AttackEffect e : effects)
            if (effect.equals(e)) return true;
        return false;
    }

    @SpirePatch(clz=FlashAtkImgEffect.class, method="loadImage")
    @SpirePatch(clz=DamageHeartEffect.class, method="loadImage")
    public static class Image {
        public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(Object __instance) {
            if (effectIs(__instance, BOGWARDEN_BLAST_EFFECT)) {
                if (BLAST_IMAGE == null)
                    BLAST_IMAGE = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/blast.png")), 0, 0, 512, 512);
                return SpireReturn.Return(BLAST_IMAGE);
            } else if (effectIs(__instance, BOGWARDEN_REFINED_BLAST_EFFECT)) {
                if (REFINED_BLAST_IMAGE == null)
                    REFINED_BLAST_IMAGE = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/refinedblast.png")), 0, 0, 512, 512);
                return SpireReturn.Return(REFINED_BLAST_IMAGE);
            } else if (effectIs(__instance, BOGWARDEN_WILD_MAGIC_EFFECT)) {
                if (WILD_MAGIC_IMAGE == null)
                    WILD_MAGIC_IMAGE = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/wildmagic.png")), 0, 0, 512, 512);
                return SpireReturn.Return(WILD_MAGIC_IMAGE);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz=FlashAtkImgEffect.class, method="playSound")
    @SpirePatch(clz=DamageHeartEffect.class, method="playSound")
    public static class Audio {
        public static SpireReturn<Void> Prefix(Object __instance) {
            if (effectIs(__instance, BOGWARDEN_BLAST_EFFECT, BOGWARDEN_WILD_MAGIC_EFFECT)) {
                CardCrawlGame.sound.play(BogAudio.BLAST);
                return SpireReturn.Return();
            } else if (effectIs(__instance, BOGWARDEN_REFINED_BLAST_EFFECT)) {
                CardCrawlGame.sound.play(BogAudio.REFINED_BLAST);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}