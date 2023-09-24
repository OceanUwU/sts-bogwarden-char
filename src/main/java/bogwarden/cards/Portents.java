package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

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

        public void dealTheDamage(int discarded) {
            flash();
            for (int i = 0; i < discarded; i++)
                atb(new DamageRandomEnemyAction(new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }

        @SpirePatch(clz=ScryAction.class, method="update")
        public static class DealDamage {
            @SpireInsertPatch(rloc=27)
            public static void Insert() {
                if (adp().hasPower(POWER_ID))
                    ((PortentsPower)adp().getPower(POWER_ID)).dealTheDamage(AbstractDungeon.gridSelectScreen.selectedCards.size());
            }
        }
    }
}