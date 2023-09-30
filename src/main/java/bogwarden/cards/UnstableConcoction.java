package bogwarden.cards;

import bogwarden.potions.ExplosivePotionPlus;
import bogwarden.potions.FirePotionPlus;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.ExplosivePotion;
import com.megacrit.cardcrawl.potions.FirePotion;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class UnstableConcoction extends AbstractBogCard {
    public final static String ID = makeID("UnstableConcoction");

    public UnstableConcoction() {
        super(ID, 1, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(4);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SMASH);
        if (isEliteOrBoss())
            atb(new ObtainPotionAction(upgraded ? new FirePotionPlus() : new FirePotion()));
        else
            atb(new ObtainPotionAction(upgraded ? new ExplosivePotionPlus() : new ExplosivePotion()));
    }
}