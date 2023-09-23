package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class CorpseFlies extends AbstractBogCard {
    public final static String ID = makeID("CorpseFlies");

    public CorpseFlies() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(6, +2);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new CorpseFliesAction(m, new DamageInfo(p, damage, damageTypeForTurn), upgraded, this));
    }

    @Override
    public void upp() {
        super.upp();
        cardsToPreview = new CorpseFlies();
    }

    public static class CorpseFliesAction extends AbstractGameAction {
        private boolean upgraded;
        private DamageInfo info;
        private AbstractCard from;
        
        public CorpseFliesAction(AbstractCreature target, DamageInfo info, boolean upgraded, AbstractCard from) {
            this.info = info;
            setValues(target, info);
            this.upgraded = upgraded;
            this.from = from;
            actionType = AbstractGameAction.ActionType.DAMAGE;
            duration = 0.1F;
        }
        
        public void update() {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AbstractGameAction.AttackEffect.NONE));
            target.damage(info);
            if ((target.isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower("Minion")) {
                if (upgraded)
                    AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(new CorpseFlies(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                else {
                    for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
                        if (c.uuid == from.uuid) {
                            c.upgrade();
                            AbstractDungeon.player.bottledCardUpgradeCheck(c);
                            AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
                            AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
                            addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                            break;
                        }
                }
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }
}