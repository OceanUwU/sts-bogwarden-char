package bogwarden.cards;

import basemod.abstracts.CustomCard;
import bogwarden.characters.TheBogwarden;
import bogwarden.patches.NonAttackDamagePatches;
import bogwarden.util.CardArtRoller;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.function.Consumer;

import static bogwarden.BogMod.makeImagePath;
import static bogwarden.BogMod.modID;
import static bogwarden.util.Wiz.atb;
import static bogwarden.util.Wiz.att;

public abstract class AbstractBogCard extends CustomCard {

    protected final CardStrings cardStrings;

    public int secondMagic;
    public int baseSecondMagic;
    public boolean upgradedSecondMagic;
    public boolean isSecondMagicModified;

    public int secondDamage;
    public int baseSecondDamage;
    public boolean upgradedSecondDamage;
    public boolean isSecondDamageModified;

    private boolean upgradesDamage = false;
    private int damageUpgrade;
    private boolean upgradesBlock = false;
    private int blockUpgrade;
    private boolean upgradesMagic = false;
    private int magicUpgrade;
    private boolean upgradesSecondMagic = false;
    private int secondMagicUpgrade;
    private boolean upgradesSecondDamage = false;
    private int secondDamageUpgrade;
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

    private boolean needsArtRefresh = false;

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

        if (textureImg.contains("ui/missing.png")) {
            if (CardLibrary.cards != null && !CardLibrary.cards.isEmpty()) {
                CardArtRoller.computeCard(this);
            } else
                needsArtRefresh = true;
        }
    }

    @Override
    protected Texture getPortraitImage() {
        if (textureImg.contains("ui/missing.png")) {
            return CardArtRoller.getPortraitTexture(this);
        } else {
            return super.getPortraitImage();
        }
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType) {
        String textureString;

        switch (cardType) {
            case ATTACK:
            case POWER:
            case SKILL:
                textureString = makeImagePath("cards/" + cardName + ".png");
                break;
            default:
                textureString = makeImagePath("ui/missing.png");
                break;
        }

        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists()) {
            textureString = makeImagePath("ui/missing.png");
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
        secondDamage = baseSecondDamage;
        isSecondDamageModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondMagic) {
            secondMagic = baseSecondMagic;
            isSecondMagicModified = true;
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

    public void update() {
        super.update();
        if (needsArtRefresh) {
            CardArtRoller.computeCard(this);
        }
    }

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

    private AbstractGameAction dmgRandomAction(AbstractGameAction.AttackEffect fx, Consumer<AbstractMonster> extraEffectToTarget) {
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
                }
            }
        };
    }

    protected void dmgRandom(AbstractGameAction.AttackEffect fx) {
        dmgRandom(fx, null);
    }

    protected void dmgRandom(AbstractGameAction.AttackEffect fx, Consumer<AbstractMonster> extraEffectToTarget) {
        atb(dmgRandomAction(fx, extraEffectToTarget));
    }

    protected void dmgRandomTop(AbstractGameAction.AttackEffect fx) {
        dmgRandomTop(fx, null);
    }

    protected void dmgRandomTop(AbstractGameAction.AttackEffect fx, Consumer<AbstractMonster> extraEffectToTarget) {
        att(dmgRandomAction(fx, extraEffectToTarget));
    }

    protected void allDmg(AbstractGameAction.AttackEffect fx) {
        atb(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx));
    }

    protected void allDmgTop(AbstractGameAction.AttackEffect fx) {
        att(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx));
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

    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
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
