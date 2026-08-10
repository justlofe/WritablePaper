package lofe.writablepaper.mixin.client;

import lofe.writablepaper.client.PaperViewScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundOpenBookPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void handleOpenBook(ClientboundOpenBookPacket packet) {
        ClientPacketListener object = (ClientPacketListener) (Object) this;

        Minecraft minecraft = Minecraft.getInstance();
        PacketUtils.ensureRunningOnSameThread(packet, object, minecraft);
        ItemStack itemstack = minecraft.player.getItemInHand(packet.getHand());
        BookViewScreen.BookAccess bookviewscreen$bookaccess = BookViewScreen.BookAccess.fromItem(itemstack);
        if (bookviewscreen$bookaccess != null) {
            minecraft.setScreen(
                    itemstack.is(Items.WRITTEN_BOOK)
                            ? new BookViewScreen(bookviewscreen$bookaccess)
                            : new PaperViewScreen(bookviewscreen$bookaccess)
            );
        }
    }

}
