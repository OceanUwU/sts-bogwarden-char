package bogwarden.util;

import basemod.AutoAdd;
import bogwarden.BogMod;
import CardAugments.CardAugmentsMod;
import CardAugments.cardmods.AbstractAugment;

public class CardAugmentsLoader {
    public static void load() {
        new AutoAdd(BogMod.modID)
            .packageFilter("bogwarden.cardmods")
            .any(AbstractAugment.class, (info, abstractAugment) -> CardAugmentsMod.registerAugment(abstractAugment, BogMod.modID));
    }
}