package bogwarden.util;

import bogwarden.BogMod;
import bogwarden.characters.TheBogwarden;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.Arrays;
import java.util.List;
import skindex.itemtypes.CustomizableItem;
import skindex.registering.SkindexPlayerSkinRegistrant;
import skindex.registering.SkindexRegistry;
import skindex.skins.player.PlayerAtlasSkin;
import skindex.skins.player.PlayerAtlasSkinData;
import skindex.skins.player.PlayerSkin;
import skindex.unlockmethods.FreeUnlockMethod;

import static bogwarden.BogMod.makeID;

public class Skindexer implements SkindexPlayerSkinRegistrant {    
    public static void register() {
        SkindexRegistry.subscribe(new Skindexer());
    }
    
    public List<PlayerSkin> getDefaultPlayerSkinsToRegister() {
        return Arrays.asList(new BogSkin(makeID("skinBase")));
    }

    public List<PlayerSkin> getPlayerSkinsToRegister() {
        return Arrays.asList(
            new BogSkin(makeID("blue")),
            new BogSkin(makeID("red")),
            new BogSkin(makeID("yellow")),
            new BogSkin(makeID("ghost"))
        );
    }

    public static class BogSkin extends PlayerAtlasSkin {
        public BogSkin(String id) {
            super(new BogSkinData(id));
        }

        @Override
        public CustomizableItem makeCopy() {
            return new BogSkin(id);
        }

        @Override
        public boolean loadOnPlayer() {
            if (AbstractDungeon.player != null && AbstractDungeon.player instanceof TheBogwarden)
                ((TheBogwarden)AbstractDungeon.player).setupAnimation(id.equals(makeID("skinBase")) ? "mainChar" : "mainChar/skins/"+id.replace(BogMod.modID + ":", ""));
            return super.loadOnPlayer();
        }

        private static class BogSkinData extends PlayerAtlasSkinData {
            private static String skinResourceFolder = BogMod.modID + "Resources/images/char/mainChar/skins/";

            public BogSkinData(String id) {
                skeletonUrl = BogMod.modID + "Resources/images/char/mainChar/bogwarden.json";
                if (id.equals(makeID("skinBase"))) {
                    atlasUrl = BogMod.modID + "Resources/images/char/mainChar/bogwarden.atlas";
                    resourceDirectoryUrl = BogMod.modID + "Resources/images/char/mainChar/";
                } else {
                    String path = id.replace(BogMod.modID + ":", "");
                    if (Gdx.files.internal(skinResourceFolder + path + "/bogwarden.json").exists())
                        skeletonUrl = skinResourceFolder + path + "/bogwarden.json";
                    atlasUrl = skinResourceFolder + path + "/bogwarden.atlas";
                    resourceDirectoryUrl = skinResourceFolder + path + "/";
                }
                defaultAnimName = "idle";

                this.id = id;
                name = id; //CardCrawlGame.languagePack.getUIString(id).TEXT[0];
                this.scale = TheBogwarden.SIZE_SCALE;

                unlockMethod = FreeUnlockMethod.methodId;
                playerClass = TheBogwarden.Enums.THE_BOGWARDEN_OCEAN.name();
            }
        }
    }
}