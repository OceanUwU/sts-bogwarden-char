package bogwarden.packs;

import bogwarden.BogMod;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import thePackmaster.packs.AbstractCardPack;
import thePackmaster.packs.PackPreviewCard;

import static bogwarden.BogMod.makeCardPath;
import static bogwarden.BogMod.makeImagePath;

public abstract class AbstractBogPack extends AbstractCardPack {
    private String previewArtCardID;

    public AbstractBogPack(String id, String previewArt, AbstractCardPack.PackSummary summary) {
        super(id, CardCrawlGame.languagePack.getUIString(id).TEXT[0], CardCrawlGame.languagePack.getUIString(id).TEXT[1], CardCrawlGame.languagePack.getUIString(id).TEXT[2], summary);
        previewArtCardID = previewArt;
        previewPackCard = this.makePreviewCard();
    }

    public PackPreviewCard makePreviewCard() {
        if (previewArtCardID == null) return super.makePreviewCard();
        return new PackPreviewCard(packID, makeCardPath(previewArtCardID.replace(BogMod.modID + ":", "")+".png"), this);
    }

    public String getHatPath() {
        return makeImagePath("hats/"+packID.replace(BogMod.modID + ":", "")+".png");
    }
}