package lofe.writablepaper.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class PaperEditScreen extends BookEditScreen {
    public PaperEditScreen(Player p_98076_, ItemStack p_98077_, InteractionHand p_98078_) {
        super(p_98076_, p_98077_, p_98078_);
    }

    // restricts adding page buttons
    @SuppressWarnings("unchecked")
    @Override
    protected <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(@NotNull T widget) {
        if(widget instanceof PageButton) return (T) new PageButton(
                0,
                0,
                true,
                (button) -> {},
                false
        );
        return super.addRenderableWidget(widget);
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int p_295019_, int p_294307_, float p_295562_) {
        this.renderTransparentBackground(guiGraphics);
        guiGraphics.blit(PaperViewScreen.PAPER_LOCATION, (this.width - 192) / 2, 2, 0, 0, 192, 192);
    }

}
