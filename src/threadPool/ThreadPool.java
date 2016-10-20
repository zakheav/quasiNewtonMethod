package threadPool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ThreadPool {// 需要改进，让线程池可以按批次完成任务
	private int COMMONSIZE = 10;
	private int MAXSIZE = 20;
	private int TASK_CRITICAL_SIZE = 100;
	private List<Thread> pool;
	private Queue<Runnable> tasks;
	private boolean openProcessingBatch = false;// 默认关闭批处理模式
	private Integer[] finishTasksNum = new Integer[1];// 批处理模式下才有用
	private static ThreadPool instance = new ThreadPool();

	private ThreadPool() {
		pool = new ArrayList<Thread>();
		tasks = new LinkedList<Runnable>();
		for (int i = 0; i < COMMONSIZE; ++i) {
			addLabour(new Worker());
		}
	}

	public static ThreadPool getInstance() {
		return instance;
	}

	class Worker extends Thread {// 正式员工线程
		public void run() {
			while (true) {
				Runnable task;
				synchronized (tasks) {
					while (tasks.isEmpty()) {
						try {
							tasks.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					task = tasks.poll();
				}

				task.run();

				if (openProcessingBatch) {// 开启了批处理模式
					synchronized (finishTasksNum) {
						++finishTasksNum[0];
						finishTasksNum.notify();
					}
				}
			}
		}
	}

	class CasualLaborer extends Thread {// 零时工线程
		public void run() {
			while (true) {
				Runnable task = null;
				synchronized (tasks) {
					if (!tasks.isEmpty()) {
						task = tasks.poll();
					}
				}
				if (task != null) {
					task.run();
					if (openProcessingBatch) {// 开启了批处理模式
						synchronized (finishTasksNum) {
							++finishTasksNum[0];
							finishTasksNum.notify();
						}
					}
				} else {
					break;
				}
			}
			removeLabour(this);
		}
	}

	public void addLabour(Thread t) {
		synchronized (this.pool) {
			if (pool.size() < MAXSIZE) {
				pool.add(t);
				pool.get(pool.size() - 1).start();
			}
		}
	}

	public void removeLabour(Thread t) {
		synchronized (this.pool) {
			if (!pool.isEmpty()) {
				pool.remove(t);
			}
		}
	}

	public void addTasks(Runnable task) {
		synchronized (tasks) {
			tasks.offer(task);
			if (tasks.size() >= this.TASK_CRITICAL_SIZE) {
				addLabour(new CasualLaborer());
			}
			tasks.notify();
		}
	}

	public void addTasksInbatches(List<Runnable> taskList) {// 一批任务需要多次执行，完成一批任务才能执行下一次迭代
		openProcessingBatch = true;// 打开批处理模式
		synchronized (finishTasksNum) {
			finishTasksNum[0] = 0;
		}
		
		for (int j = 0; j < taskList.size(); ++j) {// 加入这一批任务
			synchronized (tasks) {
				tasks.offer(taskList.get(j));
				if (tasks.size() >= this.TASK_CRITICAL_SIZE) {
					addLabour(new CasualLaborer());
				}
				tasks.notify();
			}
		}

		synchronized (finishTasksNum) {
			while (finishTasksNum[0] < taskList.size()) {// 任务列表中的任务还没有完全完成
				try {
					finishTasksNum.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			finishTasksNum[0] = 0;
		}
	}
}
