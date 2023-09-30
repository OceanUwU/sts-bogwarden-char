package bogwarden.cards;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SneckoField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class Hoodwink extends AbstractBogCard {
    public final static String ID = makeID("Hoodwink");

    public Hoodwink() {
        super(ID, -1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        SneckoField.snecko.set(this, true);
        setDamage(5, +2);
        setSecondMagic(2);
    }

    public void applyPowers() {
        super.applyPowers();
        baseMagicNumber = magicNumber = Math.max(cost, 0) + secondMagic;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++)
            dmgRandom(AbstractGameAction.AttackEffect.SLASH_VERTICAL);
    }
}