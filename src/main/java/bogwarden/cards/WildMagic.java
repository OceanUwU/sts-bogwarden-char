package bogwarden.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.MasterReality;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDrawPileEffect;
import java.util.ArrayList;
import java.util.Collections;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import bogwarden.patches.FlashAtkImgPatches;

public class WildMagic extends AbstractBogCard {
    public final static String ID = makeID("WildMagic");

    public WildMagic() {
        super(ID, 2, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        setDamage(11);
        setMagic(3);
    }

    @SuppressWarnings("unchecked")
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, FlashAtkImgPatches.BOGWARDEN_WILD_MAGIC_EFFECT);
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                ArrayList<AbstractCard> group = (ArrayList<AbstractCard>)p.drawPile.group.clone();
                Collections.sort(group);
                att(new SelectCardsAction(group, magicNumber, cardStrings.EXTENDED_DESCRIPTION[0], true, c -> true, cards -> cards.stream().forEach(c -> {
                    if (adp().drawPile.contains(c)) {
                        AbstractCard.CardRarity cardRarity;
                        int roll = AbstractDungeon.cardRandomRng.random(99);
                        if (roll < 55)
                            cardRarity = AbstractCard.CardRarity.COMMON;
                        else if (roll < 85)
                            cardRarity = AbstractCard.CardRarity.UNCOMMON;
                        else
                            cardRarity = AbstractCard.CardRarity.RARE;
                        AbstractCard generated = CardLibrary.getAnyColorCard(AbstractCard.CardType.SKILL, cardRarity).makeStatEquivalentCopy();
                        UnlockTracker.markCardAsSeen(generated.cardID);
                        if ((upgraded || AbstractDungeon.player.hasPower(MasterReality.ID)) && generated.canUpgrade())
                            generated.upgrade();
                        int cPos = adp().drawPile.group.indexOf(c);
                        att(new AbstractGameAction() {
                            public void update() {
                                isDone = true;
                                if (adp().drawPile.contains(generated)) {
                                    adp().drawPile.removeCard(generated);
                                    adp().drawPile.group.add(cPos, generated);
                                }
                            }
                        });
                        att(new VFXAction(new ShowCardAndAddToDrawPileEffect(generated, false, false)));
                        att(new ExhaustSpecificCardAction(c, adp().drawPile, true));
                    }
                })));
            }
        });
    }
}