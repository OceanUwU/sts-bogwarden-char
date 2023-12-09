package bogwarden.charbosses.bosses;

import bogwarden.charbosses.cards.*;
import charbosses.bosses.AbstractCharBoss;
import charbosses.powers.bossmechanicpowers.AbstractBossMechanicPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import java.util.ArrayList;

import static bogwarden.BogMod.makeID;
import static bogwarden.util.Wiz.*;

public class Act3BogArchetype extends ArchetypeBaseBogwarden {
    public Act3BogArchetype() {
        super(makeID("BOG_ARCHETYPE_3"), "Act3");

        maxHPModifier += 300;
        actNum = 3;
        bossMechanicName = SpecialPower.NAME;
        bossMechanicDesc = SpecialPower.DESC[0];
    }

    public void addedPreBattle() {
        super.addedPreBattle();
        atb(new ApplyPowerAction(AbstractCharBoss.boss, AbstractCharBoss.boss, new SpecialPower(AbstractCharBoss.boss)));
    }

    public void initialize() {
        //relics
    }

    public ArrayList<AbstractCard> getThisTurnCards() {
        ArrayList<AbstractCard> cardsList = new ArrayList<>();
        boolean extraUpgrades = AbstractDungeon.ascensionLevel >= 4;

        if (!looped) switch (++turn) {
            case 1:
                addToList(cardsList, new EnStrike());
                addToList(cardsList, new EnStrike());
                break;
            case 2:
                addToList(cardsList, new EnStrike());
                addToList(cardsList, new EnStrike());
                addToList(cardsList, new EnStrike(), extraUpgrades);
                loop();
                break;
        } else switch (++turn) {
            case 1:
                addToList(cardsList, new EnStrike());
                break;
            case 2:
                addToList(cardsList, new EnStrike());
                addToList(cardsList, new EnStrike());
                loop();
                break;
        }

        return cardsList;
    }

    public void initializeBonusRelic() {
        //addRelic(new A19Relic());
    }

    public static class SpecialPower extends AbstractBossMechanicPower {
        public static final String POWER_ID = makeID("Act3SpecialPower");
        private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = powerStrings.NAME;
        public static final String[] DESC = powerStrings.DESCRIPTIONS;

        public SpecialPower(AbstractCreature owner) {
            name = NAME;
            ID = POWER_ID;
            this.owner = owner;
            amount = 0;
            loadRegion("curiosity");
            type = PowerType.BUFF;
            updateDescription();
        }

        public void updateDescription() {
            description = DESC[0];
        }
    }
}