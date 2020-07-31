package edu.fhm.cs.ss.schafkopf.view.baseclasses;

import java.util.LinkedList;
import java.util.List;

import edu.fhm.cs.ss.schafkopf.view.interfaces.IView;

/**
 * This implements threading functionality, available for all views, that use threads for displaying their data or user interaction.<br>
 * <br>
 * 
 * If the threads are correctly registered, the start and stop methods are handling starting all threads at a {@link #start()} call and interrupting them at an
 * incoming {@link #stop()}. The registered threads should check the {@link #isStopped()} flag and -if it is set - terminate. stop is clearing this view's
 * thread list. So new threads can be registered again.
 * 
 * @author Sebastian Stumpf
 * 
 */
public abstract class BaseMultiThreadedView implements IView {
	/** The monitor used to synchronize the start and stop method. */
	private final Object stopMonitor;
	/** true, if this view is stopped. */
	private boolean stopped;
	/** The registered threads are stored in this list. */
	private final List<Thread> threadList;

	/**
	 * Constructor initializing all attributes.
	 */
	public BaseMultiThreadedView() {

		this.threadList = new LinkedList<Thread>();
		stopped = true;
		this.stopMonitor = new Object();

	}

	@Override
	public void start() {

		synchronized (stopMonitor) {
			stopped = false;
			for (final Thread thread : threadList) {
				if (!thread.isAlive()) {
					thread.start();
				}
			}
		}
	}

	@Override
	public void stop() {

		synchronized (stopMonitor) {
			stopped = true;
			for (final Thread thread : threadList) {
				thread.interrupt();
			}
			threadList.clear();
		}
	}

	/**
	 * This method is provided for the threads of implementing views, to check if they should terminate.
	 * 
	 * @return true if the view is stopped.
	 */
	protected final boolean isStopped() {

		synchronized (stopMonitor) {
			return stopped;
		}
	}

	/**
	 * Register a thread. registered threads will be started at a start call and interrupted at a stop call.
	 * 
	 * @param threads
	 *            the threads to register.
	 */
	protected final void registerThreads(final Thread... threads) {

		if (threads != null) {
			for (final Thread thread : threads) {
				this.threadList.add(thread);
			}
		}
	}
}
