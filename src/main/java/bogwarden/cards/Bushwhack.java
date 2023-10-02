package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Bushwhack extends AbstractBogCard {
    public final static String ID = makeID("Bushwhack");
    private final static Bushwhack dummyCard = new Bushwhack();

    public Bushwhack() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        setBlock(6, +1);
        setSecondMagic(2, +1);
        setThirdMagic(18, +2);
    }

    public void onPlayCard(AbstractCard c, AbstractMonster m) {
        AbstractDungeon.actionManager.cardsPlayedThisCombat.add(dummyCard);
        applyPowers();
        AbstractDungeon.actionManager.cardsPlayedThisCombat.remove(dummyCard);
    }

    @Override
    public void applyPowers() {
        baseMagicNumber = magicNumber = AbstractDungeon.actionManager.cardsPlayedThisCombat.size();
        super.applyPowers();
    }
  
    public void triggerOnGlowCheck() {
        this.glowColor = (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() < thirdMagic) ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                if (AbstractDungeon.actionManager.cardsPlayedThisCombat.size() - 1 < thirdMagic)
                    applyToSelfTop(new PlatedArmorPower(p, secondMagic));
            }
        });
    }
}