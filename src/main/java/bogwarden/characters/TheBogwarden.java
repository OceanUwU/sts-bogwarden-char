package bogwarden.characters;

import basemod.abstracts.CustomEnergyOrb;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpineAnimation;
import bogwarden.cards.Defend;
import bogwarden.cards.Jinx;
import bogwarden.cards.OthersiderForm;
import bogwarden.cards.ShadowFont;
import bogwarden.cards.SnapperTrap;
import bogwarden.cards.Strike;
import bogwarden.patches.FlashAtkImgPatches;
import bogwarden.relics.SwampTalisman;
import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import bogwarden.vfx.BogwardenVictoryEffect;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import java.util.ArrayList;
import java.util.List;

import static bogwarden.BogMod.*;

public class TheBogwarden extends CustomPlayer {
    static final String ID = makeID("character");
    public static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    public static final String[] NAMES = characterStrings.NAMES;
    static final String[] TEXT = characterStrings.TEXT;
    public static final float SIZE_SCALE = 1.25f;
    public static final Float ANIMATION_SPEED = 1.0F;
    public static final int MAX_HP = 78;

    public String currentSkin;
    public String defaultSkin;

    public TheBogwarden(String name, PlayerClass setClass) {
        super(name, setClass, new BogEnergyOrb(), new SpineAnimation(
                makeCharacterPath("mainChar/bogwarden.atlas"), makeCharacterPath("mainChar/bogwarden.json"), 1f / SIZE_SCALE));
        initializeClass(null,
                SHOULDER1,
                SHOULDER2,
                CORPSE,
                getLoadout(), 0f, -20f, 260f, 260f, new EnergyManager(3));

        setupAnimation("mainChar");
        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 240.0F * Settings.scale);
    }
    
    public void setupAnimation(String folder) {
        currentSkin = folder;
        if (!currentSkin.equals(OthersiderForm.OthersiderTransformationEffect.OTHERSIDER_SKIN))
            defaultSkin = currentSkin;
        loadAnimation(makeCharacterPath(folder+"/bogwarden.atlas"), makeCharacterPath("mainChar/bogwarden.json"), 1f / SIZE_SCALE);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        stateData.setMix("hit", "idle", 0.5F);
        e.setTimeScale(ANIMATION_SPEED);
    }

    public static class BogEnergyOrb extends CustomEnergyOrb {
        private static final float ORB_IMG_SCALE = 1.15F * Settings.scale;
        public static final String[] orbTextures = {
            makeCharacterPath("mainChar/orb/layer1.png"),
            makeCharacterPath("mainChar/orb/layer2.png"),
            makeCharacterPath("mainChar/orb/layer3.png"),
            makeCharacterPath("mainChar/orb/layer4.png"),
            makeCharacterPath("mainChar/orb/layer5.png"),
            makeCharacterPath("mainChar/orb/layer6.png"),
            makeCharacterPath("mainChar/orb/layer1d.png"),
            makeCharacterPath("mainChar/orb/layer2d.png"),
            makeCharacterPath("mainChar/orb/layer3d.png"),
            makeCharacterPath("mainChar/orb/layer4d.png"),
            makeCharacterPath("mainChar/orb/layer5d.png"),
        };
        public static final float[] rotationValues = new float[]{-20f, 20f, -40f, 40f, 0f};

        public BogEnergyOrb() {
            super(orbTextures, makeCharacterPath("mainChar/orb/vfx.png"), rotationValues);
        }

        @Override
        public void renderOrb(SpriteBatch sb, boolean enabled, float current_x, float current_y) {
            sb.setColor(Color.WHITE);
            if (enabled)
                for(int i = 0; i < noEnergyLayers.length; ++i)
                    sb.draw(energyLayers[i], current_x - 64f, current_y - 64f, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angles[i], 0, 0, 128, 128, false, false);
            else
                for(int i = 0; i < noEnergyLayers.length; ++i)
                    sb.draw(noEnergyLayers[i], current_x - 64.0F, current_y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F, ORB_IMG_SCALE, ORB_IMG_SCALE, angles[i], 0, 0, 128, 128, false, false);
            sb.draw(baseLayer, current_x - 128f, current_y - 128f, 128f, 128f, 256f, 256f, ORB_IMG_SCALE, ORB_IMG_SCALE, 0.0F, 0, 0, 256, 256, false, false);
        }
    }

    public Skeleton getSkeleton() {
        return skeleton;
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                MAX_HP, MAX_HP, 0, 99, 5, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            retVal.add(Strike.ID);
        for (int i = 0; i < 4; i++)
            retVal.add(Defend.ID);
        retVal.add(Jinx.ID);
        retVal.add(ShadowFont.ID);
        retVal.add(SnapperTrap.ID);
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(SwampTalisman.ID);
        return retVal;
    }

    public void damage(DamageInfo info) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - currentBlock > 0) {
            AnimationState.TrackEntry e = state.setAnimation(0, "hit", false);
            AnimationState.TrackEntry e2 = state.addAnimation(0, "idle", true, 0.0F);
            e.setTimeScale(ANIMATION_SPEED);
            e2.setTimeScale(ANIMATION_SPEED);
        }

        super.damage(info);
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA(BogAudio.TOTEM_TRIGGER, MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false);
    }

    @Override
    public void playDeathAnimation() {
        super.playDeathAnimation();
        drawY -= 30f * Settings.scale;
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return BogAudio.TOTEM_TRIGGER;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 5;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.OCEAN_BOGWARDEN_COLOR;
    }

    @Override
    public Color getCardTrailColor() {
        return characterColor.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontGreen;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new ShadowFont();
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheBogwarden(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return characterColor.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return characterColor.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                FlashAtkImgPatches.BOGWARDEN_BLAST_EFFECT,
                FlashAtkImgPatches.BOGWARDEN_BLAST_EFFECT,
                FlashAtkImgPatches.BOGWARDEN_BLAST_EFFECT};
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    @Override
    public String getSensoryStoneText() {
        return TEXT[3];
    }

    @Override
    public Texture getCutsceneBg() {
        return TexLoader.getTexture(makeImagePath("ending/bg.png"));
    }

    private static boolean endEffectStarted = false;
    @Override
    public List<CutscenePanel> getCutscenePanels() {
        endEffectStarted = false;
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel(makeImagePath("ending/1.png"), BogAudio.MALEDICT));
        panels.add(new CutscenePanel(makeImagePath("ending/2.png"), "RELIC_DROP_CLINK"));
        panels.add(new CutscenePanel(makeImagePath("ending/3.png")));
        return panels;
    }

    @Override
    public void updateVictoryVfx(ArrayList<AbstractGameEffect> effects) {
        if (!endEffectStarted) {
            effects.add(new BogwardenVictoryEffect());
            endEffectStarted = true;
        }
    }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_BOGWARDEN_OCEAN;
        @SpireEnum(name = "OCEAN_BOGWARDEN_COLOR")
        public static AbstractCard.CardColor OCEAN_BOGWARDEN_COLOR;
        @SpireEnum(name = "OCEAN_BOGWARDEN_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }
}