package bogwarden.util;

import com.evacipated.cardcrawl.modthespire.Loader;

public class ModManager {
    public static boolean isMultiplayerLoaded = Loader.isModLoaded("spireTogether");
    public static boolean isChimeraLoaded = Loader.isModLoaded("CardAugments");
    public static boolean isPackmasterLoaded = Loader.isModLoaded("anniv5");
}