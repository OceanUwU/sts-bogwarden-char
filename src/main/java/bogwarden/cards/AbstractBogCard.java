package bogwarden.cards;

import basemod.abstracts.CustomCard;
import bogwarden.characters.TheBogwarden;
import bogwarden.patches.FlashAtkImgPatches;
import bogwarden.patches.NonAttackDamagePatches;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.patches.FlavorText.AbstractCardFlavorFields;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.function.Consumer;

import static bogwarden.BogMod.makeImagePath;
import static bogwarden.BogMod.modID;
import static bogwarden.util.Wiz.atb;
import static bogwarden.util.Wiz.att;

public abstract class AbstractBogCard extends CustomCard {
    protected static AbstractGameAction.AttackEffect BLAST_EFFECT = FlashAtkImgPatches.BOGWARDEN_BLAST_EFFECT;
    protected final CardStrings cardStrings;
    private static final Color FLAVOUR_BOX_COLOR = new Color(0.53f, 0.37f, 0.69f, 1f);

    public int secondMagic = -1;
    public int baseSecondMagic = -1;
    public boolean upgradedSecondMagic;
    public boolean isSecondMagicModified;

    public int thirdMagic = -1;
    public int baseThirdMagic = -1;
    public boolean upgradedThirdMagic;
    public boolean isThirdMagicModified;

    public int secondDamage = -1;
    public int baseSecondDamage = -1;
    public boolean upgradedSecondDamage;
    public boolean isSecondDamageModified;

    public boolean upgradesDamage = false;
    public int damageUpgrade;
    public boolean upgradesBlock = false;
    public int blockUpgrade;
    public boolean upgradesMagic = false;
    public int magicUpgrade;
    public boolean upgradesSecondMagic = false;
    public int secondMagicUpgrade;
    public boolean upgradesThirdMagic = false;
    public int thirdMagicUpgrade;
    private boolean upgradesSecondDamage = false;
    private int secondDamageUpgrade;
    public boolean upgradesCost = false;
    public int costUpgrade;
    public boolean upgradesExhaust = false;
    public boolean upgradedExhaust;
    public boolean upgradesEthereal = false;
    public boolean upgradedEthereal;
    public boolean upgradesInnate = false;
    public boolean upgradedInnate;
    public boolean upgradesRetain = false;
    public boolean upgradedRetain;

    public AbstractBogCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target) {
        this(cardID, cost, type, rarity, target, TheBogwarden.Enums.OCEAN_BOGWARDEN_COLOR);
    }

    public AbstractBogCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target, final CardColor color) {
        super(cardID, "", getCardTextureString(cardID.replace(modID + ":", ""), type),
                cost, "", type, color, rarity, target);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        rawDescription = cardStrings.DESCRIPTION;
        name = originalName = cardStrings.NAME;
        initializeTitle();
        initializeDescription();
        AbstractCardFlavorFields.boxColor.set(this, FLAVOUR_BOX_COLOR);
        AbstractCardFlavorFields.textColor.set(this, Color.WHITE);
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType) {
        String textureString = makeImagePath("cards/" + cardName + ".png");
        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists())
            switch (cardType) {
                case ATTACK:
                    textureString = makeImagePath("cards/betaattack.png");
                    break;
                case POWER:
                    textureString = makeImagePath("cards/betapower.png");
                    break;
                default:
                    textureString = makeImagePath("cards/betaskill.png");
                    break;
            }
        return textureString;
    }

    @Override
    public void applyPowers() {
        if (baseSecondDamage > -1) {
            secondDamage = baseSecondDamage;

            int tmp = baseDamage;
            baseDamage = baseSecondDamage;

            super.applyPowers();

            secondDamage = damage;
            baseDamage = tmp;

            super.applyPowers();

            isSecondDamageModified = (secondDamage != baseSecondDamage);
        } else super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (baseSecondDamage > -1) {
            secondDamage = baseSecondDamage;

            int tmp = baseDamage;
            baseDamage = baseSecondDamage;

            super.calculateCardDamage(mo);

            secondDamage = damage;
            baseDamage = tmp;

            super.calculateCardDamage(mo);

            isSecondDamageModified = (secondDamage != baseSecondDamage);
        } else super.calculateCardDamage(mo);
    }

    public void resetAttributes() {
        super.resetAttributes();
        secondMagic = baseSecondMagic;
        isSecondMagicModified = false;
        thirdMagic = baseThirdMagic;
        isThirdMagicModified = false;
        secondDamage = baseSecondDamage;
        isSecondDamageModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondMagic) {
            secondMagic = baseSecondMagic;
            isSecondMagicModified = true;
        }
        if (upgradedThirdMagic) {
            thirdMagic = baseThirdMagic;
            isThirdMagicModified = true;
        }
        if (upgradedSecondDamage) {
            secondDamage = baseSecondDamage;
            isSecondDamageModified = true;
        }
    }

    protected void upgradeSecondMagic(int amount) {
        baseSecondMagic += amount;
        secondMagic = baseSecondMagic;
        upgradedSecondMagic = true;
    }

    protected void upgradeThirdMagic(int amount) {
        baseThirdMagic += amount;
        thirdMagic = baseThirdMagic;
        upgradedThirdMagic = true;
    }

    protected void upgradeSecondDamage(int amount) {
        baseSecondDamage += amount;
        secondDamage = baseSecondDamage;
        upgradedSecondDamage = true;
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upp();
        }
    }

    protected void setDamage(int base, int up) {
        setDamage(base);
        upgradesDamage = true;
        damageUpgrade = up;
    }

    protected void setDamage(int base) {
        baseDamage = base; 
    }

    protected void setBlock(int base, int up) {
        setBlock(base);
        upgradesBlock = true;
        blockUpgrade = up;
    }

    protected void setBlock(int base) {
        baseBlock = base; 
    }

    protected void setMagic(int base, int up) {
        setMagic(base);
        upgradesMagic = true;
        magicUpgrade = up;
    }

    protected void setMagic(int base) {
        baseMagicNumber = magicNumber = base; 
    }

    protected void setSecondMagic(int base, int up) {
        setSecondMagic(base);
        upgradesSecondMagic = true;
        secondMagicUpgrade = up;
    }

    protected void setSecondMagic(int base) {
        baseSecondMagic = secondMagic = base; 
    }

    protected void setThirdMagic(int base, int up) {
        setThirdMagic(base);
        upgradesThirdMagic = true;
        thirdMagicUpgrade = up;
    }

    protected void setThirdMagic(int base) {
        baseThirdMagic = thirdMagic = base; 
    }

    protected void setSecondDamage(int base, int up) {
        setSecondDamage(base);
        upgradesSecondDamage = true;
        secondDamageUpgrade = up;
    }

    protected void setSecondDamage(int base) {
        baseSecondDamage = secondDamage = base; 
    }

    protected void setExhaust(boolean exhausts, boolean exhaustsWhenUpgraded) {
        setExhaust(exhausts);
        upgradesExhaust = true;
        upgradedExhaust = exhaustsWhenUpgraded;
    }

    protected void setExhaust(boolean exhausts) {
        exhaust = exhausts;
    }

    protected void setEthereal(boolean ethereal, boolean etherealWhenUpgraded) {
        setEthereal(ethereal);
        upgradesEthereal = true;
        upgradedEthereal = etherealWhenUpgraded;
    }

    protected void setEthereal(boolean exhausts) {
        isEthereal = exhausts;
    }

    protected void setInnate(boolean innate, boolean innateWhenUpgraded) {
        setInnate(innate);
        upgradesInnate = true;
        upgradedInnate = innateWhenUpgraded;
    }

    protected void setInnate(boolean innate) {
        isInnate = innate;
    }

    protected void setRetain(boolean retains, boolean retainsWhenUpgraded) {
        setRetain(retains);
        upgradesRetain = true;
        upgradedRetain = retainsWhenUpgraded;
    }

    protected void setRetain(boolean retains) {
        selfRetain = retains;
    }

    protected void setUpgradedCost(int newCost) {
        upgradesCost = true;
        costUpgrade = newCost;
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
        if (upgradesSecondDamage)
            upgradeSecondDamage(secondDamageUpgrade);
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

    // These shortcuts are specifically for cards. All other shortcuts that aren't specifically for cards can go in Wiz.
    protected void dmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn);
        NonAttackDamagePatches.DamageInfoFields.fromCard.set(info, true);
        atb(new DamageAction(m, info, fx));
    }

    protected void dmgTop(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn);
        NonAttackDamagePatches.DamageInfoFields.fromCard.set(info, true);
        att(new DamageAction(m, info, fx));
    }

    private AbstractGameAction dmgRandomAction(AbstractGameAction.AttackEffect fx, Consumer<AbstractMonster> extraEffectToTarget, Consumer<AbstractMonster> effectBefore) {
        return new AbstractGameAction() {
            public void update() {
                isDone = true;
                AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (target != null) {
                    calculateCardDamage(target);
                    DamageInfo info = new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn);
                    NonAttackDamagePatches.DamageInfoFields.fromCard.set(info, true);
                    if (extraEffectToTarget != null)
                        extraEffectToTarget.accept(target);
                    att(new DamageAction(target, info, fx));
                    if (effectBefore != null)
                        effectBefore.accept(target);
                }
            }
        };
    }

    protected void dmgRandom(AbstractGameAction.AttackEffect fx) {
        dmgRandom(fx, null, null);
    }

    protected void dmgRandom(AbstractGameAction.AttackEffect fx, Consumer<AbstractMonster> extraEffectToTarget, Consumer<AbstractMonster> effectBefore) {
        atb(dmgRandomAction(fx, extraEffectToTarget, effectBefore));
    }

    protected void dmgRandomTop(AbstractGameAction.AttackEffect fx) {
        dmgRandomTop(fx, null, null);
    }

    protected void dmgRandomTop(AbstractGameAction.AttackEffect fx, Consumer<AbstractMonster> extraEffectToTarget, Consumer<AbstractMonster> effectBefore) {
        att(dmgRandomAction(fx, extraEffectToTarget, effectBefore));
    }

    protected void allDmg(AbstractGameAction.AttackEffect fx) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx);
        NonAttackDamagePatches.DamageAllFields.fromCard.set(action, true);
        atb(action);
    }

    protected void allDmgTop(AbstractGameAction.AttackEffect fx) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx);
        NonAttackDamagePatches.DamageAllFields.fromCard.set(action, true);
        att(action);
    }

    protected void altDmg(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        atb(new DamageAction(m, new DamageInfo(AbstractDungeon.player, secondDamage, damageTypeForTurn), fx));
    }

    protected void blck() {
        atb(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    protected void blckTop() {
        att(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    protected boolean isEliteOrBoss() {
        boolean retVal = (AbstractDungeon.getCurrRoom()).eliteTrigger;
        for (AbstractMonster mo : (AbstractDungeon.getMonsters()).monsters)
            if (mo.type == AbstractMonster.EnemyType.BOSS)
                retVal = true;
        return retVal;
    }

    public void onDiscardedViaScry() {
        
    }

    public String cardArtCopy() {
        return null;
    }

    protected void upMagic(int x) {
        upgradeMagicNumber(x);
    }

    protected void upSecondMagic(int x) {
        upgradeSecondMagic(x);
    }

    protected void upSecondDamage(int x) {
        upgradeSecondDamage(x);
    }
}