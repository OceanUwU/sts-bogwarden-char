package bogwarden.cards;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisParticle;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Sap extends AbstractBogCard {
    public final static String ID = makeID("Sap");

    public Sap() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        setUpgradedCost(0);
        setExhaust(true);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        atb(new AbstractGameAction() {
            public void update() {
                isDone = true;
                int energy = (int)m.powers.stream().filter(po -> po.type.equals(AbstractPower.PowerType.DEBUFF)).count();
                att(new GainEnergyAction(energy));
                for (int i = 0; i < energy; i++) {
                    HemokinesisParticle particle = new HemokinesisParticle(m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY, m.hb.cX > p.hb.cX);
                    ReflectionHacks.setPrivate(particle, AbstractGameEffect.class, "color", new Color(0.41f, 1f, 0.26f, 0.6f));
                    particle.renderBehind = false;
                    vfxTop(particle, i == energy - 1 ? 0.2f : 0f);
                }
            }
        });
    }
}