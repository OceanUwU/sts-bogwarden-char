package bogwarden.cards;

import basemod.AutoAdd;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

@AutoAdd.Ignore
public class BadMedicine extends AbstractBogCard {
    public final static String ID = makeID("BadMedicine");

    public BadMedicine() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setDamage(8, +2);
        setMagic(4);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < 2; i++)
            dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        applyToEnemy(m, new TickingRegenOnMonsterPower(m, magicNumber));
    }

    public static class TickingRegenOnMonsterPower extends RegenPower {    
        public static String POWER_ID = makeID("TickingRegenOnMonsterPower");

        public TickingRegenOnMonsterPower(AbstractCreature owner, int heal) {
            super(owner, heal);
            ID = POWER_ID;
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            flashWithoutSound();
            att(new RegenAction2(owner, amount));
        }

        public static class RegenAction2 extends AbstractGameAction {
            public RegenAction2(AbstractCreature target, int amount) {
                this.target = target;
                this.amount = amount;
                actionType = AbstractGameAction.ActionType.DAMAGE;
                duration = Settings.ACTION_DUR_FAST;
            }
            
            public void update() {
                if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
                    isDone = true;
                    return;
                } 
                if (duration == Settings.ACTION_DUR_FAST) {
                    if (target.currentHealth > 0) {
                        target.tint.color = Color.CHARTREUSE.cpy();
                        target.tint.changeColor(Color.WHITE.cpy());
                        target.heal(amount, true);
                    }
                    AbstractPower p = target.getPower(POWER_ID);
                    if (p != null) {
                        if (--p.amount == 0)
                            target.powers.remove(p);
                        else
                            p.updateDescription();
                    }
                } 
                tickDuration();
            }
        }
    }

    /*public static class BadMedicinePower extends AbstractBogPower {
        public static String POWER_ID = makeID("BadMedicinePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public BadMedicinePower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public int onHeal(int healAmount) {
            if (healAmount > 0f) {
                flash();
                addToBot(new DamageRandomEnemyAction(new DamageInfo(owner, healAmount * amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.POISON));
            }
           return healAmount;
        }
    }*/
}