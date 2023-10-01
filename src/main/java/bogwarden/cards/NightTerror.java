package bogwarden.cards;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HeartMegaDebuffEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class NightTerror extends AbstractBogCard {
    public final static String ID = makeID("NightTerror");

    public NightTerror() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.ALL_ENEMY);
        setMagic(99);
        setUpgradedCost(1);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractGameEffect effect = new HeartMegaDebuffEffect();
        effect.startingDuration = 1.6f;
        effect.duration = effect.startingDuration;
        vfx(effect, 0.8f);
        forAllMonstersLiving(mo -> {
            applyToEnemy(mo, new VulnerablePower(mo, magicNumber, false));
            if (mo.intent.equals(AbstractMonster.Intent.SLEEP))
                atb(new TalkAction(mo, cardStrings.EXTENDED_DESCRIPTION[0], 4.0f, 4.0f));
        });
    }
}