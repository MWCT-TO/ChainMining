package zhuanglantong.chain_mining;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import java.util.HashSet;
import java.util.Set;



public class ChainMining implements ModInitializer {
	@Override
	public void onInitialize() {
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (player instanceof ServerPlayerEntity serverPlayer && serverPlayer.getMainHandStack().getItem() == Items.DIAMOND_PICKAXE) {
				int chainCount = breakAdjacentBlocks(world, serverPlayer, pos, state);
				if (chainCount > 1) {
					Block block = state.getBlock();
					String blockName = block.getName().getString();
					Text message = Text.of("[CM] 连锁了 " + chainCount + " 个 " + blockName);
					serverPlayer.sendMessage(message, false);
				}
			}
		});
	}

	private boolean isOre(BlockState state) {
		return state.isOf(Blocks.IRON_ORE) ||
				state.isOf(Blocks.GOLD_ORE) ||
				state.isOf(Blocks.COAL_ORE) ||
				state.isOf(Blocks.COPPER_ORE);
	}
	private int breakAdjacentBlocks(World world, ServerPlayerEntity player, BlockPos pos, BlockState state) {
		int count = 1;
		Set<BlockPos> visited = new HashSet<>();

		breakAdjacentBlocksRecursive(world, player, pos, state, visited);

		return visited.size();
	}

	private void breakAdjacentBlocksRecursive(World world, ServerPlayerEntity player, BlockPos pos, BlockState state, Set<BlockPos> visited) {
		visited.add(pos);

		for (Direction direction : Direction.values()) {
			BlockPos adjacentPos = pos.offset(direction);
			BlockState adjacentState = world.getBlockState(adjacentPos);

			if (adjacentState.isOf(state.getBlock()) && !visited.contains(adjacentPos)) {
				world.breakBlock(adjacentPos, true, player);
				breakAdjacentBlocksRecursive(world, player, adjacentPos, state, visited);
			}
		}
	}

}
