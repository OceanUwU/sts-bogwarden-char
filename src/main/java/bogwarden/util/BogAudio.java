package bogwarden.util;

import basemod.BaseMod;
import bogwarden.BogMod;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makePath;

public class BogAudio {
    public static String TRAP_TRIGGER = makeID("TRIGGER");
    public static String TOTEM_TRIGGER = makeID("TOTEMTRIGGER");
    public static String BACKFIRE_TRIGGER = makeID("BACKFIRETRIGGER");

    public static void addAudio() {
        for (String i : new String[] {TRAP_TRIGGER, TOTEM_TRIGGER, BACKFIRE_TRIGGER})
            BaseMod.addAudio(i, makePath("audio/"+i.replace(BogMod.modID+":", "").toLowerCase()+".ogg"));
    }
}