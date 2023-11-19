package bogwarden.cards;

import basemod.AutoAdd;
import bogwarden.powers.Mojo;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

@AutoAdd.Ignore
public class DollsCurse extends AbstractBogCard {
    public final static String ID = makeID("DollsCurse");

    public DollsCurse() {
        super(ID, -2, CardType.CURSE, CardRarity.SPECIAL, CardTarget.NONE, CardColor.CURSE);
        setMagic(1);
        setSecondMagic(2);
        SoulboundField.soulbound.set(this, true);
    }

    public void triggerWhenDrawn() {
        applyToSelfTop(new Mojo(adp(), magicNumber));
    }
  
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard)
            att(new LoseHPAction(p, p, secondMagic));
    }
    
    public void triggerOnEndOfTurnForPlayingCard() {
        this.dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    public void upgrade() {}
}