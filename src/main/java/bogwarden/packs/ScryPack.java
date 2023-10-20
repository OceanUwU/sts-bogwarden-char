package bogwarden.packs;

import bogwarden.cards.*;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;

public class ScryPack extends AbstractBogPack {
    public static final String ID = makeID("ScryPack");

    public ScryPack() {
        super(ID, EagleEyes.ID, new PackSummary(2, 3, 4, 2, 4));
    }

    public ArrayList<String> getCards() {
        ArrayList<String> cards = new ArrayList<>();
        cards.add(Incantation.ID);
        cards.add(Bushwhack.ID);

        cards.add(Stalker.ID);
        cards.add(EagleEyes.ID);
        cards.add(LineUp.ID);
        cards.add(Darkvision.ID);
        cards.add(Portents.ID);
        cards.add(Walkabout.ID);

        cards.add(CorpseFlies.ID);
        cards.add(WildMagic.ID);
        return cards;
    }
}