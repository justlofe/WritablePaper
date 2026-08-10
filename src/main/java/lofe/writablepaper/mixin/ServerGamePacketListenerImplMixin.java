package lofe.writablepaper.mixin;

import lofe.writablepaper.WritablePaper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.WrittenBookContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    /**
     * @author just_lofe
     * @reason add writable paper to if statement
     */
    @Overwrite
    private void signBook(FilteredText title, List<FilteredText> pages, int index) {
        ServerGamePacketListenerImpl object = (ServerGamePacketListenerImpl) (Object) this;

        System.out.println("In mixin!");
        ItemStack stack = object.player.getInventory().getItem(index);
        if (stack.is(Items.WRITABLE_BOOK) || stack.is(WritablePaper.WRITABLE_PAPER.get())) {
            System.out.println("Passed check!");
            ItemStack newStack = stack.transmuteCopy(stack.is(Items.WRITABLE_BOOK) ? Items.WRITTEN_BOOK : WritablePaper.WRITTEN_PAPER.get());
            newStack.remove(DataComponents.WRITABLE_BOOK_CONTENT);
            List<Filterable<Component>> list = pages.stream()
                    .map((text) -> filterableFromOutgoing(text)
                            .map(Component::literal)
                            .map(component -> (Component) component))
                    .toList();
            newStack.set(DataComponents.WRITTEN_BOOK_CONTENT, new WrittenBookContent(filterableFromOutgoing(title), object.player.getName().getString(), 0, list, true));
            object.player.getInventory().setItem(index, newStack);
        }
    }

    @Shadow
    protected abstract Filterable<String> filterableFromOutgoing(FilteredText filteredText);

}
