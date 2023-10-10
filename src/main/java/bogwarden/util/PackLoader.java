package bogwarden.util;

import basemod.AutoAdd;
import bogwarden.BogMod;
import bogwarden.cards.AbstractBogCard;
import bogwarden.characters.TheBogwarden;
import bogwarden.packs.AbstractBogPack;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.interfaces.EditPacksSubscriber;

public class PackLoader implements EditPacksSubscriber {
    @Override
    public void receiveEditPacks() {
        SpireAnniversary5Mod.allowCardClass(AbstractBogCard.class);
        SpireAnniversary5Mod.allowCardColor(TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
        new AutoAdd(BogMod.modID)
            .packageFilter("bogwarden.packs")
            .any(AbstractBogPack.class, (info, pack) -> SpireAnniversary5Mod.declarePack(pack));
    }
}