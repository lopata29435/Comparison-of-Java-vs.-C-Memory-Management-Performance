public class BenchmarkCases {
    private static long time;
    private static final int threadCount = 4;
    private static Thread t[] = new Thread[threadCount];
    private static Thread dynamic[] = new Thread[threadCount];
 
    public static void oneThreadConstantSize() {
        time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            MemoryEater.alloc(128);
        }
        System.out.println("100 alokacji sekwencyjnie: "
                + (System.currentTimeMillis() - time) + "ms");
    }
 
    public static void FourThreadsConstantSize() {
        for (int j = 0; j < threadCount; j++) {
            AllocSpawn alloc = new AllocSpawn();
            alloc.watek = j + 1;
            t[j] = new Thread(alloc);
        }
         
        for(int j = 0; j < threadCount; j++){
            t[j].start();
        }
    }
 
    public static void oneThreadChangingSize() {
        time = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            MemoryEater.alloc(i);
        }
        System.out.println("100 alokacji o zmiennym rozmiarze "
                + (System.currentTimeMillis() - time) + "ms");
    }
 
    public static void FourThreadChangingSize() {
        for (int j = 0; j < threadCount; j++) {
            AllocSpawnDynamic allocDynamic = new AllocSpawnDynamic();
            allocDynamic.watek = j + 1;
            dynamic[j] = new Thread(allocDynamic);
        }
        for(int j = 0; j < threadCount; j++){
            dynamic[j].start();
        }
    }
 
}