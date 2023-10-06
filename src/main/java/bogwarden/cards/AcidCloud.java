package bogwarden.cards;

import bogwarden.powers.Venom;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.GainStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class AcidCloud extends AbstractBogCard {
    public final static String ID = makeID("AcidCloud");

    public AcidCloud() {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        setMagic(3, +1);
        setSecondMagic(4, +2);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mo -> applyToEnemy(mo, new Venom(mo, magicNumber)));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                forAllMonstersLivingTop(mo -> {if (!mo.hasPower(ArtifactPower.POWER_ID)) applyToEnemyTop(mo, new GainStrengthPower(mo, secondMagic));});
                forAllMonstersLivingTop(mo -> applyToEnemyTop(mo, new StrengthPower(mo, -secondMagic)));
            }
        });
    }
}