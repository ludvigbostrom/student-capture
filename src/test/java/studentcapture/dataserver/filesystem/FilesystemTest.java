package studentcapture.dataserver.filesystem;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilesystemTest {

	@Test
	public void testGeneratePathWithoutStudent() {
		String path = FileSystem.generatePath("5DV151", 1, 123);
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/");
	}
	
	@Test
	public void testGeneratePathWithStudent() {
		String path = FileSystem.generatePath("5DV151", 1, 123, 654);
		
		assertEquals(path, FilesystemConstants.FILESYSTEM_PATH 
					 + "/5DV151/1/123/654/");
	}

}
