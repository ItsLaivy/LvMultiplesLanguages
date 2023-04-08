package codes.laivy.mlanguage.utils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.Set;

public class FileUtils {

    /**
     * Converts a string to file name format
     * @param text the string
     * @return the file name formatted string
     */
    public static @NotNull String fileNameTranslate(@NotNull String text) {
        String fileName = Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", " ");
        fileName = fileName.replaceAll("\\s+", " ");
        fileName = fileName.replaceAll("\\s", "_");
        return fileName;
    }

    /**
     * Gets the files inside a path (and sub paths)
     * @param path the path
     * @return all files inside this path (including sub paths)
     */
    public static @NotNull Set<File> listFiles(@NotNull String path) throws IOException {
        Set<File> archives = new LinkedHashSet<>();
        Path diretorioBase = Paths.get(path);

        FileVisitor<Path> fileVisitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                archives.add(file.toFile());
                return FileVisitResult.CONTINUE;
            }
        };

        Files.walkFileTree(diretorioBase, fileVisitor);
        return archives;
    }

}
