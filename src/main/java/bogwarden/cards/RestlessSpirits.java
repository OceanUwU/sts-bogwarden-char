package bogwarden.cards;


import bogwarden.powers.AbstractBogPower;
import bogwarden.vfx.IncantationEffect;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class RestlessSpirits extends AbstractBogCard {
    public final static String ID = makeID("RestlessSpirits");

    public RestlessSpirits() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setInnate(false, true);
        cardsToPreview = new Blast();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new RestlessSpiritsPower(p, 1));
    }

    public static class RestlessSpiritsPower extends AbstractBogPower {
        public static String POWER_ID = makeID("RestlessSpiritsPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public RestlessSpiritsPower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[amount == 1 ? 1 : 2];
        }
  
        public void atStartOfTurn() {
            flash();
            vfx(new IncantationEffect());
            atb(new MakeTempCardInHandAction(new Blast(), amount, false));
        }
    }
}