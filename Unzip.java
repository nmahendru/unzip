/**
Adapted from:
https://www.mkyong.com/java/how-to-decompress-files-from-a-zip-file/

*/
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Unzip{
    public static void main(String[] args) {
        if(args.length != 1){
            System.out.println("Custom unzip command only takes two arguments");
            System.out.println("usage:  unzip <filename>");
            System.exit(-1);
        }
        String zipFilePath = args[0];


        String destDirectory = new File(zipFilePath).getAbsoluteFile().getParent();

        UnzipUtility unzipper = new UnzipUtility();
        try {
            unzipper.unzip(zipFilePath, destDirectory);
        } catch (Exception ex) {
            // some errors occurred
            ex.printStackTrace();
        }
    }
}



/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 *
 */
class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    private void createDir(File path){
        if(!path.exists()){
            if(!path.mkdir()){
                File temp = path.getParentFile();
                createDir(temp);
                if(!path.exists()) {
                    path.mkdir();
                }
            }
        }
    }

    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = null;
        Path t = Paths.get(filePath);
        t = t.toAbsolutePath();
        t = t.getParent();
        createDir(t.toFile());
        try {
            bos = new BufferedOutputStream(new FileOutputStream(filePath));
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
