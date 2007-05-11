

import java.io.*;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;

public class DownloadWorkQueue {

	private final int nThreads;

	private final PoolWorker[] threads;

	private final LinkedList<Runnable> queue;

	OutputStream OUT = System.out;

	public boolean allThreadsStopped() {
		for (int i = 0; i < threads.length; i++)
			if (threads[i].isAlive())
				return false;
		return true;
	}

	public boolean isAllThreadWorkFinished() {
		for (int i = 0; i < threads.length; i++)
			if (threads[i].isAlive())
				return false;
		return true;
	}

	public DownloadWorkQueue(int nThreads, Writer writer) {
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

	public void addToQueue(String url, String location, Writer writer) {
		synchronized (queue) {
			DownloadRunnableClass drc = new DownloadRunnableClass(url,
					location, writer);
			queue.addLast(drc);
			queue.notify();
		}
	}

	boolean end = false;

	public void putAStop() {
		end = true;
		// queue.notifyAll();
	}

	private class PoolWorker extends Thread {

		OutputStream OUT;

		public void run() {
			Runnable r = null;

			while (true) {
				synchronized (queue) {
					if (queue.isEmpty() && end)
						break;
					while (queue.isEmpty() && !end) {
						try {
							queue.wait(500);
						} catch (InterruptedException ignored) {
						}
					}
					if (!queue.isEmpty())
						r = queue.removeFirst();
				}

				// If we don't catch RuntimeException,
				// the pool could leak threads
				try {

					if (r != null)
						r.run();
				} catch (RuntimeException e) {
					// You might want to log something here
					System.err.println(e.getMessage());
				}

			}
		}
	}
}