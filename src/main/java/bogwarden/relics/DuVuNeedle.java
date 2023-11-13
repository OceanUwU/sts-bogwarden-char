package bogwarden.relics;

import basemod.helpers.CardPowerTip;
import bogwarden.cards.DollsCurse;
import bogwarden.characters.TheBogwarden;
import bogwarden.powers.Mojo;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static bogwarden.BogMod.makeID;

public class DuVuNeedle extends AbstractBogRelic {
    public static final String ID = makeID("DuVuNeedle");
    private static int MOJO_PER_CURSE = 1;

    public DuVuNeedle() {
        super(ID, RelicTier.SPECIAL, LandingSound.MAGICAL, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
        tips.add(new CardPowerTip(new DollsCurse()));
    }
  
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + MOJO_PER_CURSE + DESCRIPTIONS[1];
    }
    
    public void setCounter() {
        counter = 0;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group)
            if (c.type == AbstractCard.CardType.CURSE)
                counter += MOJO_PER_CURSE;
        if (counter == 0)
            description = DESCRIPTIONS[0] + MOJO_PER_CURSE + DESCRIPTIONS[1] + DESCRIPTIONS[2];
        else
            description = DESCRIPTIONS[0] + MOJO_PER_CURSE + DESCRIPTIONS[1] + DESCRIPTIONS[3] + (counter / MOJO_PER_CURSE) + DESCRIPTIONS[counter / MOJO_PER_CURSE == 1 ? 4 : 5];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new CardPowerTip(new DollsCurse()));
        initializeTips();
    }
    
    public void onMasterDeckChange() {
        setCounter();
    }
    
    public void onEquip() {
        setCounter();
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new DollsCurse(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
    }
    
    public void atBattleStart() {
        if (counter > 0) {
            flash();
            addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new Mojo(AbstractDungeon.player, counter), counter));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        } 
    }
}