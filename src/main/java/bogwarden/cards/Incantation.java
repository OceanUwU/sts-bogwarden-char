package bogwarden.cards;

import bogwarden.vfx.OpenEyesEffect;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlameParticleEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Incantation extends AbstractBogCard {
    public final static String ID = makeID("Incantation");
    private static boolean fromIncantation = false;
    private static int blastsToAdd;

    public Incantation() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.NONE);
        setMagic(1, +1);
        setSecondMagic(2);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        OpenEyesEffect eyes = new OpenEyesEffect(new Color(0.93f, 0f, 0.55f, 1f), false, false, false, 1f);
        vfx(eyes);
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
                eyes.canGoPastHalf = true;
            }
        });
    }

    private static void makeThem(int amt) {
        att(new MakeTempCardInHandAction(new Blast(), amt));
        //vfxTop(new IncantationEffect());
        att(new AbstractGameAction() {
            public void update() {
                isDone = true;
                for (int i = 0; i < 2; i++)
                    for (int j = 0; j < 65; j++)
                        AbstractDungeon.effectList.add(new FlameParticleEffect(Settings.WIDTH / 2f + OpenEyesEffect.GAP * (i*2-1), Settings.HEIGHT / 2f + 175f * Settings.scale));
            }
        });
        att(new SFXAction("ATTACK_FIRE"));
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