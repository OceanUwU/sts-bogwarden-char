package bogwarden.util;

import basemod.BaseMod;
import bogwarden.BogMod;
import java.lang.reflect.Field;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makePath;

public class BogAudio {
    public static String TRAP_TRIGGER = makeID("TRIGGER");
    public static String TOTEM_TRIGGER = makeID("TOTEMTRIGGER");
    public static String BACKFIRE_TRIGGER = makeID("BACKFIRETRIGGER");
    public static String BLAST = makeID("BLAST");
    public static String BUGS = makeID("BUGS");
    public static String BATS = makeID("BATS");
    public static String MOJO = makeID("MOJO");
    public static String MALEDICT = makeID("MALEDICT");
    public static String FROGS = makeID("FROGS");
    public static String TOAD = makeID("TOAD");
    public static String BLOW_DART = makeID("BLOWDART");
    public static String SHOTGUN = makeID("SHOTGUN");
    public static String OTHERSIDER = makeID("OTHERSIDER");
    public static String REFINED_BLAST = makeID("REFINEDBLAST");
    public static String IMPLODE = makeID("IMPLODE");
    public static String RUSTLE = makeID("RUSTLE");
    public static String PLANT_PULL = makeID("PLANTPULL");
    public static String WHOOSH = makeID("WHOOSH");

    public static void addAudio() {
        try {
            for (Field field : BogAudio.class.getFields())
                if (field.getType().equals(String.class))
                    BaseMod.addAudio((String)field.get(null), makePath("audio/"+((String)field.get(null)).replace(BogMod.modID+":", "").toLowerCase()+".ogg"));
        } catch (Exception e) {}
    }
}