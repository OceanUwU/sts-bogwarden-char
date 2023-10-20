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
    public static ArrayList<AbstractGameAction> saveActions = new ArrayList<>();
    private AbstractMonster target;
    private int times;

    public TriggerTrapAction(AbstractCreature target, int times) {
        this(times);
        if (target instanceof AbstractMonster)
            this.target = (AbstractMonster)target;
    }

    public TriggerTrapAction(int times) {
        this.times = times;
    }

    public TriggerTrapAction(AbstractCreature target) {
        this(1);
        if (target instanceof AbstractMonster)
            this.target = (AbstractMonster)target;
    }

    public TriggerTrapAction() {
        this(1);
    }

    public void update() {
        isDone = true;
        if (target != null && target.isDeadOrEscaped())
            target = null;
        System.out.println("trying!");
        System.out.println(this);
        for (AbstractCard c : AbstractDungeon.player.hand.group)
            if (c instanceof AbstractTrapCard && !((AbstractTrapCard)c).using) {
                c.dontTriggerOnUseCard = true;
                ((AbstractTrapCard)c).using = true;
                ((AbstractTrapCard)c).timesToTrigger = times;
                CardQueueItem q = new CardQueueItem(c, true);
                q.monster = target;
                AbstractDungeon.actionManager.addCardQueueItem(q, true);
                saveActions.addAll(AbstractDungeon.actionManager.actions);
                AbstractDungeon.actionManager.actions.clear();
                ReflectionHacks.privateMethod(GameActionManager.class, "getNextAction").invoke(AbstractDungeon.actionManager);
                return;
            }
    }
}