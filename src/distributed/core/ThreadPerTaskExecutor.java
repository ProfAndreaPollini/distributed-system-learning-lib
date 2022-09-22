package distributed.core;

import java.util.concurrent.Executor;

public class ThreadPerTaskExecutor implements Executor {
	public void execute(Runnable r) {
		try {
			var t = new Thread(r);
			t.start();
			t.join();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
