package bogwarden.cards;

import bogwarden.actions.TriggerTrapAction;
import bogwarden.powers.AbstractBogPower;
import bogwarden.relics.AbstractBogRelic;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static bogwarden.BogMod.makeID;

public abstract class AbstractTrapCard extends AbstractBogCard {
    private static final CardStrings trapStrings = CardCrawlGame.languagePack.getCardStrings(makeID("TrapCard"));

    public int timesToTrigger;
    protected String sfx = BogAudio.TRAP_TRIGGER;

    public AbstractTrapCard(String cardID, CardRarity rarity) {
        super(cardID, -2, CardType.SKILL, rarity, CardTarget.NONE);
        setRetain(true);
        damageType = damageTypeForTurn = DamageType.THORNS;
    }
  
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = trapStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (dontTriggerOnUseCard) {
            AbstractDungeon.actionManager.actions.addAll(TriggerTrapAction.saveActions);
            for (int i = 0; i < timesToTrigger; i++) {
                applyPowers();
                calculateCardDamage(m);
                trigger(p, m);
                for (AbstractPower po : p.powers)
                    if (po instanceof AbstractBogPower)
                        ((AbstractBogPower)po).onTriggerTrap(this);
                for (AbstractRelic r : p.relics)
                    if (r instanceof AbstractBogRelic)
                        ((AbstractBogRelic)r).onTriggerTrap(this);
            }
            CardCrawlGame.sound.play(sfx);
        }
    }

    public abstract void trigger(AbstractPlayer p, AbstractMonster m);
}