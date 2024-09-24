import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryServiceTest {

    @Test
    void testInitRepo() {
        RepositoryService.initRepo();
        File myGitDir = new File(".mygit");
        assertTrue(myGitDir.exists(), "The .mygit directory should be created.");
        assertTrue(new File(".mygit/commits").exists(), "The commits directory should be created.");
        assertTrue(new File(".mygit/staging").exists(), "The staging directory should be created.");
        assertTrue(new File(".mygit/HEAD").exists(), "The HEAD file should be created.");
    }
}
