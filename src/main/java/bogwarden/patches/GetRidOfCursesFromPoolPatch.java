package bogwarden.patches;

import bogwarden.cards.BackfiringTrap;
import bogwarden.cards.DollsCurse;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import java.util.ArrayList;

public class GetRidOfCursesFromPoolPatch {
    @SpirePatch(clz=CardLibrary.class,method="getCurse",paramtypez={})
    public static class WithoutRandom {
        @SpireInsertPatch(rloc=8,localvars={"tmp"})
        public static void Insert(ArrayList<String> tmp) {removeRustFromPool(tmp);}
    }
    @SpirePatch(clz=CardLibrary.class,method="getCurse",paramtypez={AbstractCard.class, Random.class})
    public static class WithRandom {
        @SpireInsertPatch(rloc=12,localvars={"tmp"})
        public static void Insert(ArrayList<String> tmp) {removeRustFromPool(tmp);}
    }

    private static void removeRustFromPool(ArrayList<String> tmp) {
        tmp.remove(BackfiringTrap.ID);
        tmp.remove(DollsCurse.ID);
    }
}