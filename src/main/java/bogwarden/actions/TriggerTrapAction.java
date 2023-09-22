package bogwarden.actions;

import basemod.ReflectionHacks;
import bogwarden.cards.AbstractTrapCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.ArrayList;

public class TriggerTrapAction extends AbstractGameAction {
    public static ArrayList<AbstractGameAction> saveActions;
    private AbstractMonster target;

    public TriggerTrapAction(AbstractCreature target) {
        if (target instanceof AbstractMonster)
            this.target = (AbstractMonster)target;
    }

    public TriggerTrapAction() {}

    public void update() {
        isDone = true;
        if (target == null || target.isDeadOrEscaped())
            target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (c instanceof AbstractTrapCard) {
                c.dontTriggerOnUseCard = true;
                CardQueueItem q = new CardQueueItem(c, true);
                q.monster = target;
                AbstractDungeon.actionManager.cardQueue.add(q);
                saveActions = AbstractDungeon.actionManager.actions;
                AbstractDungeon.actionManager.actions = new ArrayList<>();
                ReflectionHacks.privateMethod(GameActionManager.class, "getNextAction").invoke(AbstractDungeon.actionManager);
                return;
            }
    }
}