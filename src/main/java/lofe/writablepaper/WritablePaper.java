package lofe.writablepaper;

import com.mojang.logging.LogUtils;
import lofe.writablepaper.item.WritablePaperItem;
import lofe.writablepaper.item.WrittenPaperItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(WritablePaper.MODID)
public final class WritablePaper {

    public static final String MODID = "writablepaper";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredItem<WritablePaperItem> WRITABLE_PAPER = ITEMS.registerItem("writable_paper", WritablePaperItem::new);
    public static final DeferredItem<WrittenPaperItem> WRITTEN_PAPER = ITEMS.registerItem("written_paper", WrittenPaperItem::new);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WRITABLE_PAPER_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder().title(Component.translatable("itemGroup.writablepaper")).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> WRITABLE_PAPER.get().getDefaultInstance()).displayItems((parameters, output) -> {
        output.accept(WRITABLE_PAPER.get());
    }).build());

    public WritablePaper(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

}
