package bogwarden.cards;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Implode extends AbstractBogCard {
    public final static String ID = makeID("Implode");

    public Implode() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);
        setRetain(false, true);
        setBlock(11, +1);
        setSecondMagic(11);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new FlashAtkImgEffect(p.hb.cX, p.hb.cY, BLAST_EFFECT));
        blck();
        atb(new GainBlockAction(AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng), p, secondMagic));
    }
}