package openoffice.html;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class ImageCache {
	
	private final File directory;
	
	private Set<File> cachedImages;
	
	private boolean autoClean;
	
	
	public ImageCache(File directory, boolean autoClean) {
		this.directory = directory;
		
		cachedImages = new HashSet<File>();
		
		this.autoClean = autoClean;
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				if (!ImageCache.this.autoClean) return;
				
				clean();
			}
		});
	}
	public ImageCache(String directory, boolean autoClean) {
		this(new File(directory), autoClean);
	}
	
	
	public File getDirectory() {
		return directory;
	}
	
	public boolean isAutoClean() {
		return autoClean;
	}
	
	public boolean isCached(String image) {
		File imageFile = new File(directory, image);
		
		return imageFile.isFile();
	}
	
	public File getImage(String image) {
		if (!isCached(image)) return null;
		
		return new File(directory, image);
	}
	
	
	public void setAutoClean(boolean autoClean) {
		this.autoClean = autoClean;
	}
	
	
	public File newImage(String image) {
		File imageFile = new File(directory, image);
		
		if (imageFile.isFile()) imageFile.delete();
		
		cachedImages.add(imageFile);
		
		return imageFile;
	}
	
	
	public void clean() {
		for (File cachedImage : cachedImages) {
			cachedImage.delete();
		}
	}
	
}