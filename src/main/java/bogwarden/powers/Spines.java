package bogwarden.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;

public class Spines extends AbstractBogPower {
    public static final String POWER_ID = makeID("Spines");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static int HALF_ATTACK_DIV = 2;

    public Spines(AbstractCreature owner, int amount) {
        super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
    }
    
    public void updateDescription() {
        description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + (amount / HALF_ATTACK_DIV) + powerStrings.DESCRIPTIONS[2];
    }

    public void trigger(AbstractMonster m, int div) {
        addToBot(new AbstractGameAction() {
            public void update() {
                isDone = true;
                flash();
            }
        });
        addToBot(new DamageAction(m, new DamageInfo(owner, amount / div, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
    }

    @SpirePatch(clz=GameActionManager.class, method="getNextAction")
    public static class Trigger {
        private static AbstractMonster.Intent[] excludedIntents = new AbstractMonster.Intent[]{AbstractMonster.Intent.ATTACK};
        private static AbstractMonster.Intent[] halfIntents = new AbstractMonster.Intent[]{AbstractMonster.Intent.ATTACK_BUFF, AbstractMonster.Intent.ATTACK_DEBUFF, AbstractMonster.Intent.ATTACK_DEFEND};
        
        @SpireInsertPatch(rloc=210, localvars={"m"})
        public static void Insert(GameActionManager __instance, AbstractMonster m) {
            if (AbstractDungeon.player.hasPower(POWER_ID)) {
                for (AbstractMonster.Intent i : excludedIntents)
                    if (m.intent == i)
                        return;
                for (AbstractMonster.Intent i : halfIntents)
                    if (m.intent == i) {
                        ((Spines)AbstractDungeon.player.getPower(POWER_ID)).trigger(m, HALF_ATTACK_DIV);
                        return;
                    }
                ((Spines)AbstractDungeon.player.getPower(POWER_ID)).trigger(m, 1);
            }
        }
    }
}