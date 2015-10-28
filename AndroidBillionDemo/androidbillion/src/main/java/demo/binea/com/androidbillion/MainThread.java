/*
 * Copyright 2014 serso aka se.solovyev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Contact details
 *
 * Email: se.solovyev@gmail.com
 * Site:  http://se.solovyev.org
 */

package demo.binea.com.androidbillion;

import android.os.Handler;
import android.os.Looper;

import javax.annotation.Nonnull;

/**
 * Utility class which executes runnables on the main application thread
 */
final class MainThread implements CancellableExecutor {

	@Nonnull
	private final Handler mainHandler;

	MainThread(@Nonnull Handler mainHandler) {
		Check.isTrue(mainHandler.getLooper() == Looper.getMainLooper(), "Should be main application thread handler");
		this.mainHandler = mainHandler;
	}

	static boolean isMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}

	/**
	 * Method executes <var>runnable</var> on the main application thread. If method is called on the main application thread
	 * then <var>runnable</var> is executed synchronously. Otherwise, it is posted to be executed on the next loop of
	 * the main thread looper.
	 *
	 * @param runnable runnable to be executed on the main application thread
	 */
	@Override
	public void execute(@Nonnull Runnable runnable) {
		if (MainThread.isMainThread()) {
			runnable.run();
		} else {
			mainHandler.post(runnable);
		}
	}

	@Override
	public void cancel(@Nonnull Runnable runnable) {
		this.mainHandler.removeCallbacks(runnable);
	}
}
