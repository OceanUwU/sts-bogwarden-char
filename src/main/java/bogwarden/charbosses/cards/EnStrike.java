package bogwarden.charbosses.cards;

import bogwarden.cards.Strike;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;

public class EnStrike extends AbstractBogwardenBossCard {
    public EnStrike() {
        super(new Strike(), Intent.ATTACK);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(p, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }
}
