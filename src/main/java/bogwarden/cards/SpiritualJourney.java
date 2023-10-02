package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class SpiritualJourney extends AbstractBogCard {
    public final static String ID = makeID("SpiritualJourney");

    public SpiritualJourney() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        setMagic(4, +1);
        setEthereal(true, false);
        setExhaust(true);
        tags.add(CardTags.HEALING);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                p.increaseMaxHp(magicNumber, true);
            } 
        });
        applyToSelf(new SpiritualJourneyPower(p, magicNumber));
    }

    public static class SpiritualJourneyPower extends AbstractBogPower {
        public static String POWER_ID = makeID("SpiritualJourneyPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public SpiritualJourneyPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            if (card.type == AbstractCard.CardType.ATTACK) {
                flash();
                att(new ReducePowerAction(owner, owner, this, 1));
                att(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        owner.decreaseMaxHealth(1);
                        AbstractDungeon.effectsQueue.add(new DamageNumberEffect(owner, owner.hb.cX, owner.hb.cY, 1));
                        for(int i = 0; i < 50; i++)
                            AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(owner.hb.cX, owner.hb.cY));
                    }
                });
            }
        }
    }
}