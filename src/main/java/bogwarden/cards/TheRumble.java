package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class TheRumble extends AbstractBogCard {
    public final static String ID = makeID("TheRumble");

    public TheRumble() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        setDamage(6, +2);
        isMultiDamage = true;
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = isEliteOrBoss() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < (isEliteOrBoss() ? 2 : 1); i++)
            allDmg(AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }
}