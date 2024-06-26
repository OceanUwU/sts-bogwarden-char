package bogwarden.cards;

import bogwarden.powers.AbstractBogPower;
import bogwarden.vfx.OpenEyesEffect;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

import basemod.ReflectionHacks;

public class Darkvision extends AbstractBogCard {
    public final static String ID = makeID("Darkvision");

    public Darkvision() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        setMagic(2, +1);
        setSecondMagic(2);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new DarkvisionPower(p, magicNumber, secondMagic));
    }

    public static class DarkvisionPower extends AbstractBogPower implements NonStackablePower {
        public static String POWER_ID = makeID("DarkvisionPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        private static DarkvisionPower fromDarkVision;

        private OpenEyesEffect eyes;
    
        public DarkvisionPower(AbstractCreature owner, int amount, int secondAmount) {
            super(POWER_ID, powerStrings.NAME, PowerType.BUFF, false, owner, amount);
            isTwoAmount = true;
            amount2 = secondAmount;
            updateDescription();
        }
        
        public void updateDescription() {
            description = powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1] + amount2 + powerStrings.DESCRIPTIONS[amount2 == 1 ? 2 : 3];
        }
  
        public void atStartOfTurn() {
            eyes = new OpenEyesEffect(new Color(0.66f, 0.35f, 0.80f, 1f), true, false, false, 1.4f);
            vfx(eyes);
            if (AbstractDungeon.player.drawPile.size() <= 0)
                att(new EmptyDeckShuffleAction()); 
            atb(new AbstractGameAction() {
                public void update() {
                    isDone = true;
                    flash();
                    fromDarkVision = DarkvisionPower.this;
                }
            });
            atb(new ScryAction(amount));
            atb(new AbstractGameAction() {
                public void update() {
                    isDone = true;
                    eyes.canGoPastHalf = true;
                }
            });
        }

        public void gainTheEnergy(int discarded) {
            if (discarded >= amount2) {
                flash();
                att(new GainEnergyAction(1));
                SmallLaserEffect laser = new SmallLaserEffect(Settings.WIDTH / 2f, Settings.HEIGHT / 2f, owner.hb.cX, owner.hb.cY);
                ReflectionHacks.setPrivate(laser, AbstractGameEffect.class, "color", new Color(0.99f, 0.94f, 0.33f, 1f));
                vfxTop(laser);
                att(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.6f, true));
            }
        }

        @SpirePatch(clz=ScryAction.class, method="update")
        public static class GetBlock {
            @SpireInsertPatch(rloc=27)
            public static void Insert() {
                if (fromDarkVision != null)
                    fromDarkVision.gainTheEnergy(AbstractDungeon.gridSelectScreen.selectedCards.size());
            }

            public static void Postfix(ScryAction __instance) {
                if (__instance.isDone)
                    fromDarkVision = null;
            }
        }
    }
}