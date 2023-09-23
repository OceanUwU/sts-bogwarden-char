package bogwarden.cards;

import bogwarden.patches.ScryPatches;
import bogwarden.powers.Venom;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SneakierStrike extends AbstractBogCard {
    public final static String ID = makeID("SneakierStrike");

    public SneakierStrike() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(13, +4);
        setMagic(1);
        setSecondMagic(1);
        tags.add(CardTags.STRIKE);
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (ScryPatches.ScriedThisTurn.yep)
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        if (ScryPatches.ScriedThisTurn.yep) {
            atb(new GainEnergyAction(magicNumber));
            applyToEnemy(m, new Venom(m, secondMagic));
        }
    }
}