package week4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Homework03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        way1();
//        way2();
//        way3();
//        way4();
//        way5();
        way6();
    }

    private static int sum() {
        return fibo(36);
    }

    private static int fibo(int a) {
        if (a < 2) {
            return 1;
        }
        return fibo(a - 1) + fibo(a - 2);
    }

    private static void way1() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        new Thread(futureTask).start();
        int result = futureTask.get(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }

    private static void way2() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        });
        executor.submit(futureTask);
        int result = futureTask.get();
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        executor.shutdown();
    }

    private static void way3() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sum();
            }
        };
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<Integer> future = executorService.submit(callable);
        Integer result = future.get();
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        executorService.shutdown();
    }

    private static void way4() throws InterruptedException {
        long start = System.currentTimeMillis();
        List<Integer> resultList = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                resultList.add(sum());
            }
        });
        thread.start();
        thread.join();
        int result = resultList.get(0);

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
        // 然后退出main线程
    }

    private static void way5() throws InterruptedException {
        long start = System.currentTimeMillis();

        CountDownLatch count = new CountDownLatch(1);
        List<Integer> resultList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                resultList.add(sum());
                count.countDown();
            }
        }).start();
        count.await();

        int result = resultList.get(0);
        System.out.println("异步计算结果为：" + result);

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }

    public static void way6() {
        long start = System.currentTimeMillis();
        List<Integer> resultList = new ArrayList<>();

        CyclicBarrier cyclicBarrier = new CyclicBarrier(1, new Runnable() {
            @Override
            public void run() {
                int result = resultList.get(0);
                System.out.println("异步计算结果为：" + result);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    resultList.add(sum());
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        System.out.println("使用时间：" + (System.currentTimeMillis() - start) + " ms");
    }
}
