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

/**
 * Billing response codes, codes starting with 10000 are this library specific.
 * See <a href="http://developer.android.com/google/play/billing/index.html">In-App Billing</a> docs for more information.
 */
public final class ResponseCodes {

	private ResponseCodes() {
		throw new AssertionError();
	}

	/**
	 * Success
	 */
	public static final int OK = 0;
	/**
	 * User pressed back or canceled a dialog
	 */
	public static final int USER_CANCELED = 1;
	/**
	 * Account error, for example, user is not logged in
	 */
	public static final int ACCOUNT_ERROR = 2;
	/**
	 * This billing API version is not supported for the type requested
	 */
	public static final int BILLING_UNAVAILABLE = 3;
	/**
	 * Requested SKU is not available for purchase
	 */
	public static final int ITEM_UNAVAILABLE = 4;
	/**
	 * Invalid arguments provided to the API
	 */
	public static final int DEVELOPER_ERROR = 5;
	/**
	 * Fatal error during the API action
	 */
	public static final int ERROR = 6;
	/**
	 * Failure to purchase since item is already owned
	 */
	public static final int ITEM_ALREADY_OWNED = 7;
	/**
	 * Failure to consume since item is not owned
	 */
	public static final int ITEM_NOT_OWNED = 8;


	/**
	 * Billing service can't be connected, {@link android.content.Context#bindService} returned <code>false</code>.
	 * @see android.content.Context#bindService
	 */
	public static final int SERVICE_NOT_CONNECTED = 10000;
	/**
	 * Exception occurred during executing the request
	 */
	public static final int EXCEPTION = 10001;
	/**
	 * Purchase has a wrong signature
	 */
	public static final int WRONG_SIGNATURE = 10002;
	/**
	 * Intent passed to {@link ActivityCheckout#onActivityResult(int, int, android.content.Intent)} is null
	 */
	public static final int NULL_INTENT = 10003;

}
