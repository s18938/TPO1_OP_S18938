package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {

    public Futil() {
    }


    public static void processDir(String startDirName, String resultFileName) {
        try {
            Files.walkFileTree(Paths.get(startDirName), new FileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    FileChannel chanelReader = FileChannel.open(file);
                    ByteBuffer buf = ByteBuffer.allocate((int) chanelReader.size());
                    chanelReader.read(buf);
                    chanelReader.close();
                    buf.flip();
                    FileChannel chanelWriter = FileChannel.open(Paths.get(resultFileName),StandardOpenOption.CREATE, StandardOpenOption.WRITE);// new RandomAccessFile(resultFile, "rw").getChannel();
                    chanelWriter.write(ByteBuffer.wrap(new String(buf.array(), Charset.forName("Cp1250")).getBytes(StandardCharsets.UTF_8)), chanelWriter.size());
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
