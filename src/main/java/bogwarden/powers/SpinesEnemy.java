package bogwarden.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import static bogwarden.BogMod.makeID;

public class SpinesEnemy extends AbstractBogPower {
    public static final String POWER_ID = makeID("SpinesEnemy");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SpinesEnemy(AbstractCreature owner, int amount) {
        super(Spines.POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
        ID = POWER_ID;
    }

    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == CardType.SKILL) {
            addToTop(new DamageAction(AbstractDungeon.player, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            flash();
        }
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
    }
}
