package bogwarden.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.stance.WrathStanceChangeParticle;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import basemod.ReflectionHacks;

public class BroilingFlames extends AbstractBogCard {
    public final static String ID = makeID("BroilingFlames");

    public BroilingFlames() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        setDamage(14, +4);
        isMultiDamage = true;
        cardsToPreview = new Burn();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        float startX = p.hb.cX;
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                CardCrawlGame.sound.play("STANCE_ENTER_WRATH");
                forAllMonstersLiving(mo -> {
                    for(int i = 0; i < 20; ++i) {
                        WrathStanceChangeParticle particle = new WrathStanceChangeParticle(0);
                        ReflectionHacks.setPrivate(particle, WrathStanceChangeParticle.class, "x", (float)ReflectionHacks.getPrivate(particle, WrathStanceChangeParticle.class, "x") + mo.hb.cX - startX);
                        AbstractDungeon.effectsQueue.add(particle);
                    }
                }); 
            } 
        });
        allDmg(AbstractGameAction.AttackEffect.FIRE);
        makeInHand(cardsToPreview);
    }
}