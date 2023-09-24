package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class Blunderbuss extends AbstractBogCard {
    public final static String ID = makeID("Blunderbuss");

    public Blunderbuss() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        setDamage(0);
        setMagic(2);
        setSecondMagic(2, +1);
    }
  
    public int getDamage() {
        int count = 0;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cost == -2)
                count += secondMagic;
            else
                count++;
        }
        return count * magicNumber;
    }
  
    public void calculateCardDamage(AbstractMonster mo) {
        baseDamage = getDamage();
        super.calculateCardDamage(mo);
    }
    
    public void applyPowers() {
        baseDamage = getDamage();
        super.applyPowers();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        baseDamage = getDamage();
        calculateCardDamage(m);
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
    }
}