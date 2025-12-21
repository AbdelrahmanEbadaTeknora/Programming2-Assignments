package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file system operations
 * Handles folder creation, file deletion, and directory traversal
 */
public class FileManager {

    /**
     * Creates a folder if it doesn't exist
     */
    public static boolean createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            return true;
        }
        return folder.mkdirs();
    }

    /**
     * Creates folder structure recursively
     */
    public static boolean createFolderStructure(String... folders) {
        for (String folder : folders) {
            if (!createFolder(folder)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Deletes a file
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * Deletes all files in a folder
     */
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

    /**
     * Deletes a folder and all its contents
     */
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

    /**
     * Checks if a file exists
     */
    public static boolean fileExists(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    /**
     * Checks if a folder exists
     */
    public static boolean folderExists(String folderPath) {
        File folder = new File(folderPath);
        return folder.exists() && folder.isDirectory();
    }

    /**
     * Gets all files in a folder with specific extension
     */
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

    /**
     * Gets all files in a folder
     */
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

    /**
     * Counts files in a folder
     */
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

    /**
     * Gets file size in bytes
     */
    public static long getFileSize(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return 0;
        }
        return file.length();
    }

    /**
     * Copies a file
     */
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

    /**
     * Renames a file
     */
    public static boolean renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);

        return oldFile.exists() && oldFile.isFile() && oldFile.renameTo(newFile);
    }

    /**
     * Gets absolute path
     */
    public static String getAbsolutePath(String path) {
        return new File(path).getAbsolutePath();
    }

    /**
     * Gets file extension
     */
    public static String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return "";
    }

    /**
     * Gets file name without extension
     */
    public static String getFileNameWithoutExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0) {
            return fileName.substring(0, lastDot);
        }
        return fileName;
    }
}