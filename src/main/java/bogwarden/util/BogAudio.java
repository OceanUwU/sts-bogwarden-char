package bogwarden.util;

import basemod.BaseMod;
import bogwarden.BogMod;
import java.lang.reflect.Field;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makePath;

public class BogAudio {
    public static String
        TRAP_TRIGGER = makeID("TRIGGER"),
        TOTEM_TRIGGER = makeID("TOTEMTRIGGER"),
        BACKFIRE_TRIGGER = makeID("BACKFIRETRIGGER"),
        BLAST = makeID("BLAST"),
        BUGS = makeID("BUGS"),
        BATS = makeID("BATS"),
        MOJO = makeID("MOJO"),
        MALEDICT = makeID("MALEDICT"),
        FROGS = makeID("FROGS"),
        TOAD = makeID("TOAD"),
        BLOW_DART = makeID("BLOWDART"),
        SHOTGUN = makeID("SHOTGUN"),
        OTHERSIDER = makeID("OTHERSIDER"),
        REFINED_BLAST = makeID("REFINEDBLAST"),
        IMPLODE = makeID("IMPLODE"),
        RUSTLE = makeID("RUSTLE"),
        PLANT_PULL = makeID("PLANTPULL"),
        WHOOSH = makeID("WHOOSH"),
        BUS = makeID("BUS"),
        NATURESWRATH = makeID("NATURESWRATH"),
        INCARCERATE = makeID("INCARCERATE");

    public static void addAudio() {
        try {
            for (Field field : BogAudio.class.getFields())
                if (field.getType().equals(String.class))
                    BaseMod.addAudio((String)field.get(null), makePath("audio/"+((String)field.get(null)).replace(BogMod.modID+":", "").toLowerCase()+".ogg"));
        } catch (Exception e) {}
    }
}