package bogwarden.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.vfx.OpenEyesEffect;

public class NightTerror extends AbstractBogCard {
    public final static String ID = makeID("NightTerror");

    public NightTerror() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setMagic(99);
        setSecondMagic(0, +5);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameEffect effect = new HeartMegaDebuffEffect();
        effect.startingDuration = 1.6f;
        effect.duration = effect.startingDuration;
        vfx(new OpenEyesEffect(Color.WHITE, false, true, true, 1f));
        vfx(effect, 0.8f);
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new VulnerablePower(mo, magicNumber, false));
            if (mo.intent.equals(AbstractMonster.Intent.SLEEP))
                atb(new TalkAction(mo, cardStrings.EXTENDED_DESCRIPTION[0], 4.0f, 4.0f));
        });
        if (secondMagic > 0)
            applyToSelf(new VigorPower(p, secondMagic));
    }
}