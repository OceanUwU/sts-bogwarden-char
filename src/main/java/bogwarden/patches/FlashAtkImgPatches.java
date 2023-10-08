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
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static bogwarden.BogMod.makeImagePath;

public class FlashAtkImgPatches {
    @SpireEnum
    public static AbstractGameAction.AttackEffect BOGWARDEN_BLAST_EFFECT;
    private static TextureAtlas.AtlasRegion BLAST_IMAGE;

    @SpireEnum
    public static AbstractGameAction.AttackEffect BOGWARDEN_WILD_MAGIC_EFFECT;
    private static TextureAtlas.AtlasRegion WILD_MAGIC_IMAGE;

    @SpirePatch(clz=FlashAtkImgEffect.class, method="loadImage")
    public static class Image {
        public static SpireReturn<TextureAtlas.AtlasRegion> Prefix(FlashAtkImgEffect __instance) {
            if (((AbstractGameAction.AttackEffect)ReflectionHacks.getPrivate(__instance, FlashAtkImgEffect.class, "effect")).equals(BOGWARDEN_BLAST_EFFECT)) {
                if (BLAST_IMAGE == null)
                    BLAST_IMAGE = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/blast.png")), 0, 0, 512, 512);
                return SpireReturn.Return(BLAST_IMAGE);
            } else if (((AbstractGameAction.AttackEffect)ReflectionHacks.getPrivate(__instance, FlashAtkImgEffect.class, "effect")).equals(BOGWARDEN_WILD_MAGIC_EFFECT)) {
                if (WILD_MAGIC_IMAGE == null)
                    WILD_MAGIC_IMAGE = new TextureAtlas.AtlasRegion(TexLoader.getTexture(makeImagePath("vfx/wildmagic.png")), 0, 0, 512, 512);
                return SpireReturn.Return(WILD_MAGIC_IMAGE);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz=FlashAtkImgEffect.class, method="playSound")
    public static class Audio {
        public static SpireReturn<Void> Prefix(FlashAtkImgEffect __instance) {
            if (((AbstractGameAction.AttackEffect)ReflectionHacks.getPrivate(__instance, FlashAtkImgEffect.class, "effect")).equals(BOGWARDEN_BLAST_EFFECT)
                || ((AbstractGameAction.AttackEffect)ReflectionHacks.getPrivate(__instance, FlashAtkImgEffect.class, "effect")).equals(BOGWARDEN_WILD_MAGIC_EFFECT))
            {
                CardCrawlGame.sound.play(BogAudio.BLAST);
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}