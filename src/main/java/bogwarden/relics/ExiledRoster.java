package bogwarden.relics;

import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Maledict;
import bogwarden.powers.Venom;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class ExiledRoster extends AbstractBogRelic {
    public static final String ID = makeID("ExiledRoster");
    private static final int STACKS = 3;

    public ExiledRoster() {
        super(ID, RelicTier.RARE, LandingSound.FLAT, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + STACKS + DESCRIPTIONS[1];
    }
  
    public void atBattleStart() {
        boolean isEliteOrBoss = (AbstractDungeon.getCurrRoom()).eliteTrigger;
        for (AbstractMonster m : (AbstractDungeon.getMonsters()).monsters)
            if (m.type == AbstractMonster.EnemyType.BOSS)
                isEliteOrBoss = true; 
        if (isEliteOrBoss) {
            flash();
            atb(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            forAllMonstersLiving(mo -> {
                applyToEnemy(mo, new Maledict(mo, STACKS));
                applyToEnemy(mo, new Venom(mo, STACKS));
            });
        }
    } 
}