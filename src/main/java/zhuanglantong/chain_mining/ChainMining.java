package zhuanglantong.chain_mining;


import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import java.util.HashSet;
import java.util.Set;



public class ChainMining implements ModInitializer {
	private boolean result;
	@Override
	public void onInitialize() {
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			if (player.isSneaking() && !player.isCreative() && isOre(state)){
				ItemStack heldItem = player.getMainHandStack();
				Item item = heldItem.getItem();
				float itemMiningSpeed = item.getMiningSpeedMultiplier(heldItem, state);
				float blockHardness = state.getHardness(world, pos);

				if (itemMiningSpeed / blockHardness > 1.0) {
					result = true;
					return result;
				} else {
					result = false;
					Text message = Text.of("[CM] 挖掘等级不足，无法连锁挖掘");
					player.sendMessage(message, false);
					return result;
				}
			}
			return true;
		});

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (isOre(state) && result) {
				if (player instanceof ServerPlayerEntity serverPlayer && player.isSneaking()) {
					int chainCount = breakAdjacentBlocks(world, serverPlayer, pos, state);
					if (chainCount > 1) {
						Block block = state.getBlock();
						String blockName = block.getName().getString();
						Text message = Text.of("[CM] 连锁挖掘了 " + chainCount + " 个 " + blockName);
						serverPlayer.sendMessage(message, false);
					}
				}
			}
			if (isWood(state)) {
				if (player instanceof ServerPlayerEntity serverPlayer && player.isSneaking()) {
					int chainCount = breakAdjacentBlocks(world, serverPlayer, pos, state);
					if (chainCount > 1) {
						Block block = state.getBlock();
						String blockName = block.getName().getString();
						Text message = Text.of("[CM] 连锁挖掘了 " + chainCount + " 个 " + blockName);
						serverPlayer.sendMessage(message, false);
					}
				}
			}
		});
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
	private boolean isOre(BlockState state) {
				return state.isOf(Blocks.IRON_ORE) ||
						state.isOf(Blocks.GOLD_ORE) ||
						state.isOf(Blocks.COAL_ORE) ||
						state.isOf(Blocks.COPPER_ORE) ||
						state.isOf(Blocks.DEEPSLATE_COAL_ORE) ||
						state.isOf(Blocks.DEEPSLATE_COPPER_ORE) ||
						state.isOf(Blocks.DEEPSLATE_EMERALD_ORE) ||
						state.isOf(Blocks.DEEPSLATE_GOLD_ORE) ||
						state.isOf(Blocks.DEEPSLATE_IRON_ORE) ||
						state.isOf(Blocks.DEEPSLATE_LAPIS_ORE) ||
						state.isOf(Blocks.DEEPSLATE_REDSTONE_ORE) ||
						state.isOf(Blocks.DIAMOND_ORE) ||
						state.isOf(Blocks.EMERALD_ORE) ||
						state.isOf(Blocks.OBSIDIAN) ||
						state.isOf(Blocks.ANCIENT_DEBRIS) ||
						state.isOf(Blocks.NETHER_GOLD_ORE);
	}

	private boolean isWood(BlockState state) {
		return state.isOf(Blocks.ACACIA_LOG) ||
				state.isOf(Blocks.CHERRY_LOG) ||
				state.isOf(Blocks.BIRCH_LOG) ||
				state.isOf(Blocks.JUNGLE_LOG) ||
				state.isOf(Blocks.DARK_OAK_LOG) ||
				state.isOf(Blocks.OAK_LOG) ||
				state.isOf(Blocks.MANGROVE_LOG)||
				state.isOf(Blocks.SPRUCE_LOG);
	}
}