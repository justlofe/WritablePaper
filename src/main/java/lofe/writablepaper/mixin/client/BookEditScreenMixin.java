package lofe.writablepaper.mixin.client;

import lofe.writablepaper.client.PaperEditScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings({"SpellCheckingInspection"})
@Mixin(BookEditScreen.class)
public abstract class BookEditScreenMixin extends Screen {

    @Unique
    private static final Component writablepaper$EDIT_TITLE_LABEL = Component.translatable("paper.editTitle");
    @Unique
    private static final Component writablepaper$FINALIZE_WARNING_LABEL = Component.translatable("paper.finalizeWarning");

    @Unique
    private static final Method writablepaper$method_getDisplayCache, writablepaper$method_renderCursor;
    @Unique
    private static final Field
            writablepaper$field_lines, writablepaper$field_selection, writablepaper$field_cursor, writablepaper$field_cursorAtEnd,
            writablepaper$field_asComponent, writablepaper$field_x, writablepaper$field_y;

    static {
        try {
            writablepaper$method_getDisplayCache = BookEditScreen.class.getDeclaredMethod("getDisplayCache");
            writablepaper$method_renderCursor = BookEditScreen.class.getDeclaredMethod(
                    "renderCursor",
                    GuiGraphics.class,
                    Class.forName("net.minecraft.client.gui.screens.inventory.BookEditScreen$Pos2i"),
                    boolean.class
            );
            writablepaper$method_getDisplayCache.setAccessible(true);
            writablepaper$method_renderCursor.setAccessible(true);

            Class<?> displayCacheClass = Class.forName("net.minecraft.client.gui.screens.inventory.BookEditScreen$DisplayCache");
            writablepaper$field_lines = displayCacheClass.getDeclaredField("lines");
            writablepaper$field_selection = displayCacheClass.getDeclaredField("selection");
            writablepaper$field_cursor = displayCacheClass.getDeclaredField("cursor");
            writablepaper$field_cursorAtEnd = displayCacheClass.getDeclaredField("cursorAtEnd");
            writablepaper$field_lines.setAccessible(true);
            writablepaper$field_selection.setAccessible(true);
            writablepaper$field_cursor.setAccessible(true);
            writablepaper$field_cursorAtEnd.setAccessible(true);

            Class<?> lineInfoClass = Class.forName("net.minecraft.client.gui.screens.inventory.BookEditScreen$LineInfo");
            writablepaper$field_asComponent = lineInfoClass.getDeclaredField("asComponent");
            writablepaper$field_x = lineInfoClass.getDeclaredField("x");
            writablepaper$field_y = lineInfoClass.getDeclaredField("y");
            writablepaper$field_asComponent.setAccessible(true);
            writablepaper$field_x.setAccessible(true);
            writablepaper$field_y.setAccessible(true);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    protected BookEditScreenMixin(Component title) {
        super(title);
    }

    @Shadow @Final private static FormattedCharSequence BLACK_CURSOR;
    @Shadow @Final private static FormattedCharSequence GRAY_CURSOR;

    @Shadow private String title;
    @Shadow private boolean isSigning;
    @Shadow private int frameTick;

    @Shadow
    @Final
    private Component ownerText;

    @Shadow
    protected abstract void renderHighlight(GuiGraphics guiGraphics, Rect2i[] rectangles);

    @Inject(method = "render", at = {@At("HEAD")}, cancellable = true)
    public void render(GuiGraphics guiGraphics, int p_282965_, int p_283294_, float p_281293_, CallbackInfo ci) {
        if(!(((BookEditScreen) (Object) this) instanceof PaperEditScreen)) return;

        ci.cancel();

        super.render(guiGraphics, p_282965_, p_283294_, p_281293_);
        this.setFocused(null);
        int i = (this.width - 192) / 2;

        if (isSigning) {
            boolean flag = frameTick / 6 % 2 == 0;
            FormattedCharSequence formattedcharsequence = FormattedCharSequence.composite(FormattedCharSequence.forward(title, Style.EMPTY), flag ? BLACK_CURSOR : GRAY_CURSOR);
            int k = this.font.width(writablepaper$EDIT_TITLE_LABEL);
            guiGraphics.drawString(this.font, writablepaper$EDIT_TITLE_LABEL, i + 36 + (114 - k) / 2, 34, 0, false);
            int l = this.font.width(formattedcharsequence);
            guiGraphics.drawString(this.font, formattedcharsequence, i + 36 + (114 - l) / 2, 50, 0, false);
            int i1 = this.font.width(ownerText);
            guiGraphics.drawString(this.font, ownerText, i + 36 + (114 - i1) / 2, 60, 0, false);
            guiGraphics.drawWordWrap(this.font, writablepaper$FINALIZE_WARNING_LABEL, i + 36, 82, 114, 0);
        }
        else {
            BookEditScreen bookEditScreen = (BookEditScreen) (Object) this;

            try {
                Object bookeditscreen$displaycache = writablepaper$method_getDisplayCache.invoke(bookEditScreen);
                Object[] array = (Object[]) writablepaper$field_lines.get(bookeditscreen$displaycache);

                for (Object bookeditscreen$lineinfo : array) {
                    guiGraphics.drawString(
                            font,
                            (Component) writablepaper$field_asComponent.get(bookeditscreen$lineinfo),
                            (int) writablepaper$field_x.get(bookeditscreen$lineinfo),
                            (int) writablepaper$field_y.get(bookeditscreen$lineinfo),
                            -16777216,
                            false
                    );
                }

                renderHighlight(guiGraphics, (Rect2i[]) writablepaper$field_selection.get(bookeditscreen$displaycache));
                writablepaper$method_renderCursor.invoke(bookEditScreen, guiGraphics, writablepaper$field_cursor.get(bookeditscreen$displaycache), (boolean) writablepaper$field_cursorAtEnd.get(bookeditscreen$displaycache));
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

}
