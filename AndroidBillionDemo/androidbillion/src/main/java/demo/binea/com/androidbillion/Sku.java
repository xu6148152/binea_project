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

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * SKU object as described <a href="http://developer.android.com/google/play/billing/billing_reference.html#product-details-table">here</a>
 */
@Immutable
public final class Sku {

	// value must be “inapp” for an in-app product or "subs" for subscriptions.
	@Nonnull
	public final String product;

	// the product ID for the product
	@Nonnull
	public final String id;

	// formatted price of the item, including its currency sign. The price does not include tax.
	// See #detailedPrice for parsed values
	@Nonnull
	public final String price;

	@Nonnull
	public final Price detailedPrice;

	// title of the product
	@Nonnull
	public final String title;

	// description of the product
	@Nonnull
	public final String description;

	Sku(@Nonnull String product, @Nonnull String id, @Nonnull String price, @Nonnull Price detailedPrice, @Nonnull String title, @Nonnull String description) {
		this.product = product;
		this.id = id;
		this.price = price;
		this.detailedPrice = detailedPrice;
		this.title = title;
		this.description = description;
	}

	@Nonnull
	static Sku fromJson(@Nonnull String json, @Nonnull String product) throws JSONException {
		final JSONObject object = new JSONObject(json);
		final String sku = object.getString("productId");
		final String price = object.getString("price");
		final Price detailedPrice = Price.fromJson(object);
		final String title = object.getString("title");
		final String description = object.optString("description");
		return new Sku(product, sku, price, detailedPrice, title, description);
	}

	/**
	 * Contains detailed information about SKU's price as described <a href="http://developer.android.com/google/play/billing/billing_reference.html#getSkuDetails">here</a>
	 */
	public static final class Price {

		@Nonnull
		static final Price EMPTY = new Price(0, "");

		// price in micro-units, where 1,000,000 micro-units equal one unit of the currency.
		// For example, if price is "€7.99", price_amount_micros is "7990000"
		public final long amount;

		// ISO 4217 currency code for price. For example, if price is specified in British pounds
		// sterling, price_currency_code is "GBP"
		@Nonnull
		public final String currency;

		private Price(long amount, @Nonnull String currency) {
			this.amount = amount;
			this.currency = currency;
		}

		@Nonnull
		private static Price fromJson(@Nonnull JSONObject json) throws JSONException {
			final long amount = json.optLong("price_amount_micros");
			final String currency = json.optString("price_currency_code");
			if (amount == 0 || TextUtils.isEmpty(currency)) {
				return EMPTY;
			} else {
				return new Price(amount, currency);
			}
		}

		/**
		 * @return true if both {@link #amount} and {@link #currency} are valid (non empty)
		 */
		public boolean isValid() {
			return amount > 0 && !TextUtils.isEmpty(currency);
		}
	}
}
