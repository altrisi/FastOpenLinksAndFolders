package altrisi.mods.fastopenresourcepacks.mixin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.util.Util;

@Mixin(Util.OperatingSystem.class)
abstract class OperatingSystemMixin {
	@Shadow
	public abstract void open(URL url);
	
	// Get vanilla logger
	private static final Logger LOGGER = LogManager.getLogger(Util.class);
	
	/**
	 * @author altrisi
	 * @reason Make opening screenshots and chat links non-blocking 
	 */
	@Overwrite
	public void open(URI uri) {
		CompletableFuture.runAsync(() -> {
			try {
				this.open(uri.toURL());
			} catch (MalformedURLException e) {
        	 	LOGGER.error("Couldn't open uri '{}'", uri, e);
         	}
		});
	}
	
	/**
	 * @author altrisi
	 * @reason Make opening resourcepack and datapacks folder non-blocking 
	 */
	@Overwrite
	public void open(File file) {
		CompletableFuture.runAsync(() -> {
			try {
				this.open(file.toURI().toURL());
			} catch (MalformedURLException e) {
        	 	LOGGER.error("Couldn't open file '{}'", file, e);
         	}
		});
	}
}
