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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class dispatches all <var>listener</var> method calls on the main thread and allows to cancel them.
 * @param <R> type of the result
 */
final class MainThreadRequestListener<R> extends RequestListenerWrapper<R> {

	@Nonnull
	private final CancellableExecutor mainThread;

	@Nullable
	private Runnable successRunnable;

	@Nullable
	private Runnable errorRunnable;

	MainThreadRequestListener(@Nonnull CancellableExecutor mainThread, @Nonnull RequestListener<R> listener) {
		super(listener);
		this.mainThread = mainThread;
	}

	@Override
	public void onSuccess(@Nonnull final R result) {
		successRunnable = new Runnable() {
			@Override
			public void run() {
				listener.onSuccess(result);
			}
		};
		mainThread.execute(successRunnable);
	}

	@Override
	public void onError(final int response, @Nonnull final Exception e) {
		errorRunnable = new Runnable() {
			@Override
			public void run() {
				listener.onError(response, e);
			}
		};
		mainThread.execute(errorRunnable);
	}

	public void onCancel() {
		if (successRunnable != null) {
			mainThread.cancel(successRunnable);
			successRunnable = null;
		}

		if (errorRunnable != null) {
			mainThread.cancel(errorRunnable);
			errorRunnable = null;
		}
	}
}
