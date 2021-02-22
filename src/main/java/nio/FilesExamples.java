package nio;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;

public class FilesExamples {
    public static void main(String[] args) throws IOException {
        StandardCopyOption co;
        StandardOpenOption oo;
        StandardCharsets sc;

        //CREATE - пересоздание
        //APPEND - дописывание

        Files.write(Paths.get("client/2.txt"), "Hello World.".getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE);//файлик будет пересоздан

        Files.copy(Paths.get("client/2.txt"), Paths.get("client/4.txt"),
                StandardCopyOption.REPLACE_EXISTING); //не важно был файл или не было, всегда будем перезаписывать


        Files.walkFileTree(Paths.get("./"), new HashSet<>(), 3, // зайдет на глубину в 3 папки у каждого Path
                new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        System.out.println(file); //напечатает все Path
                                                  //можно накрутить логику при прохождении по всем Path
                        return super.visitFile(file, attrs);
                    }
                });
    }
}
