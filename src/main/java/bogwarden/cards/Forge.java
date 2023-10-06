package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Forge extends AbstractBogCard {
    public final static String ID = makeID("Forge");

    public Forge() {
        super(ID, -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF, CardColor.COLORLESS);
        setMagic(8, +2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        onChoseThisOption();
    }

    public void onChoseThisOption() {
        vfx(new UpgradeShineEffect(adp().hb.cX, adp().hb.cY));
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                for (AbstractCard c : adp().hand.getAttacks().group) {
                    c.superFlash();
                    att(new ModifyDamageAction(c.uuid, magicNumber));
                }
            }
        });
    }

    /*public static class ForgePower extends AbstractBogPower {
        public static String POWER_ID = makeID("ForgePower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    
        public ForgePower(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        }
        
        public void updateDescription() {
            description = amount == 1 ? powerStrings.DESCRIPTIONS[0] : (powerStrings.DESCRIPTIONS[1] + amount + powerStrings.DESCRIPTIONS[2]);
        }

        public void atStartOfTurnPostDraw() {
            flash();
            atb(new ArmamentsAction(true));
            atb(new ReducePowerAction(owner, owner, this, 1));
        }
    }*/
}