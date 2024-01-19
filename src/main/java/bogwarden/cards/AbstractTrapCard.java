package bogwarden.cards;

import bogwarden.actions.TriggerTrapAction;
import bogwarden.powers.AbstractBogPower;
import bogwarden.relics.AbstractBogRelic;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public abstract class AbstractTrapCard extends AbstractBogCard {
    private static final CardStrings trapStrings = CardCrawlGame.languagePack.getCardStrings(makeID("TrapCard"));

    public int timesToTrigger;
    public boolean using = false;
    protected String sfx = BogAudio.TRAP_TRIGGER;

    public AbstractTrapCard(String cardID, CardRarity rarity) {
        super(cardID, -2, CardType.SKILL, rarity, CardTarget.NONE);
        setRetain(true);
        damageType = damageTypeForTurn = DamageType.THORNS;
    }
  
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = trapStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (dontTriggerOnUseCard) {
            AbstractDungeon.actionManager.actions.addAll(TriggerTrapAction.saveActions);
            TriggerTrapAction.saveActions.clear();
            for (int i = 0; i < timesToTrigger; i++) {
                AbstractTrapCard trap = this;
                att(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        target = m;
                        if (target == null)
                            target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                        applyPowers();
                        calculateCardDamage((AbstractMonster)target);
                        trigger(p, (AbstractMonster)target);
                        for (AbstractPower po : p.powers)
                            if (po instanceof AbstractBogPower)
                                ((AbstractBogPower)po).onTriggerTrap(trap);
                        for (AbstractRelic r : p.relics)
                            if (r instanceof AbstractBogRelic)
                                ((AbstractBogRelic)r).onTriggerTrap(trap);
                    }
                });
            }
            CardCrawlGame.sound.play(sfx);
            using = false;
        }
    }

    public abstract void trigger(AbstractPlayer p, AbstractMonster m);
}