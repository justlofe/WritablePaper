package lofe.writablepaper.item;

import lofe.writablepaper.client.PaperEditScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.WrittenBookContent;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public final class WritablePaperItem extends Item {

    public WritablePaperItem(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY));
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if(level.isClientSide())
            Minecraft.getInstance().setScreen(new PaperEditScreen(player, stack, hand));

        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

}
