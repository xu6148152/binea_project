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

enum RequestType {
	BILLING_SUPPORTED("supported", Billing.DAY),
	GET_PURCHASES("purchases", 20L * Billing.MINUTE),
	GET_SKU_DETAILS("skus", Billing.DAY),
	PURCHASE("purchase", 0L),
	CONSUME_PURCHASE("consume", 0L);

	@Nonnull
	private final String cacheKeyName;

	final long expiresIn;

	RequestType(@Nonnull String cacheKeyName, long expiresIn) {
		this.cacheKeyName = cacheKeyName;
		this.expiresIn = expiresIn;
	}

	@Nonnull
	Cache.Key getCacheKey(@Nonnull String key) {
		final int keyType = getCacheKeyType();
		return new Cache.Key(keyType, key);
	}

	int getCacheKeyType() {
		return ordinal();
	}

	@Nonnull
	static String getCacheKeyName(int keyType) {
		return values()[keyType].cacheKeyName;
	}
}
