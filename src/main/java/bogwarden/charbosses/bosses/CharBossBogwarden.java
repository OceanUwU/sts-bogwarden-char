package bogwarden.charbosses.bosses;

import bogwarden.characters.TheBogwarden;
import bogwarden.util.TexLoader;
import charbosses.actions.util.CharBossMonsterGroup;
import charbosses.bosses.AbstractBossDeckArchetype;
import charbosses.bosses.AbstractCharBoss;
import charbosses.core.EnemyEnergyManager;
import charbosses.ui.EnemyEnergyPanel;
import collector.CollectorCollection;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.map.DungeonMap;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import downfall.downfallMod;
import downfall.patches.BossSetPatch;

import static bogwarden.BogMod.makeID;
import static bogwarden.BogMod.makeImagePath;

import basemod.BaseMod;
import bogwarden.BogMod;
import bogwarden.cards.Defend;
import bogwarden.cards.Jinx;
import bogwarden.cards.Strike;

import static bogwarden.BogMod.makeCharacterPath;

public class CharBossBogwarden extends AbstractCharBoss {
    public static String ID = makeID("BogwardenDownfallBoss");

    public CharBossBogwarden() {
        super(TheBogwarden.NAMES[0], ID, TheBogwarden.MAX_HP, 0f, -20f, 260f, 260f, null, 0f, 0f, TheBogwarden.Enums.THE_BOGWARDEN_OCEAN);

        loadAnimation(makeCharacterPath("mainChar/bogwarden.atlas"), makeCharacterPath("mainChar/bogwarden.json"), 1f / TheBogwarden.SIZE_SCALE);
        AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
        flipHorizontal = true;
        skeleton.setFlip(flipHorizontal, flipVertical);
        stateData.setMix("hit", "idle", 0.5F);
        e.setTimeScale(TheBogwarden.ANIMATION_SPEED);
        
        energyOrb = new TheBogwarden.BogEnergyOrb();
        energy = new EnemyEnergyManager(3);
        energyString = "[E]";
        type = EnemyType.BOSS;
    }

    @Override
    public void generateDeck() {
        AbstractBossDeckArchetype archetype = new Act1BogArchetype();
        if (downfallMod.overrideBossDifficulty) {
            downfallMod.overrideBossDifficulty = false;
            currentHealth -= 100;
        } else if (AbstractDungeon.actNum == 2)
            archetype = new Act2BogArchetype();
        else if (AbstractDungeon.actNum == 3)
            archetype = new Act3BogArchetype();

        archetype.initialize();
        chosenArchetype = archetype;
        if (AbstractDungeon.ascensionLevel >= 19)
            archetype.initializeBonusRelic();
    }

    public void damage(DamageInfo info) {
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output - currentBlock > 0) {
            AnimationState.TrackEntry e = state.setAnimation(0, "hit", false);
            AnimationState.TrackEntry e2 = state.addAnimation(0, "idle", true, 0.0F);
            e.setTimeScale(TheBogwarden.ANIMATION_SPEED);
            e2.setTimeScale(TheBogwarden.ANIMATION_SPEED);
        }
        super.damage(info);
    }

    @Override
    public void die() {
        super.die();
        downfallMod.saveBossFight(id);
    }

    @SpirePatch(clz=CollectorCollection.class, method="getCollectedCard", requiredModId="downfall")
    public static class GetCollectedCard {
        public static SpireReturn<AbstractCard> Prefix(AbstractMonster m) {
            if (m instanceof CharBossBogwarden && ((CharBossBogwarden)m).chosenArchetype != null) switch (((CharBossBogwarden)m).chosenArchetype.actNum) {
                case 1:
                    return SpireReturn.Return(CardLibrary.getCopy(Strike.ID));
                case 2:
                    return SpireReturn.Return(CardLibrary.getCopy(Defend.ID));
                case 3:
                default:
                    return SpireReturn.Return(CardLibrary.getCopy(Jinx.ID));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz=downfallMod.class, method="initializeMonsters", requiredModId="downfall")
    public static class Register {
        public static void Postfix() {
            BaseMod.addMonster(CharBossBogwarden.ID, () -> new CharBossMonsterGroup(new AbstractMonster[]{new CharBossBogwarden()}));
        }
    }

    @SpirePatch(clz=downfallMod.class, method="resetBossList", requiredModId="downfall")
    public static class AddToList {
        public static void Postfix() {
            //if (BogMod.doDownfallBoss)
            //    downfallMod.possEncounterList.add(CharBossBogwarden.ID);
        }
    }

    @SpirePatch(clz=EnemyEnergyPanel.class, method=SpirePatch.CONSTRUCTOR, requiredModId="downfall")
    public static class CustomiseEnergyOrb {
        public static void Postfix(EnemyEnergyPanel __instance, AbstractCharBoss owner) {
            if (owner.chosenClass.equals(TheBogwarden.Enums.THE_BOGWARDEN_OCEAN)) {
                __instance.gainEnergyImg = TexLoader.getTexture(makeCharacterPath("mainChar/orb/vfx.png"));
                __instance.energyNumFont = FontHelper.energyNumFontGreen;
            }
        }
    }

    @SpirePatch(clz=BossSetPatch.class, method="Postfix", requiredModId="downfall")
    public static class SetMapImage {
        public static void Postfix(AbstractDungeon __instance, String key) {
            if (key.equals(CharBossBogwarden.ID)) {
                if (__instance instanceof TheBeyond) {
                    DungeonMap.boss = TexLoader.getTexture(makeImagePath("ui/map/DownfallBossAct3.png"));
                    DungeonMap.bossOutline = TexLoader.getTexture(makeImagePath("ui/map/DownfallBossAct3Outline.png"));
                } else if (__instance instanceof TheCity) {
                    DungeonMap.boss = TexLoader.getTexture(makeImagePath("ui/map/DownfallBossAct2.png"));
                    DungeonMap.bossOutline = TexLoader.getTexture(makeImagePath("ui/map/DownfallBossAct1Outline.png"));
                } else {
                    DungeonMap.boss = TexLoader.getTexture(makeImagePath("ui/map/DownfallBossAct2.png"));
                    DungeonMap.bossOutline = TexLoader.getTexture(makeImagePath("ui/map/DownfallBossAct1Outline.png"));
                }
            }
        }
    }
}