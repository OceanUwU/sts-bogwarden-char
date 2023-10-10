package bogwarden.multiplayer;

import basemod.ReflectionHacks;
import bogwarden.BogMod;
import bogwarden.characters.TheBogwarden;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import spireTogether.SpireTogetherMod;
import spireTogether.Unlockable;
import spireTogether.modcompat.generic.energyorbs.CustomizableEnergyOrbCustom;
import spireTogether.monsters.CharacterEntity;
import spireTogether.monsters.playerChars.NetworkCharPreset;
import spireTogether.skins.PlayerSkin;
import spireTogether.ui.elements.presets.Nameplate;
import spireTogether.util.BundleManager;
import spireTogether.util.DevConfig;

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
        lobbyScale = 0.5f;
    }

    public String GetThreeLetterID() {
        return "BOG";
    }

    public void GetSkins() {
        skins.add(GetDefaultSkin());
        skins.add(new BogwardenSkin("RED", Unlockable.UnlockMethod.FREE, playerClass));
        skins.add(new BogwardenSkin("BLUE", Unlockable.UnlockMethod.FREE, playerClass));
        skins.add(new BogwardenSkin("YELLOW", Unlockable.UnlockMethod.FREE, playerClass));
        skins.add(GetGhostSkin());
        skins.add(new BogwardenSkin("HEARTSLAYER", Unlockable.UnlockMethod.ACHIEVEMENT, playerClass));
    }
  
    public PlayerSkin GetDefaultSkin() {
        return new BogwardenSkin("BASE", Unlockable.UnlockMethod.FREE, playerClass);
    }

    public PlayerSkin GetGhostSkin() {
        return new BogwardenSkin("GHOST", Unlockable.UnlockMethod.ACHIEVEMENT, playerClass).SetBundles(BundleManager.GHOST);
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

    @Override
    public void loadAnimation(String atlasUrl, String skeletonUrl, float scale) {
        atlas = new TextureAtlas(Gdx.files.internal(atlasUrl));
        SkeletonJson json = new SkeletonJson(this.atlas);
        json.setScale(Settings.renderScale / scale);
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal(skeletonUrl));
        skeleton = new Skeleton(skeletonData);
        skeleton.setColor(Color.WHITE);
        stateData = new AnimationStateData(skeletonData);
        state = new AnimationState(this.stateData);

        ReflectionHacks.setPrivate(source, AbstractCreature.class, "skeleton", skeleton);
        ReflectionHacks.setPrivate(source, AbstractCreature.class, "stateData", stateData);
        source.state = new AnimationState(ReflectionHacks.getPrivate(source, AbstractCreature.class, "stateData"));

        AnimationState.TrackEntry track = setStateAnimation(0, "idle", true);
        track.setTimeScale(TheBogwarden.ANIMATION_SPEED);
        setStateDataMix("hit", "idle", 0.5f);
    }
  
    @SpirePatch(clz=SpireTogetherMod.class, method="RegisterModdedChars", requiredModId="spireTogether")
    public static class Register {
        public static void Postfix() {
            SpireTogetherMod.allCharacterEntities.put(TheBogwarden.Enums.THE_BOGWARDEN_OCEAN, new NetworkBogwarden());
            DevConfig.isDeveloper = true; //TODO: remove
        }
    }
}