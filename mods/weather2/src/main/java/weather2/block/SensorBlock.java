package weather2.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import weather2.WeatherBlocks;
import weather2.blockentity.SensorBlockEntity;

import java.util.List;

public class SensorBlock extends BaseEntityBlock {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final void register() {}

	public SensorBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWERED);
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		//tooltip.add(Component.translatable(this.getDescriptionId() + ".desc").withStyle(ChatFormatting.GRAY));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new SensorBlockEntity(p_153215_, p_153216_);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
		return createTickerHelper(p_153214_, WeatherBlocks.BLOCK_ENTITY_TORNADO_SENSOR.get(), SensorBlockEntity::tick);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTicker(final BlockEntityType<A> type, final BlockEntityType<E> tickerType, final BlockEntityTicker<? super E> ticker) {
		return tickerType == type ? (BlockEntityTicker<A>) ticker : null;
	}

	@Override
	public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
		return pState.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
		return pBlockState.getValue(POWERED) ? 15 : 0;
	}

	@Override
	public boolean isSignalSource(BlockState pState) {
		return true;
	}

	public BlockState setPoweredState(BlockState pState, Level pLevel, BlockPos pPos, boolean state) {
		pLevel.setBlock(pPos, pState.setValue(POWERED, Boolean.valueOf(state)), 3);
		pLevel.updateNeighborsAt(pPos, this);
		return pState;
	}

	public BlockState toggle(BlockState pState, Level pLevel, BlockPos pPos) {
		pState = pState.cycle(POWERED);
		pLevel.setBlock(pPos, pState, 3);
		pLevel.updateNeighborsAt(pPos, this);
		return pState;
	}
}
