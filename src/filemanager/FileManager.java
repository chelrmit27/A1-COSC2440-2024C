package filemanager;

import java.util.HashMap;
import java.io.IOException;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public interface FileManager {
    void deleteAllDataFiles();
    HashMap<String, HashMap<String, ?>> loadFiles() throws IOException;
    void saveFile(HashMap<String, HashMap<String, ?>> dataMap) throws IOException;
}
