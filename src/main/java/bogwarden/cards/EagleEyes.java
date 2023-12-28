package bogwarden.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.vfx.OpenEyesEffect;

public class EagleEyes extends AbstractBogCard {
    public final static String ID = makeID("EagleEyes");
    private static int scryAmt = 0;

    public EagleEyes() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setBlock(7, +3);
        setMagic(2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        OpenEyesEffect eyes = new OpenEyesEffect(Color.YELLOW, false, false, false, 1f);
        vfx(eyes);
        blck();
        scryAmt = 0;
        forAllMonstersLiving(mo -> scryAmt += magicNumber);
        if (scryAmt > 0)
            atb(new ScryAction(scryAmt));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                eyes.canGoPastHalf = true;
            }
        });
    }
}