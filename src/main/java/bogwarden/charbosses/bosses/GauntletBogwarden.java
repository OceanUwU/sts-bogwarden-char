package bogwarden.charbosses.bosses;

import basemod.ReflectionHacks;
import bogwarden.cards.Bonfire;
import bogwarden.cards.DeathWard;
import bogwarden.cards.Defend;
import bogwarden.cards.SpinyShawl;
import bogwarden.cards.Strike;
import bogwarden.characters.TheBogwarden;
import bogwarden.powers.AbstractBogPower;
import bogwarden.powers.SpinesEnemy;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import downfall.actions.NeowRezAction;
import downfall.monsters.NeowBossSelector;
import downfall.monsters.gauntletbosses.GauntletBoss;
import downfall.powers.gauntletpowers.MonsterVigor;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeCharacterPath;
import static bogwarden.util.Wiz.*;

public class GauntletBogwarden extends GauntletBoss {
    public static String ID = makeID("BogwardenDownfallGauntlet");

    private static final int SPINES_FROM_SHAWL = 2;
    private static final int DEATH_WARD_AMOUNT = 3;

    private int turnNum = 0;

    public GauntletBogwarden(float x, float y) {
        super(TheBogwarden.NAMES[0], ID, TheBogwarden.MAX_HP * 2, 0f, -20f, 260f, 260f, null, x, y);

        loadAnimation(makeCharacterPath("mainChar/bogwarden.atlas"), makeCharacterPath("mainChar/bogwarden.json"), 1f / TheBogwarden.SIZE_SCALE);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        flipHorizontal = true;
        skeleton.setFlip(flipHorizontal, flipVertical);
        stateData.setMix("hit", "idle", 0.5F);
        e.setTimeScale(TheBogwarden.ANIMATION_SPEED);
        type = EnemyType.ELITE;

        this.damage.add(new DamageInfo(this, 6));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        Bonfire bonfire = new Bonfire();
        bonfire.upgrade();
        atb(new ApplyPowerAction(this, this, new OnDeathBogEveryone(this, bonfire.baseMagicNumber), bonfire.baseMagicNumber));
    }

    public void takeTurn() {
        int dex = pwrAmt(this, DexterityPower.POWER_ID);
        switch (nextMove) {
            case 1:
                atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                if (hasPower(MonsterVigor.POWER_ID))
                    atb(new RemoveSpecificPowerAction(this, this, MonsterVigor.POWER_ID));
                break;
            case 2:
                atb(new DamageAction(AbstractDungeon.player, damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                atb(new GainBlockAction(this, 5 + dex));
                if (hasPower(MonsterVigor.POWER_ID))
                    atb(new RemoveSpecificPowerAction(this, this, MonsterVigor.POWER_ID));
                break;
            case 3:
                atb(new GainBlockAction(this, (5 + dex) * 2));
                break;
            case 4:
                atb(new ApplyPowerAction(this, this, new SpinesEnemy(this, SPINES_FROM_SHAWL), SPINES_FROM_SHAWL));
                break;
            case 5:
                atb(new ApplyPowerAction(this, this, new DeathWard.DeathWardPower(this, DEATH_WARD_AMOUNT), DEATH_WARD_AMOUNT));
                break;
        }

        atb(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        turnNum++;
        if (turnNum == 5) {
            isAttacking = false;
            setMove(moveName(DeathWard.ID), (byte)5, Intent.BUFF);
        } else {
            if (isThird && turnNum > 1 && ally1 != null && ally2 != null) {
                if (!ally1.isDeadOrEscaped() && !ally2.isDeadOrEscaped() && ally1.isAttacking && ally2.isAttacking)
                    setMove(moveName(Defend.ID, Defend.ID), (byte)3, Intent.DEFEND);
                else
                    bossMove();
            } else
                bossMove();
        }
    }

    private void bossMove() {
        int rnd = AbstractDungeon.cardRandomRng.random(0, 3);
        switch (rnd) {
            case 0:
                isAttacking = false;
                setMove(moveName(Strike.ID, Strike.ID), (byte) 1, Intent.ATTACK, this.damage.get(0).base, 2, true);
                break;
            case 1:
                isAttacking = false;
                setMove(moveName(Strike.ID, Defend.ID), (byte) 2, Intent.ATTACK_DEFEND, this.damage.get(1).base);
                break;
            case 2:
                isAttacking = false;
                setMove(moveName(Defend.ID, Defend.ID), (byte) 3, Intent.DEFEND);
                break;
            case 3:
                isAttacking = false;
                setMove(moveName(SpinyShawl.ID), (byte) 4, Intent.BUFF);
                break;
        }
    }

    public static class OnDeathBogEveryone extends AbstractBogPower {
        public static String POWER_ID = makeID("OnDeathBogEveryone");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

        public OnDeathBogEveryone(AbstractCreature owner, int amount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            loadRegion("curiosity");
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1];
        }

        public void onDeath() {
            flash();
            atb(new AddTemporaryHPAction(adp(), owner, amount));
            forAllMonstersLiving(mo -> atb(new AddTemporaryHPAction(mo, owner, amount)));
        }
    }

    @SpirePatch(clz=NeowBossSelector.class, method="validClass", requiredModId="downfall")
    public static class MakeFindable1 {
        public static SpireReturn<Boolean> Prefix(String key) {
            if (key.equals(CharBossBogwarden.ID))
                return SpireReturn.Return(true);
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz=NeowBossSelector.class, method="charbossToGauntlet", requiredModId="downfall")
    public static class MakeFindable2 {
        public static SpireReturn<String> Prefix(String key) {
            if (key.equals(CharBossBogwarden.ID))
                return SpireReturn.Return(ID);
            else return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz=NeowRezAction.class, method="rezBoss", requiredModId="downfall")
    public static class MakeFindable3 {
        public static SpireReturn<AbstractMonster> Prefix(NeowRezAction __instance, String name, int index) {
            if (name.equals(ID))
                return SpireReturn.Return((AbstractMonster)new GauntletBogwarden((float)(int)ReflectionHacks.privateStaticMethod(NeowRezAction.class, "locationSwitch", int.class).invoke(new Object[] {index}), -20f));
            else return SpireReturn.Continue();
        }
    }
}