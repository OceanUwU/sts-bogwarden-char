package bogwarden.util;

import aspiration.Aspiration;
import aspiration.relics.skillbooks.SkillbookRelic;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import bogwarden.characters.TheBogwarden;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.vfx.combat.OmegaFlashEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class AspirationSkillbookAdder {
    public static void add() {
        BaseMod.addRelic(new BogwardenSkillbook(), RelicType.SHARED);
    }

    private static class BogwardenSkillbook extends SkillbookRelic {
        public static final String ID = makeID("BogwardenSkillbook");
    
        public static final int CARD_THRESHOLD = 6;
        public static final int DAMAGE = 10;
    
        public BogwardenSkillbook() {
            super(ID, "BogwardenSkillbook.png", RelicTier.BOSS, LandingSound.FLAT);
        }
    
        @Override
        public String getUpdatedDescription() {
            String desc = DESCRIPTIONS[1] + CARD_THRESHOLD + DESCRIPTIONS[2] + DAMAGE + DESCRIPTIONS[3];
            if(Aspiration.skillbookCardpool())
                desc += DESCRIPTIONS[0];
            return desc;
        }
    
        @Override
        public void onEquip() {
            modifyCardPool();
        }
    
        public void modifyCardPool() {
            if(Aspiration.skillbookCardpool())
                mixCardpools(CardLibrary.getCardList(TheBogwarden.Enums.LIBRARY_COLOR));
        }
      
        public void atTurnStart() {
            beginPulse();
            pulse = true;
            counter = CARD_THRESHOLD;
        }
      
        public void onUseCard(AbstractCard card, UseCardAction action) {
            if (counter > 0 && --counter <= 0)
                pulse = false;
        }
    
        public void onPlayerEndTurn() {
            if (counter > 0) {
                flash();
                pulse = false;
                atb(new RelicAboveCreatureAction(adp(), this));
                forAllMonstersLiving(mo -> vfx(new OmegaFlashEffect(mo.hb.cX, mo.hb.cY), Settings.FAST_MODE ? 0f : 0.2f));
                atb(new DamageAllEnemiesAction(null,DamageInfo.createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
            }
        }
      
        public void onVictory() {
            this.counter = -1;
            pulse = false;
        }
    
        @Override
        public boolean canSpawn() {
            return !(AbstractDungeon.player instanceof TheBogwarden) && !hasSkillbookRelic(AbstractDungeon.player);
        }
    }
}