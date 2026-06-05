package io.github.invisighost7.minemod;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	public static final String MOD_ID = "minemod";

	@Override
	public void onInitialize() {
		ModBlocks.initialize();
	}
}
