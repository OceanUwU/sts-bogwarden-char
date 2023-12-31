package bogwarden.multiplayer;

import basemod.ReflectionHacks;
import bogwarden.BogMod;
import bogwarden.characters.TheBogwarden;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import skindex.registering.SkindexRegistry;
import skindex.skins.player.PlayerSkin;
import spireTogether.SpireTogetherMod;
import spireTogether.modcompat.generic.energyorbs.CustomizableEnergyOrbCustom;
import spireTogether.monsters.CharacterEntity;
import spireTogether.monsters.playerChars.NetworkCharPreset;
import spireTogether.ui.elements.presets.Nameplate;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;

public class NetworkBogwarden extends NetworkCharPreset {
    private static final float ORB_SCALE = ReflectionHacks.getPrivateStatic(CustomizableEnergyOrbCustom.class, "ORB_IMG_SCALE");

    public NetworkBogwarden() {
        super(new TheBogwarden(TheBogwarden.characterStrings.NAMES[1], TheBogwarden.Enums.THE_BOGWARDEN_OCEAN));
        energyOrb = new CustomizableEnergyOrbCustom(TheBogwarden.BogEnergyOrb.orbTextures, makeImagePath("char/mainChar/orb/vfx.png"), TheBogwarden.BogEnergyOrb.rotationValues) {
            @Override
            public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
                sb.setColor(Color.WHITE);
                if (enabled)
                    for(int i = 0; i < noEnergyLayers.length; ++i)
                        sb.draw(energyLayers[i], current_x - 64f, current_y - 64f, 64.0F, 64.0F, 128.0F, 128.0F, ORB_SCALE, ORB_SCALE, angles[i], 0, 0, 128, 128, false, false);
                else
                    for(int i = 0; i < noEnergyLayers.length; ++i)
                        sb.draw(noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_SCALE, ORB_SCALE, angles[i], 0, 0, 128, 128, false, false);
                sb.draw(baseLayer, current_x - 128f, current_y - 128f, 128f, 128f, 256f, 256f, ORB_SCALE, ORB_SCALE, 0.0F, 0, 0, 256, 256, false, false);
            }
        };
        loadAnimation(makeImagePath("char/mainChar/bogwarden.atlas"), makeImagePath("char/mainChar/bogwarden.json"), 1f);
        lobbyScale = 1.6f;
    }

    public String GetThreeLetterID() {
        return "BOG";
    }

    public PlayerSkin GetGhostSkin() {
        return SkindexRegistry.getPlayerSkinByClassAndId(playerClass, makeID("ghost"));
    }

    public CharacterEntity CreateNew() {
        return new NetworkBogwarden();
    }

    public Texture GetNameplateIcon(String s) {
        return TexLoader.getTexture(makeImagePath("char/multiplayer/icons/"+s+".png"));
    }

    public Texture GetDefaultIcon() {
        return GetNameplateIcon("basic");
    }

    public Texture GetWhiteSpecialIcon() {
        return GetNameplateIcon("whiteSpecial");
    }

    public Nameplate GetNameplateUnlock() {
        return null;
    }

    public Color GetCharColor() {
        return BogMod.characterColor.cpy();
    }
  
    @SpirePatch(clz=SpireTogetherMod.class, method="RegisterModdedChars", requiredModId="spireTogether")
    public static class Register {
        public static void Postfix() {
            SpireTogetherMod.allCharacterEntities.put(TheBogwarden.Enums.THE_BOGWARDEN_OCEAN, new NetworkBogwarden());
        }
    }
}