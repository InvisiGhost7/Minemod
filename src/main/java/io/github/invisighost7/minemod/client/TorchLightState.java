package io.github.invisighost7.minemod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class TorchLightState {
    private static volatile Snapshot current = Snapshot.empty();

    private TorchLightState() {
    }

    public static void update(Minecraft client) {
        Snapshot previous = current;

        if (client.level == null || client.player == null) {
            current = Snapshot.empty();
            return;
        }

        int lightLevel = getOffhandTorchLightLevel(client.player.getOffhandItem());
        Snapshot next;
        if (lightLevel <= 0) {
            next = Snapshot.empty();
        } else {
            BlockPos sourcePos = BlockPos.containing(client.player.getX(), client.player.getY() + 1.0D, client.player.getZ());
            next = new Snapshot(sourcePos, lightLevel);
        }

        current = next;
        if (client.levelRenderer == null || previous.equals(next)) {
            return;
        }

        refreshNearbySections(client.levelRenderer, previous);
        refreshNearbySections(client.levelRenderer, next);
    }

    public static Snapshot current() {
        return current;
    }

    private static void refreshNearbySections(LevelRenderer levelRenderer, Snapshot snapshot) {
        if (snapshot.lightLevel() <= 0) {
            return;
        }

        BlockPos pos = snapshot.sourcePos();
        int radius = 16;
        levelRenderer.setBlocksDirty(
            pos.getX() - radius,
            pos.getY() - radius,
            pos.getZ() - radius,
            pos.getX() + radius,
            pos.getY() + radius,
            pos.getZ() + radius
        );
        levelRenderer.needsUpdate();
    }

    private static int getOffhandTorchLightLevel(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }

        if (stack.getItem() == Items.TORCH || stack.getItem() == Items.COPPER_TORCH) {
            return 14;
        }

        if (stack.getItem() == Items.SOUL_TORCH) {
            return 10;
        }

        if (stack.getItem() == Items.REDSTONE_TORCH) {
            return 7;
        }

        return 0;
    }

    public record Snapshot(BlockPos sourcePos, int lightLevel) {
        private static Snapshot empty() {
            return new Snapshot(BlockPos.ZERO, 0);
        }
    }
}
