package bogwarden.cards;

import bogwarden.powers.LoseSpinesPower;
import bogwarden.powers.Spines;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class BrambleShield extends AbstractBogCard {
    public final static String ID = makeID("BrambleShield");

    public BrambleShield() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        setBlock(4, +2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SFXAction(BogAudio.PLANT_PULL));
        vfx(new NaturesWrath.PlantMassacreEffect(p));
        blck();
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                applyToSelfTop(new LoseSpinesPower(p, p.currentBlock)); 
                applyToSelfTop(new Spines(p, p.currentBlock)); 
            }
        });
    }
}