package bogwarden.charbosses.cards;

import bogwarden.cards.AbstractBogCard;
import bogwarden.util.TexLoader;
import charbosses.cards.AbstractBossCard;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import static bogwarden.BogMod.makeID;

import bogwarden.BogMod;

public abstract class AbstractBogwardenBossCard extends AbstractBossCard {
    protected CardStrings cardStrings;

    public int secondMagic = -1;
    public int thirdMagic = -1;

    private boolean upgradesDamage = false;
    private int damageUpgrade;
    private boolean upgradesBlock = false;
    private int blockUpgrade;
    private boolean upgradesMagic = false;
    private int magicUpgrade;
    private boolean upgradesSecondMagic = false;
    private int secondMagicUpgrade;
    private boolean upgradesThirdMagic = false;
    private int thirdMagicUpgrade;
    private boolean upgradesCost = false;
    private int costUpgrade;
    private boolean upgradesExhaust = false;
    private boolean upgradedExhaust;
    private boolean upgradesEthereal = false;
    private boolean upgradedEthereal;
    private boolean upgradesInnate = false;
    private boolean upgradedInnate;
    private boolean upgradesRetain = false;
    private boolean upgradedRetain;

    public AbstractBogwardenBossCard(AbstractBogCard from, AbstractMonster.Intent intent) {
        super(from.cardID.replace(":", ":En"), from.name, null, from.cost, from.rawDescription, from.type, from.color, from.rarity, from.target, intent, false);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(from.cardID);
        portraitImg = TexLoader.getTexture(AbstractBogCard.getCardTextureString(from.cardID.replace(BogMod.modID + ":", "") + (Settings.PLAYTESTER_ART_MODE || UnlockTracker.betaCardPref.getBoolean(cardID, false) ? "_b" : ""), from.type));
        portrait = new AtlasRegion(portraitImg, 0, 0, 250, 190);

        baseDamage = from.baseDamage;
        baseBlock = from.baseBlock;
        baseMagicNumber = magicNumber = from.baseMagicNumber;
        secondMagic = from.baseSecondMagic;
        thirdMagic = from.baseThirdMagic;

        exhaust = from.exhaust;
        isEthereal = from.isEthereal;
        isInnate = from.isInnate;
        selfRetain = from.selfRetain;
        damageType = damageTypeForTurn = from.damageTypeForTurn;

        upgradesDamage = from.upgradesDamage;
        damageUpgrade = from.damageUpgrade;
        upgradesBlock = from.upgradesBlock;
        blockUpgrade = from.blockUpgrade;
        upgradesMagic = from.upgradesMagic;
        magicUpgrade = from.magicUpgrade;
        upgradesSecondMagic = from.upgradesSecondMagic;
        secondMagicUpgrade = from.secondMagicUpgrade;
        upgradesThirdMagic = from.upgradesThirdMagic;
        thirdMagicUpgrade = from.thirdMagicUpgrade;
        upgradesCost = from.upgradesCost;
        costUpgrade = from.costUpgrade;
        upgradesExhaust = from.upgradesExhaust;
        upgradedExhaust = from.upgradedExhaust;
        upgradesEthereal = from.upgradesEthereal;
        upgradedEthereal = from.upgradedEthereal;
        upgradesInnate = from.upgradesInnate;
        upgradedInnate = from.upgradedInnate;
        upgradedInnate = from.upgradedInnate;
        upgradesRetain = from.upgradesRetain;
        upgradedRetain = from.upgradedRetain;
    }

    @Override
    public void initializeDescription() {
        String origDesc = rawDescription;
        rawDescription = rawDescription.replace(makeID("m2"), String.valueOf(secondMagic))
            .replace(makeID("m3"), String.valueOf(thirdMagic));
        super.initializeDescription();
        rawDescription = origDesc;
    }

    @Override
    public void loadCardImage(String img) {
        super.loadCardImage(img);
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upp();
        }
    }

    public void upp() {
        if (upgradesDamage)
            upgradeDamage(damageUpgrade);
        if (upgradesBlock)
            upgradeBlock(blockUpgrade);
        if (upgradesMagic)
            upgradeMagicNumber(magicUpgrade);
        if (upgradesSecondMagic)
            upgradeSecondMagic(secondMagicUpgrade);
        if (upgradesThirdMagic)
            upgradeThirdMagic(thirdMagicUpgrade);
        if (upgradesCost)
            upgradeBaseCost(costUpgrade);
        if (upgradesExhaust)
            exhaust = upgradedExhaust;
        if (upgradesEthereal)
            isEthereal = upgradedEthereal;
        if (upgradesInnate)
            isInnate = upgradedInnate;
        if (upgradesRetain)
            selfRetain = upgradedRetain;
    };

    protected void upgradeSecondMagic(int amount) {
        secondMagic += amount;
    }

    protected void upgradeThirdMagic(int amount) {
        thirdMagic += amount;
    }

    public AbstractCard makeCopy() {
        try {
            return getClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException("AbstractBogwardenBossCard: Failed to auto-generate makeCopy for card: " + cardID);
        }
    }

    protected void dmg(AbstractPlayer p, AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        atb(new DamageAction(p, new DamageInfo(m, damage, damageTypeForTurn), fx));
    }

    protected void blck(AbstractMonster m) {
        atb(new GainBlockAction(m, m, block));
    }
}