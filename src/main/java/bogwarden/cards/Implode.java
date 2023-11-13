package bogwarden.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Implode extends AbstractBogCard {
    public final static String ID = makeID("Implode");

    public Implode() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);
        setBlock(12, +4);
        setMagic(1);
        setSecondMagic(8);
        setThirdMagic(1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new FlashAtkImgEffect(p.hb.cX, p.hb.cY, BLAST_EFFECT));
        blck();
        applyToSelf(new BlurPower(p, magicNumber));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                AbstractMonster mo = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                applyToEnemyTop(mo, new BlurPower(mo, thirdMagic));
                att(new GainBlockAction(mo, p, secondMagic));
            } 
        });
    }

    @SpirePatch(clz=MonsterGroup.class, method="applyPreTurnLogic")
    public static class MakeEnemiesUseBlurPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall methodCall) throws CannotCompileException {
                    if (methodCall.getClassName().equals(AbstractMonster.class.getName()) && methodCall.getMethodName().equals("hasPower"))
                        methodCall.replace("$_ = ($proceed($$) || $proceed(\"" + BlurPower.POWER_ID + "\"));");
                }
            };
        }
    }
}