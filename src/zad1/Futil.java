package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public static void processDir(String startDirName, String resultFileName) {
        try {
            FileChannel.open(Paths.get(resultFileName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE).write(ByteBuffer.allocate(0));
            Files.walkFileTree(Paths.get(startDirName), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileChannel chanelReader = FileChannel.open(file, StandardOpenOption.READ);
                    ByteBuffer buf = ByteBuffer.allocate((int) chanelReader.size());
                    chanelReader.read(buf);
                    chanelReader.close();
                    buf.flip();
                    FileChannel chanelWriter = FileChannel.open(Paths.get(resultFileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                    chanelWriter.write(Charset.forName("UTF-8").encode(Charset.forName("Cp1250").decode(buf)), chanelWriter.size());
                    chanelWriter.close();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
