package bogwarden;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import bogwarden.cards.AbstractBogCard;
import bogwarden.cards.cardvars.*;
import bogwarden.characters.TheBogwarden;
import bogwarden.potions.*;
import bogwarden.relics.AbstractBogRelic;
import bogwarden.util.AspirationSkillbookAdder;
import bogwarden.util.BogAudio;
import bogwarden.util.CardAugmentsLoader;
import bogwarden.util.ModManager;
import bogwarden.util.PackLoader;
import bogwarden.util.Skindexer;
import bogwarden.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import thePackmaster.SpireAnniversary5Mod;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class BogMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber {

    public static final String modID = "bogwarden";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static Color characterColor = new Color(0.125f, 0.294f, 0.255f, 1);

    public static final String SHOULDER1 = makeCharacterPath("mainChar/shoulder.png");
    public static final String SHOULDER2 = makeCharacterPath("mainChar/shoulder2.png");
    public static final String CORPSE = makeCharacterPath("mainChar/corpse.png");
    private static final String ATTACK_S_ART = makeImagePath("512/attack.png");
    private static final String SKILL_S_ART = makeImagePath("512/skill.png");
    private static final String POWER_S_ART = makeImagePath("512/power.png");
    private static final String CARD_ENERGY_S = makeImagePath("512/energy.png");
    private static final String TEXT_ENERGY = makeImagePath("512/text_energy.png");
    private static final String ATTACK_L_ART = makeImagePath("1024/attack.png");
    private static final String SKILL_L_ART = makeImagePath("1024/skill.png");
    private static final String POWER_L_ART = makeImagePath("1024/power.png");
    private static final String CARD_ENERGY_L = makeImagePath("1024/energy.png");
    private static final String CHARSELECT_BUTTON = makeImagePath("charSelect/charButton.png");
    private static final String CHARSELECT_PORTRAIT = makeImagePath("charSelect/charBG.png");
    private static SpireConfig config;
    public static boolean useModdedPools;
    public static boolean doDownfallBoss;

    public static Settings.GameLanguage[] SupportedLanguages = {
            Settings.GameLanguage.ENG,
    };

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    public BogMod() {
        BaseMod.subscribe(this);
        if (ModManager.isPackmasterLoaded)
            SpireAnniversary5Mod.subscribe(new PackLoader());
        if (Loader.isModLoaded("skindex") || Loader.isModLoaded("spireTogether"))
            Skindexer.register();

        BaseMod.addColor(TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR, characterColor, characterColor, characterColor,
                characterColor, characterColor, characterColor, characterColor,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath)
    {
        return modID + "Resources/images/char/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() throws IOException {
        Properties defaults = new Properties();
        defaults.setProperty("usemoddedpools", "true");
        defaults.setProperty("downfallboss", "true");
        config = new SpireConfig(modID, "config", defaults);
        useModdedPools = config.getBool("usemoddedpools");
        doDownfallBoss = config.getBool("downfallboss");

        BogMod thismod = new BogMod();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new TheBogwarden(TheBogwarden.NAMES[1], TheBogwarden.Enums.THE_BOGWARDEN_OCEAN),
                CHARSELECT_BUTTON, CHARSELECT_PORTRAIT, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        
        //liquidColor, hybridColor, spotsColor
        BaseMod.addPotion(MojoPotion.class, new Color(0.47f, 0.12f, 0.66f, 1f), new Color(0.79f, 0.34f, 0.90f, 1f), new Color(1f, 0.73f, 0.01f, 1f), MojoPotion.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        BaseMod.addPotion(BogWater.class, new Color(0.19f, 0.36f, 0.74f, 1f), new Color(0.19f, 0.36f, 0.74f, 1f), new Color(0.41f, 0.25f, 0.13f, 0.6f), BogWater.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        BaseMod.addPotion(BottleOfTricks.class, new Color(1f, 1f, 1f, 0f), new Color(1f, 1f, 1f, 0f), new Color(1f, 1f, 1f, 0f), BottleOfTricks.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        BaseMod.addPotion(TempMojoPotion.class, new Color(0.79f, 0.34f, 0.90f, 1f), new Color(1f, 0.73f, 0.01f, 1f), new Color(1f, 1f, 1f, 0f), TempMojoPotion.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        BaseMod.addPotion(MaledictPotion.class, new Color(0.24f, 0.94f, 0.35f, 1f), new Color(0.17f, 0.81f, 0.28f, 1f), new Color(0.62f, 0.24f, 0.94f, 1f), MaledictPotion.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        BaseMod.addPotion(SpinesPotion.class, new Color(0.26f, 0.31f, 0.22f, 1f), new Color(0.65f, 0.55f, 0.24f, 1f), null, SpinesPotion.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);

        BaseMod.addPotion(FirePotionPlus.class, Color.RED.cpy(), Color.ORANGE.cpy(), new Color(0.79f, 0.34f, 0.90f, 1f), FirePotionPlus.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
        BaseMod.addPotion(ExplosivePotionPlus.class, Color.ORANGE.cpy(), new Color(1f, 1f, 1f, 0f), new Color(0.79f, 0.34f, 0.90f, 1f), ExplosivePotionPlus.POTION_ID, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
            .packageFilter(AbstractBogRelic.class)
            .any(AbstractBogRelic.class, (info, relic) -> {
                if (relic.color == null) {
                    BaseMod.addRelic(relic, RelicType.SHARED);
                } else {
                    BaseMod.addRelicToCustomPool(relic, relic.color);
                }
                if (!info.seen) {
                    UnlockTracker.markRelicAsSeen(relic.relicId);
                }
            });
        if (Loader.isModLoaded("aspiration"))
            AspirationSkillbookAdder.add();
    }

    @Override
    public void receiveAddAudio() {
        BogAudio.addAudio();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        BaseMod.addDynamicVariable(new SecondDamage());
        new AutoAdd(modID)
                .packageFilter(AbstractBogCard.class)
                .setDefaultSeen(true)
                .cards();
    }


    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, modID + "Resources/localization/" + getLangString() + "/Cardstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, modID + "Resources/localization/" + getLangString() + "/Relicstrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, modID + "Resources/localization/" + getLangString() + "/Charstrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, modID + "Resources/localization/" + getLangString() + "/Potionstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, modID + "Resources/localization/" + getLangString() + "/Powerstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/" + getLangString() + "/UIstrings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/" + getLangString() + "/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receivePostInitialize() {
        String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenu")).TEXT;
        ModPanel settingsPanel = new ModPanel();
        settingsPanel.addUIElement(new ModLabeledToggleButton(TEXT[3], 350, 800, Settings.CREAM_COLOR, FontHelper.charDescFont, config.getBool("usemoddedpools"), settingsPanel, label -> {}, button -> {
            useModdedPools = button.enabled;
            config.setBool("usemoddedpools", button.enabled);
            try {config.save();} catch (Exception e) {}
        }));
        /*settingsPanel.addUIElement(new ModLabeledToggleButton(TEXT[4], 350, 800-80, Settings.CREAM_COLOR, FontHelper.charDescFont, config.getBool("downfallboss"), settingsPanel, label -> {}, button -> {
            doDownfallBoss = button.enabled;
            config.setBool("downfallboss", button.enabled);
            try {config.save();} catch (Exception e) {}
        }));*/
        BaseMod.registerModBadge(TexLoader.getTexture(makeImagePath("ui/badge.png")), TEXT[0], TEXT[1], TEXT[2], settingsPanel);

        if (ModManager.isChimeraLoaded)
            CardAugmentsLoader.load();
    }
}
