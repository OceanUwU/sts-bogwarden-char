package bogwarden.charbosses.bosses;

import charbosses.bosses.AbstractBossDeckArchetype;

public abstract class ArchetypeBaseBogwarden extends AbstractBossDeckArchetype {
    public ArchetypeBaseBogwarden(String id, String loggerName) {
        super(id, "Bogwarden", loggerName);
    }

    protected void loop() {
        turn = 0;
        looped = true;
    }
}