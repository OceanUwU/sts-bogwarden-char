package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Darkvision extends AbstractBogCard {
    public final static String ID = makeID("Darkvision");

    public Darkvision() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(3);
        setSecondMagic(2, -1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DarkvisionPower(p, magicNumber, secondMagic));
    }

    public static class DarkvisionPower extends AbstractBogPower implements NonStackablePower {
        public static String POWER_ID = makeID("DarkvisionPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        private static DarkvisionPower fromDarkVision;
    
        public DarkvisionPower(AbstractCreature owner, int amount, int secondAmount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            isTwoAmount = true;
            amount2 = secondAmount;
            updateDescription();
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + amount2 + powerStrings.DESCRIPTIONS[amount2 == 1 ? 2 : 3];
        }
  
        public void atStartOfTurn() {
            if (AbstractDungeon.player.drawPile.size() <= 0)
                att(new EmptyDeckShuffleAction()); 
            atb(new AbstractGameAction() {
                public void update() {
                    isDone = true;
                    flash();
                    fromDarkVision = DarkvisionPower.this;
                }
            });
            atb(new ScryAction(amount));
        }

        public void gainTheEnergy(int discarded) {
            if (discarded >= amount2) {
                flash();
                att(new GainEnergyAction(1));
            }
        }

        @SpirePatch(clz=ScryAction.class, method="update")
        public static class GetBlock {
            @SpireInsertPatch(rloc=27)
            public static void Insert() {
                if (fromDarkVision != null)
                    fromDarkVision.gainTheEnergy(AbstractDungeon.gridSelectScreen.selectedCards.size());
            }

            public static void Postfix(ScryAction __instance) {
                if (__instance.isDone)
                    fromDarkVision = null;
            }
        }
    }
}