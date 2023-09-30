package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
import com.megacrit.cardcrawl.vfx.combat.DamageNumberEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Friends extends AbstractBogCard {
    public final static String ID = makeID("Friends");

    public Friends() {
        super(ID, 1, CardType.POWER, CardRarity.RARE, CardTarget.SELF);
        setMagic(1);
        setSecondMagic(3);
        setUpgradedCost(0);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new BufferPower(p, 1));
        applyToSelf(new NextTurnIntangible(p, magicNumber));
        applyToSelf(new LoseMaxHPLater(p, secondMagic, 2));
    }

    public static class NextTurnIntangible extends AbstractBogPower {
        public static String POWER_ID = makeID("NextTurnIntangible");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public NextTurnIntangible(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            flash();
            applyToSelf(new IntangiblePlayerPower(owner, amount));
            atb(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    public static class LoseMaxHPLater extends AbstractBogPower implements NonStackablePower {
        public static String POWER_ID = makeID("LoseMaxHPLater");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public LoseMaxHPLater(AbstractCreature owner, int amount, int turns) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
            isTwoAmount = true;
            amount2 = turns;
            updateDescription();
        }

        public boolean isStackable(AbstractPower power) {
            return power instanceof LoseMaxHPLater && ((LoseMaxHPLater)power).amount2 == amount2;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount2 + powerStrings.DESCRIPTIONS[amount2 == 1 ? 1 : 2] + amount + powerStrings.DESCRIPTIONS[3];
        }
  
        public void atStartOfTurn() {
            if (--amount2 <= 0) {
                int maxHPToLose = amount;
                atb(new AbstractGameAction() {
                    public void update() {
                        isDone = true;
                        owner.decreaseMaxHealth(maxHPToLose);
                        AbstractDungeon.effectsQueue.add(new DamageNumberEffect(owner, owner.hb.cX, owner.hb.cY, maxHPToLose));
                        for(int i = 0; i < 50; i++)
                            AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(owner.hb.cX, owner.hb.cY));
                        flash();
                    }
                });
                atb(new RemoveSpecificPowerAction(owner, owner, this));
            } else {
                stackPower(0);
                updateDescription();
            }
        }
    }
}