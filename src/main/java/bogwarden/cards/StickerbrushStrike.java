package bogwarden.cards;

import bogwarden.cards.NaturesWrath.PlantMassacreEffect;
import bogwarden.powers.Spines;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class StickerbrushStrike extends AbstractBogCard {
    public final static String ID = makeID("StickerbrushStrike");

    public StickerbrushStrike() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(7, +2);
        setMagic(5, +2);
        tags.add(CardTags.STRIKE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new SFXAction(BogAudio.NATURESWRATH));
        vfx(new PlantMassacreEffect(m));
        dmg(m, AbstractGameAction.AttackEffect.SLASH_VERTICAL, true);
        applyToSelf(new Spines(p, magicNumber));
    }
}