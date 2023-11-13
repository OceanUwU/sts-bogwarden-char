/*package bogwarden.multiplayer;

import basemod.ReflectionHacks;
import bogwarden.characters.TheBogwarden;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireTogether.Unlockable;
import spireTogether.monsters.CharacterEntity;
import spireTogether.skins.AtlasSkin;

import static bogwarden.BogMod.makeImagePath;

public class BogwardenSkin extends AtlasSkin {
    public BogwardenSkin(String ID, Unlockable.UnlockMethod skinType, AbstractPlayer.PlayerClass playerClass) {
        super(ID, makeImagePath("char/multiplayer"), skinType, playerClass);
        scale = TheBogwarden.SIZE_SCALE;
        idleAnimName = "idle";
    }

    public void LoadResources() {
        super.LoadResources();
        shoulderIMG = makeImagePath("char/mainChar/shoulder.png");
        shoulder2IMG = makeImagePath("char/mainChar/shoulder2.png");
        corpseIMG = makeImagePath("char/mainChar/corpse.png");
        jsonLoc = Gdx.files.internal(skinResourceFolder + "bogwarden.json").exists() ? (skinResourceFolder + "bogwarden.json") : makeImagePath("char/mainChar/bogwarden.json");
        atlasLoc = Gdx.files.internal(skinResourceFolder + "bogwarden.atlas").exists() ? (skinResourceFolder + "bogwarden.atlas") : makeImagePath("char/mainChar/bogwarden.atlas");
    }

    public boolean LoadSkin(CharacterEntity e, float scaleMult) {
        e.loadAnimation(atlasLoc, jsonLoc, 1f / scale * scaleMult);
        return true;
    }

    public boolean LoadSkinOnPlayer() {
        if (playerClass.equals(AbstractDungeon.player.chosenClass)) {
            ReflectionHacks.privateMethod(AbstractCreature.class, "loadAnimation", String.class, String.class, float.class).invoke(AbstractDungeon.player, atlasLoc, jsonLoc, 1f / scale);
            ((TheBogwarden)AbstractDungeon.player).setupAnimation();
            return true;
        }
        return false;
    }
}*/