import java.util.LinkedList;

public class DownloadWorkQueue {
	private final int nThreads;

	private final PoolWorker[] threads;

	private final LinkedList<Runnable> queue;

	public DownloadWorkQueue(int nThreads) {
		this.nThreads = nThreads;
		queue = new LinkedList<Runnable>();
		threads = new PoolWorker[nThreads];

		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public void execute(Runnable r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	public void addToQueue(String url, String location) {
		synchronized (queue) {
			DownloadRunnableClass drc = new DownloadRunnableClass(url, location);
			queue.addLast(drc);
			queue.notify();
		}
	}

	boolean end = false;

	public void putAnEnd() {
		end = true;
	}

	private class PoolWorker extends Thread {
		public void run() {
			Runnable r;

			while (true) {
				synchronized (queue) {
					if (queue.isEmpty() && end)
						break;
					while (queue.isEmpty()) {
						if(end) break;
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
						}
					}

					r = queue.removeFirst();
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				try {
					r.run();
				} catch (RuntimeException e) {
					// You might want to log something here
				}
				
			}
		}
	}
}