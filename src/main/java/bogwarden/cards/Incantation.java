package bogwarden.cards;

import bogwarden.vfx.IncantationEffect;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Incantation extends AbstractBogCard {
    public final static String ID = makeID("Incantation");
    private static boolean fromIncantation = false;
    private static int blastsToAdd;

    public Incantation() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        setUpgradedCost(0);
        setMagic(2);
        setSecondMagic(2);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                fromIncantation = true;
                blastsToAdd = magicNumber;
            }
        });
        atb(new ScryAction(secondMagic));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                fromIncantation = false;
            }
        });
    }

    private static void makeThem(int amt) {
        att(new MakeTempCardInHandAction(new Blast(), amt));
        vfxTop(new IncantationEffect());
    }

    @SpirePatch(clz=ScryAction.class, method="update")
    public static class DrawThem {
        @SpireInsertPatch(rloc=10)
        public static void OnSeeNoCards() {
            if (fromIncantation)
                makeThem(blastsToAdd);
        }

        @SpireInsertPatch(rloc=27)
        public static void Insert(ScryAction __instance) {
            if (fromIncantation && AbstractDungeon.gridSelectScreen.selectedCards.size() >= AbstractDungeon.gridSelectScreen.targetGroup.group.size())
                makeThem(blastsToAdd);
        }
    }
}