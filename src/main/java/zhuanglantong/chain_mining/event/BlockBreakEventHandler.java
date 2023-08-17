//package zhuanglantong.chain_mining.event;
//
//
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.text.Text;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.world.World;
//import net.minecraft.util.ActionResult;
//
//import net.minecraft.util.ActionResult;
//import net.minecraft.world.World;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//
//public class BlockBreakEventHandler {
//
//    public static ActionResult onBlockBreak(BlockState state, World world, BlockPos pos, PlayerEntity player, Direction direction) {
//        if (player.isSneaking() && player.isCreative()) {
//            Block block = state.getBlock();
//            if (isOre(block)) {
//                if (!world.isClient()) {
//                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
//                    int chainCount = breakAdjacentBlocks(world, serverPlayer, pos, block);
//                    Text message = Text.of("[ChainMining] 连锁了" + chainCount + "个方块");
//                    player.sendMessage(message, false);
//                }
//            }
//        }
//        return ActionResult.PASS;
//    }
//    private static boolean isOre(Block block) {
//        // 判断方块是否为矿石 (ore)
//        // 返回 true 或 false
//        // 你可以根据方块的特性进行判断，比如通过方块的标签或名称
//        // 例如，可以使用 block.getTags() 方法获取方块的标签，然后判断是否包含"forge:ores"标签
//        return false;
//    }
//
//    private static int breakAdjacentBlocks(World world, ServerPlayerEntity player, BlockPos pos, Block block) {
//        // 实现你的连锁破坏方块的逻辑
//        // 返回连锁破坏的方块数量
//        return 0;
//    }
//
//}