package bogwarden.cards;

import bogwarden.actions.TriggerTrapAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public abstract class AbstractTrapCard extends AbstractBogCard {
    private static final CardStrings trapStrings = CardCrawlGame.languagePack.getCardStrings(makeID("TrapCard"));

    public int timesToTrigger;

    public AbstractTrapCard(String cardID, CardRarity rarity) {
        super(cardID, -2, CardType.SKILL, rarity, CardTarget.NONE);
        setRetain(true);
    }
  
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = trapStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (dontTriggerOnUseCard) {
            AbstractDungeon.actionManager.actions.addAll(TriggerTrapAction.saveActions);
            for (int i = 0; i < timesToTrigger; i++)
                trigger(p, m);
        }
    }

    public abstract void trigger(AbstractPlayer p, AbstractMonster m);
}