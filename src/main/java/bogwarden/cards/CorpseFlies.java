package bogwarden.cards;

import bogwarden.util.TexLoader;
import bogwarden.vfx.BugSwarmEffect;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

import bogwarden.util.BogAudio;

public class CorpseFlies extends AbstractBogCard {
    public final static String ID = makeID("CorpseFlies");

    private boolean real;

    public CorpseFlies() {
        this(true);
    }

    public CorpseFlies(boolean real) {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(6, +2);
        setExhaust(true);
        this.real = real;
        tags.add(CardTags.HEALING);
        if (real) {
            cardsToPreview = new CorpseFlies(false);
            cardsToPreview.upgrade();
        }
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new BugSwarmEffect((int)AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().filter(c -> c instanceof CorpseFlies).count() * 3, m.hb.cX, m.hb.cY, TexLoader.getTexture(makeImagePath("vfx/corpsefly.png")), BogAudio.BUGS));
        atb(new CorpseFliesAction(m, new DamageInfo(p, damage, damageTypeForTurn), upgraded, this));
        if (upgraded)
            atb(new DrawCardAction(1));
            /*atb(new AbstractGameAction() {
                public void update() {
                    isDone = true;
                    CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    p.drawPile.group.stream().filter(c -> c instanceof CorpseFlies).forEach(c -> cards.addToRandomSpot(c));
                    cards.shuffle(AbstractDungeon.cardRandomRng);
                    if (cards.size() > 0)
                        att(new MoveCardsAction(p.hand, p.drawPile, c -> c == cards.getBottomCard(), 1));
                }
            });*/
    }

    @Override
    public void upp() {
        super.upp();
        if (real)
            cardsToPreview = new CorpseFlies(false);
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
            isDone = true;
            if (target == null || target.isDeadOrEscaped())
                return;
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
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
                    if (from.canUpgrade())
                        from.upgrade();
                }
            }
            if ((AbstractDungeon.getCurrRoom()).monsters.areMonstersBasicallyDead())
                AbstractDungeon.actionManager.clearPostCombatActions();
        }
    }
}