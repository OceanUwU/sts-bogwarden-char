package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Portents extends AbstractBogCard {
    public final static String ID = makeID("Portents");

    public Portents() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2, +1);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new PortentsPower(p, magicNumber));
        applyToSelf(new Portents2Power(p, magicNumber));
    }

    public static class PortentsPower extends AbstractBogPower {
        public static String POWER_ID = makeID("PortentsPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public PortentsPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
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
                if (adp().hasPower(POWER_ID))
                    ((PortentsPower)adp().getPower(POWER_ID)).gainTheBlock(AbstractDungeon.gridSelectScreen.selectedCards.size());
            }
        }
    }

    public static class Portents2Power extends AbstractBogPower {
        public static String POWER_ID = makeID("Portents2Power");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public Portents2Power(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void gainTheVigor(int discarded) {
            flash();
            for (int i = 0; i < discarded; i++)
                applyToSelfTop(new VigorPower(owner, amount));
        }

        @SpirePatch(clz=ScryAction.class, method="update")
        public static class GetBlock {
            @SpireInsertPatch(rloc=27)
            public static void Insert() {
                if (adp().hasPower(POWER_ID))
                    ((Portents2Power)adp().getPower(POWER_ID)).gainTheVigor(AbstractDungeon.gridSelectScreen.selectedCards.size());
            }
        }
    }
}