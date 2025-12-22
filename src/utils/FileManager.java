package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class FileManager {


    public static boolean createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            return true;
        }
        return folder.mkdirs();
    }


    public static boolean createFolderStructure(String... folders) {
        for (String folder : folders) {
            if (!createFolder(folder)) {
                return false;
            }
        }
        return true;
    }

    public static boolean deleteAllFilesInFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return false;
        }

        for (File file : files) {
            if (file.isFile()) {
                if (!file.delete()) {
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean deleteFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file.getAbsolutePath());
                } else {
                    file.delete();
                }
            }
        }
        return folder.delete();
    }


    public static boolean folderExists(String folderPath) {
        File folder = new File(folderPath);
        return folder.exists() && folder.isDirectory();
    }


    public static List<File> getFilesWithExtension(String folderPath, String extension) {
        List<File> files = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return files;
        }

        File[] fileList = folder.listFiles((dir, name) -> name.endsWith(extension));
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }

        return files;
    }


    public static List<File> getAllFiles(String folderPath) {
        List<File> files = new ArrayList<>();
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            return files;
        }

        File[] fileList = folder.listFiles();
        if (fileList != null) {
            for (File file : fileList) {
                if (file.isFile()) {
                    files.add(file);
                }
            }
        }

        return files;
    }

    public static int countFiles(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return 0;
        }

        File[] files = folder.listFiles();
        if (files == null) {
            return 0;
        }

        int count = 0;
        for (File file : files) {
            if (file.isFile()) {
                count++;
            }
        }
        return count;
    }


    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return 0;
        }
        return file.length();
    }

    public static boolean copyFile(String sourcePath, String destinationPath)
            throws IOException {
        File source = new File(sourcePath);
        File destination = new File(destinationPath);

        if (!source.exists() || !source.isFile()) {
            return false;
        }

        Files.copy(source.toPath(), destination.toPath());
        return true;
    }

}