package bogwarden.cards;

import bogwarden.powers.Mojo;
import bogwarden.relics.DuVuNeedle;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.NecronomicurseEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class DollsCurse extends AbstractBogCard {
    public final static String ID = makeID("DollsCurse");

    public DollsCurse() {
        super(ID, -2, CardType.CURSE, CardRarity.SPECIAL, CardTarget.NONE, CardColor.CURSE);
        setMagic(1);
        setSecondMagic(3);
        SoulboundField.soulbound.set(this, true);
    }
  
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
            applyToSelfTop(new Mojo(p, magicNumber));
            att(new LoseHPAction(p, p, secondMagic));
        }
    }
  
    public void onRemoveFromMasterDeck() {
        if (AbstractDungeon.player.hasRelic(DuVuNeedle.ID))
            AbstractDungeon.player.getRelic(DuVuNeedle.ID).flash(); 
        AbstractDungeon.effectsQueue.add(new NecronomicurseEffect(makeCopy(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
    }
    
    public void triggerOnExhaust() {
        if (AbstractDungeon.player.hasRelic(DuVuNeedle.ID))
            AbstractDungeon.player.getRelic(DuVuNeedle.ID).flash(); 
        addToBot(new MakeTempCardInHandAction(this));
    }
    
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }
}