class AllocSpawn implements Runnable {
	public int watek;
	
	@Override
	public void run() {
		long d = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			if((System.currentTimeMillis() <( d + 2 * 1000))){ 
				MemoryEater.alloc(32);
			}else{
				System.out.println("watek " + watek + " wykonal " + (i+1) + " alokacji");
				break;
			}
		}
	}
}

class AllocSpawnDynamic implements Runnable {
	public int watek;
	
	@Override
	public void run() {
		long d = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			if((System.currentTimeMillis() <( d + 2 * 1000))){ 
				MemoryEater.alloc(i);
			}else{
				System.out.println("watek " + watek + " wykonal " + (i+1) + " alokacji");
				break;
			}
		}
	}
}