package ThreadTest;

public class createThread {
    // 题目：用两种不同的方式创建并启动线程
    // 方式2：实现Runnable接口
    // 要求：两个线程交替打印数字1-10
    public static int current_number = 1;
    public static final int max_number = 10;
    public static final Object lock = new Object(); // 在类中添加共享锁对象

    public static void main(String[] args) {
        //testThread();// 方式1,Thread方式创建线程并执行，两个线程都打印1-10
        //testRunnable(); //方式2,Runnable方式创建线程并执行，两个线程都打印1-10
        //testThreadNullSynchronized(); // 方式3,线程不加锁，Thread方式测试依次对值累加，1-10
        //testThreadSynchronized();// 方式4,线程加锁，Thread方式测试依次对值累加，1-10
        //testRunnableNullSynchronized();// 方式5,线程不加锁，Runnable方式测试依次对值累加，1-10
        //testRunnableSynchronized(); // 方式6,线程加锁，Runnable方式测试依次对值累加，1-10
        //testRunnableSynchronizedTwoThread(); // 方式7,两个线程交替打印1-10 Runnable方式
        //testThreadSynchronizedTwoThread(); // 方式8,线程加锁，Thread方式测试依次对值累加，1-10
        testSynchronizedTwoThread();// 方式9,线程加锁，两个线程交替打印数字1-10 Runnable方式和Thread方式
    }

    // 方式1：继承Thread类
    public static class MyThread extends Thread {
        private final String threadName;

        public MyThread(String name) {
            threadName = name;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(threadName + "打印数字:" + (i + 1));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    // 方式2：实现Runnable接口
    public static class MyRunnable implements Runnable {
        public MyRunnable() {
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "打印数字:" + (i + 1));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static void testThread() {
        MyThread thread1 = new MyThread("线程1");
        MyThread thread2 = new MyThread("线程2");
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void testRunnable() {
        MyRunnable runnable1 = new MyRunnable();
        Thread thread1 = new Thread(runnable1,"线程1");
        Thread thread2 = new Thread(runnable1,"线程2");
        thread1.start();
        thread2.start();
    }

    //线程不加锁，测试依次对值累加，1-10
    public static class MyThread2 extends Thread {
        private final String threadName;

        public MyThread2(String name) {
            threadName = name;
        }

        @Override
        public void run() {
            while (current_number <= max_number) {
                    System.out.println(threadName + "打印数字:" + current_number);
                    current_number++;
            }
        }
    }

    //线程加锁，测试依次对值累加，1-10
    public static class MyThread3 extends Thread {
        private final String threadName;

        public MyThread3(String name) {
            threadName = name;
        }

        @Override
        public void run() {
            synchronized (lock) {
                while (current_number <= max_number) {
                    System.out.println(threadName + "打印数字:" + current_number);
                    current_number++;
                }
            }
        }
    }
    public static void testThreadNullSynchronized() {
        System.out.println("主线程开始运行");
        MyThread2 thread1 = new MyThread2("线程1");
        MyThread2 thread2 = new MyThread2("线程2");
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            System.out.println("线程1运行结束");
            thread2.join();
            System.out.println("线程2运行结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("主线程运行结束");
    }
    public static void testThreadSynchronized() {
        MyThread3 thread1 = new MyThread3("线程1");
        MyThread3 thread2 = new MyThread3("线程2");
        thread1.start();
        thread2.start();
    }

    public static class MyRunnable2 implements Runnable{

        public MyRunnable2() {
        }
        public void run() {
            //线程不加锁，测试依次对值累加，1-10
            while (current_number <= max_number) {
                System.out.println(Thread.currentThread().getName() + "打印数字:" + current_number);
                current_number++;
            }
        }
    }
    public static void testRunnableNullSynchronized() {
        MyRunnable2 myRunnable1 = new MyRunnable2();
        Thread thread1 = new Thread(myRunnable1,"线程1");
        Thread thread2 = new Thread(myRunnable1,"线程2");
        thread1.start();
        thread2.start();
    }

    public static class MyRunnable3 implements Runnable{

        public MyRunnable3() {
        }
        public void run() {
            synchronized (lock) {
                //线程不加锁，测试依次对值累加，1-10
                while (current_number <= max_number) {
                    System.out.println(Thread.currentThread().getName() + "打印数字:" + current_number);
                    current_number++;
                }
            }
        }
    }
    public static void testRunnableSynchronized() {
        MyRunnable3 myRunnable1 = new MyRunnable3();
        Thread thread1 = new Thread(myRunnable1,"线程1");
        Thread thread2 = new Thread(myRunnable1,"线程2");
        thread1.start();
        thread2.start();
    }

    //打印奇数偶数
    public static class MyRunnable4 implements Runnable{
        private final Boolean isEven;//是否是偶数
        public MyRunnable4(Boolean isEven) {
            this.isEven = isEven;
        }
        //是否偶数，判断，偶数则打印，奇数则等待
        public void run() {
            synchronized (lock) {
                while (current_number <= max_number) {
                    Boolean flag=current_number%2==0;
                    if(flag==isEven){
                        //如果是需要打印的数字，则打印，并且唤醒其他线程
                        System.out.println(Thread.currentThread().getName() + "打印数字:" + current_number);
                        current_number++;
                        lock.notifyAll();
                    }
                    else{
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void testRunnableSynchronizedTwoThread() {
        MyRunnable4 myRunnable1 = new MyRunnable4(true);
        MyRunnable4 myRunnable2 = new MyRunnable4(false);
        Thread thread1 = new Thread(myRunnable1,"线程1");
        Thread thread2 = new Thread(myRunnable2,"线程2");
        thread1.start();
        thread2.start();
    }
    public static class MyThead4 extends Thread {
        private final Boolean isEven;
        private String threadName;

        public MyThead4(String threadName ,Boolean isEven) {
            this.threadName = threadName;
            this.isEven = isEven;
        }
        public void run() {
            synchronized (lock) {
                while (current_number <= max_number) {
                    Boolean flag=current_number%2==0;
                    if(flag==isEven){
                        System.out.println(threadName + "打印数字:" + current_number);
                        current_number++;
                        lock.notifyAll();
                    }
                    else{
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
    public static void testThreadSynchronizedTwoThread() {
        MyThead4 thread1 = new MyThead4("线程1",true);
        MyThead4 thread2 = new MyThead4("线程2",false);
        thread1.start();
        thread2.start();
    }
    public static void testSynchronizedTwoThread() {
        MyThead4 thread1 = new MyThead4("Thead线程1",true);
        MyRunnable4 myRunnable4 = new MyRunnable4(false);
        Thread thread2 = new Thread(myRunnable4,"Runnable线程2");
        thread1.start();
        thread2.start();
    }
}
