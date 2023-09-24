package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
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
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DarkvisionPower(p, magicNumber));
    }

    public static class DarkvisionPower extends AbstractBogPower {
        public static String POWER_ID = makeID("DarkvisionPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        private static boolean fromDarkVision = false;
    
        public DarkvisionPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2];
        }
  
        public void atStartOfTurn() {
            flash();
            if (AbstractDungeon.player.drawPile.size() <= 0)
                att(new EmptyDeckShuffleAction()); 
            atb(new AbstractGameAction() {
                public void update() {
                    isDone = true;
                    fromDarkVision = true;
                }
            });
            atb(new ScryAction(amount));
        }

        public void gainTheBlock(int discarded) {
            flash();
            for (int i = 0; i < discarded; i++)
                att(new GainBlockAction(owner, owner, amount, true));
        }

        @SpirePatch(clz=ScryAction.class, method="update")
        public static class GetBlock {
            @SpireInsertPatch(rloc=27)
            public static void Insert() {
                if (fromDarkVision) {
                    fromDarkVision = false;
                    if (adp().hasPower(POWER_ID))
                        ((DarkvisionPower)adp().getPower(POWER_ID)).gainTheBlock(AbstractDungeon.gridSelectScreen.selectedCards.size());
                }
            }
        }
    }
}