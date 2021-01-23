import java.util.concurrent.*;

public class ThreadDemo {

    static {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> System.out.println("Thread " + thread + " got exception " + throwable));
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,
                1,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        // submit 有返回值，需要查看结果用 get，execute 没有，表示不关心结果
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            executor.execute(() -> {
                if (finalI == 5 || finalI == 6) {
                    throw new RuntimeException();
                }
            });
//            Future<?> future = executor.submit(new Runnable() {
//                @Override
//                public void run() {
//                    if (finalI == 5 || finalI == 6) {
//                        throw new RuntimeException();
//                    }
//                }
//            });
        }
        executor.shutdown();

    }
}
