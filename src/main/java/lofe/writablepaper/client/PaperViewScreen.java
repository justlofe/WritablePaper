package lofe.writablepaper.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public final class PaperViewScreen extends BookViewScreen {

    public static final ResourceLocation PAPER_LOCATION = ResourceLocation.fromNamespaceAndPath("writablepaper", "textures/gui/paper.png");

    public PaperViewScreen(BookAccess bookAccess) {
        super(bookAccess);
    }

    public void renderBackground(@NotNull GuiGraphics guiGraphics, int p_296491_, int p_294260_, float p_294869_) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(PAPER_LOCATION, (this.width - 192) / 2, 2, 0, 0, 192, 192);
    }

}
