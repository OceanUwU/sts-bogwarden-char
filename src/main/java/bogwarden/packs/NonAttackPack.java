package bogwarden.packs;

import bogwarden.cards.*;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;

public class NonAttackPack extends AbstractBogPack {
    public static final String ID = makeID("NonAttackPack");

    public NonAttackPack() {
        super(ID, Blast.ID, new PackSummary(4, 2, 2, 2, 4));
    }

    public ArrayList<String> getCards() {
        ArrayList<String> cards = new ArrayList<>();
        cards.add(BolaToss.ID);
        cards.add(VilePowder.ID);

        cards.add(AcidCloud.ID);
        cards.add(FlockOfBats.ID);
        cards.add(RestlessSpirits.ID);
        cards.add(Attunement.ID);
        cards.add(Survey.ID);

        cards.add(UnstableConcoction.ID);
        cards.add(RefinedBlast.ID);
        cards.add(DeathWard.ID);
        return cards;
    }
}