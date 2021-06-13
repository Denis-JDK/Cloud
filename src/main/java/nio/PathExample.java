package nio;




import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class PathExample {
    public static void main(String[] args) throws IOException {

        String p = "1/2/3/n.txt";
        Path path = Paths.get("client");
        Path path1 = Paths.get("logo.txt");

        System.out.println(path.getParent());
        System.out.println(path.toAbsolutePath().getParent());
        System.out.println(path.resolve(path1));


        WatchService service = FileSystems.getDefault().newWatchService();
        new Thread(() -> {   //повесили наблюдателя на папку и отслежываем изменения в ней
            while (true) {
                WatchKey key = null;
                try {
                    key = service.take();
                    List<WatchEvent<?>> events = key.pollEvents();
                    if (key.isValid()) {
                        for (WatchEvent<?> event : events) {
                            // можно реализовать логику синхронизации с сервером
                            System.out.println(event.count() + " " + event.kind() + " " + event.context());
                        }

                    }

                    key.reset();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        path.register(service, StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_DELETE,
                StandardWatchEventKinds.ENTRY_MODIFY);
    }
}







