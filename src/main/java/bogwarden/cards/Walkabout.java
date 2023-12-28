package bogwarden.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.vfx.OpenEyesEffect;

public class Walkabout extends AbstractBogCard {
    public final static String ID = makeID("Walkabout");
    private static boolean fromWalkabout = false;

    public Walkabout() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        setMagic(3, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        OpenEyesEffect eyes = new OpenEyesEffect(Color.BROWN, false, false, false, 1f);
        vfx(eyes);
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                fromWalkabout = true;
            }
        });
        atb(new ScryAction(magicNumber));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                fromWalkabout = false;
                eyes.canGoPastHalf = true;
            }
        });
    }

    @SpirePatch(clz=ScryAction.class, method="update")
    public static class DrawThem {
        @SpireInsertPatch(rloc=27)
        public static void Insert() {
            if (fromWalkabout)
                att(new DrawCardAction(AbstractDungeon.gridSelectScreen.selectedCards.size()));
        }
    }
}