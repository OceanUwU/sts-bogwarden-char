package bogwarden.cards;

import bogwarden.util.BogAudio;
import bogwarden.util.TexLoader;
import bogwarden.vfx.BugSwarmEffect;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SneckoField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;
import static bogwarden.util.Wiz.*;

public class Hoodwink extends AbstractBogCard {
    public final static String ID = makeID("Hoodwink");

    public Hoodwink() {
        super(ID, -1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY);
        SneckoField.snecko.set(this, true);
        setDamage(5, +2);
        setSecondMagic(2);
    }

    public void applyPowers() {
        super.applyPowers();
        baseMagicNumber = magicNumber = Math.max(cost, 0) + secondMagic;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyPowers();
        for (int i = 0; i < magicNumber; i++) {
            final boolean first = i == 0;
            dmgRandom(AbstractGameAction.AttackEffect.SLASH_VERTICAL, mo -> vfxTop(new BugSwarmEffect(5, mo.hb.cX, mo.hb.cY, TexLoader.getTexture(makeImagePath("vfx/bee.png")), first ? BogAudio.BUGS : null)), null);
        }
    }
}