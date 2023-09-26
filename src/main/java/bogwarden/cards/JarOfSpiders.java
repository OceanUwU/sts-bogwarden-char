package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class JarOfSpiders extends AbstractBogCard {
    public final static String ID = makeID("JarOfSpiders");

    public JarOfSpiders() {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        setDamage(4, +1);
        setMagic(4, +1);
        isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new VFXAction(p, new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()), 0.1F));
        allDmg(AbstractGameAction.AttackEffect.NONE);
        forAllMonstersLiving(mo -> applyToEnemy(mo, new JarOfSpidersPower(mo, magicNumber)));
    }

    public static class JarOfSpidersPower extends AbstractBogPower {
        public static String POWER_ID = makeID("JarOfSpidersPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public JarOfSpidersPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.DEBUFF, false, owner, amount);
            priority = 1;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                flashWithoutSound();
                atb(new DamageAction(owner, new DamageInfo(adp(), amount, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.POISON));
                atb(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
    }
}