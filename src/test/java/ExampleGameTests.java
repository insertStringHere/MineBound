import com.mineboundteam.minebound.MineBound;
import com.mineboundteam.minebound.item.registry.ItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;

@GameTestHolder(MineBound.MOD_ID)
public class ExampleGameTests {

    // Class name is prepended, template name is not specified
    // Template Location at 'modid:exampletest'
    @GameTest
    @PrefixGameTestTemplate(false)
    public static void exampleTest(GameTestHelper helper) {
        Player player = helper.makeMockPlayer();
        player.setItemSlot(EquipmentSlot.HEAD, ItemRegistry.MYRIAL_EFFIGY_HELMET.get().getDefaultInstance());
        player.setItemSlot(EquipmentSlot.CHEST, ItemRegistry.MYRIAL_EFFIGY_CHESTPLATE.get().getDefaultInstance());
        player.setItemSlot(EquipmentSlot.LEGS, ItemRegistry.MYRIAL_EFFIGY_LEGGINGS.get().getDefaultInstance());
        player.setItemSlot(EquipmentSlot.FEET, ItemRegistry.MYRIAL_EFFIGY_BOOTS.get().getDefaultInstance());
        System.out.println("Player Created with Armor");
        helper.assertEntityData(BlockPos.ZERO, EntityType.PLAYER, (playerEntity) -> {
            return playerEntity.getArmorValue() == 20;
        }, true);

    }

}