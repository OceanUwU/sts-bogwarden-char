package bogwarden.patches;

import basemod.ReflectionHacks;
import basemod.patches.whatmod.WhatMod;
import bogwarden.cards.*;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import java.util.HashMap;

import static bogwarden.BogMod.makeID;

@SpirePatch(clz=WhatMod.class, method="getBody")
public class BetaArtistWhatModPatch {
    private static final String[] TEXT = CardCrawlGame.languagePack.getUIString(makeID("BetaArtCredits")).TEXT;
    private static final HashMap<Class<? extends AbstractBogCard>, String> artists = new HashMap<Class<? extends AbstractBogCard>,String>();
    private static boolean doing;

    static {
        for (Class<?> c : new Class<?>[] {
            EntanglingVines.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "Carbon the Wanderer");
        for (Class<?> c : new Class<?>[] {
            Bushwhack.class, VilePowder.class, IllIntendedSchemes.class, SpinningBlades.class, Implode.class, Shank.class, PoisonNova.class, CampOut.class, NaturesWrath.class, UnstableConcoction.class, Blast.class, Forge.class, BackfiringTrap.class, BadMedicine.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "cuboid consumer");
        for (Class<?> c : new Class<?>[] {
            PressurePlate.class, DarkPact.class, VengefulTotem.class, HexingTotem.class, WardingTotem.class, OthersiderForm.class, SpiritualJourney.class, Bonfire.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "Dark_Brick");
        for (Class<?> c : new Class<?>[] {
            DeathWard.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "Mezix");
        for (Class<?> c : new Class<?>[] {
            Geyser.class, RabbitPunch.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "mucky");
        for (Class<?> c : new Class<?>[] {
            Tripwire.class, PlagueOfToads.class, Blunderbuss.class, AcidCloud.class, WildMagic.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "NuclearCyborg");
        for (Class<?> c : new Class<?>[] {
            Strike.class, Defend.class, SnapperTrap.class, SnareTrap.class, Incantation.class, TheRumble.class, BolaToss.class, BlendingIn.class, RestlessSpirits.class, Darkvision.class, LineUp.class, BrambleShield.class, Walkabout.class, Hurl.class, Bide.class, Hoodwink.class, StickerbrushStrike.class, JarOfSpiders.class, Friends.class, BriarPatch.class, RefinedBlast.class, NightTerror.class, Incarcerate.class, CorpseFlies.class, TinShield.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "occultpyromancer");
        for (Class<?> c : new Class<?>[] {
            Snip.class, Attunement.class, WitchingHour.class, Stalker.class, BoulderTrap.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "ocean");
        for (Class<?> c : new Class<?>[] {
            ShadowFont.class, WatchAndLearn.class, Submerge.class, SpitefulStaff.class, HairTrigger.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "TheLethargicWeirdo");
        for (Class<?> c : new Class<?>[] {
            Jinx.class, Blowpipe.class, ViperStrike.class, SpinyShawl.class, Portents.class, EagleEyes.class, Survey.class, Sap.class, BarbedLasso.class, BroilingFlames.class, BagAndTag.class, SludgeBomb.class 
        }) artists.put((Class<? extends AbstractBogCard>)c, "Vex'd");
        for (Class<?> c : new Class<?>[] {
            DarkRitual.class
        }) artists.put((Class<? extends AbstractBogCard>)c, "Zorgrox");
        artists.put(Strike.class, "occultpyromancer");
    }

    public static SpireReturn<String> Prefix(Class<?>... cls) {
        try {
            if (doing)
                doing = false;
            else if (cls.length > 0 && AbstractBogCard.class.isAssignableFrom(cls[0]) && (Settings.PLAYTESTER_ART_MODE || UnlockTracker.betaCardPref.getBoolean((String)cls[0].getField("ID").get(null), false)) && artists.containsKey(cls[0])) {
                doing = true;
                return SpireReturn.Return(ReflectionHacks.privateStaticMethod(WhatMod.class, "getBody", Class[].class).invoke(new Object[] {cls}) + TEXT[0] + artists.get(cls[0]));
            }
        } catch (Exception e) {}
        return SpireReturn.Continue();
    }
}