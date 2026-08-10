package lofe.writablepaper.item;

import lofe.writablepaper.client.PaperViewScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class WrittenPaperItem extends Item {

    public WrittenPaperItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    public @NotNull Component getName(ItemStack stack) {
        WrittenBookContent writtenBookContent = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent != null) {
            String title = writtenBookContent.title().raw();
            if (!StringUtil.isBlank(title)) return Component.literal(title);
        }

        return super.getName(stack);
    }

    public void appendHoverText(ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> lore, @NotNull TooltipFlag tooltipFlag) {
        WrittenBookContent writtenBookContent = stack.get(DataComponents.WRITTEN_BOOK_CONTENT);
        if (writtenBookContent != null) {
            if (!StringUtil.isBlank(writtenBookContent.author())) {
                lore.add(Component.translatable("book.byAuthor", writtenBookContent.author()).withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide()) Minecraft.getInstance().setScreen(new PaperViewScreen(BookViewScreen.BookAccess.fromItem(stack)));
        else {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            if (WrittenBookItem.resolveBookComponents(stack, serverPlayer.createCommandSourceStack(), serverPlayer)) {
                serverPlayer.containerMenu.broadcastChanges();
            }

            serverPlayer.connection.send(new ClientboundOpenBookPacket(hand));
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

}
