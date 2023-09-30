package bogwarden.cards;


import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.LoseMojoPower;
import bogwarden.powers.Mojo;
import bogwarden.util.BogAudio;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class HexingTotem extends AbstractTrapCard {
    public final static String ID = makeID("HexingTotem");

    public HexingTotem() {
        super(ID, CardRarity.UNCOMMON);
        setMagic(3, +2);
        setSecondMagic(2);
        setExhaust(true);
        sfx = BogAudio.TOTEM_TRIGGER;
        tags.add(CardTags.HEALING);
    }

    public void trigger(AbstractPlayer p, AbstractMonster m) {
        applyToSelfTop(new NextTurnTemporaryMojo(p, secondMagic));
        att(new HealAction(p, p, magicNumber));
    }

    public static class NextTurnTemporaryMojo extends AbstractBogPower {
        public static String POWER_ID = makeID("NextTurnTemporaryMojo");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public NextTurnTemporaryMojo(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            priority = 0;
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }
  
        public void atStartOfTurn() {
            flash();
            applyToSelf(new Mojo(owner, amount));
            applyToSelf(new LoseMojoPower(owner, amount));
            atb(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }
}