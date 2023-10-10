package bogwarden.packs;

import bogwarden.cards.*;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;

public class TrapPack extends AbstractBogPack {
    public static final String ID = makeID("TrapPack");

    public TrapPack() {
        super(ID, makeID("TrapperMaster"), new PackSummary(3, 3, 2, 3, 2));
        hatHidesHair = true;
    }

    public ArrayList<String> getCards() {
        ArrayList<String> cards = new ArrayList<>();
        cards.add(SnapperTrap.ID);
        cards.add(SnareTrap.ID);
        cards.add(WatchAndLearn.ID);

        cards.add(BagAndTag.ID);
        cards.add(SpinningBlades.ID);
        cards.add(WardingTotem.ID);
        cards.add(HairTrigger.ID);
        cards.add(Bide.ID);

        cards.add(SludgeBomb.ID);
        cards.add(Friends.ID);
        return cards;
    }
}