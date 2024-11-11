package filemanager;

import java.util.HashMap;
import java.io.IOException;

public interface FileManager {
    HashMap<String, HashMap<String, ?>> loadFiles() throws IOException;
    void saveFile(HashMap<String, HashMap<String, ?>> dataMap) throws IOException;
}
