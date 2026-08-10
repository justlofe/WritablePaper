package lofe.writablepaper.mixin.client;

import lofe.writablepaper.client.PaperViewScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(BookViewScreen.class)
public abstract class BookViewScreenMixin extends Screen {

    @Shadow
    private int cachedPage;

    @Shadow
    private int currentPage;

    @Shadow
    private BookViewScreen.BookAccess bookAccess;

    @Shadow
    private List<FormattedCharSequence> cachedPageComponents;

    @Shadow
    @Nullable
    public abstract Style getClickedComponentStyleAt(double mouseX, double mouseY);

    protected BookViewScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "render", at = {@At(value = "HEAD")}, cancellable = true)
    public void render(GuiGraphics guiGraphics, int x, int y, float p_282251_, CallbackInfo ci) {
        if(!(((BookViewScreen) (Object) this) instanceof PaperViewScreen)) return;

        ci.cancel();

        super.render(guiGraphics, x, y, p_282251_);
        int i = (this.width - 192) / 2;
        int j = 2;
        if (cachedPage != currentPage) {
            FormattedText formattedtext = bookAccess.getPage(this.currentPage);
            cachedPageComponents = this.font.split(formattedtext, 114);
        }

        this.cachedPage = this.currentPage;
        int k = Math.min(14, this.cachedPageComponents.size());

        for(int l = 0; l < k; ++l) {
            FormattedCharSequence formattedcharsequence = this.cachedPageComponents.get(l);
            guiGraphics.drawString(this.font, formattedcharsequence, i + 36, 32 + l * 9, 0, false);
        }

        Style style = getClickedComponentStyleAt(x, y);
        if (style != null) {
            guiGraphics.renderComponentHoverEffect(this.font, style, x, y);
        }
    }

}
