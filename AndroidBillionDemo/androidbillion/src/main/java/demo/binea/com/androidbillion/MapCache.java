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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class MapCache implements Cache {

	@Nonnull
	private final Map<Key, Entry> map = new HashMap<Key, Entry>();

	MapCache() {
	}

	@Nullable
	@Override
	public Entry get(@Nonnull Key key) {
		return map.get(key);
	}

	@Override
	public void put(@Nonnull Key key, @Nonnull Entry entry) {
		map.put(key, entry);
	}

	@Override
	public void init() {
	}

	@Override
	public void remove(@Nonnull Key key) {
		map.remove(key);
	}

	@Override
	public void removeAll(int type) {
		final Set<Map.Entry<Key, Entry>> entries = map.entrySet();
		final Iterator<Map.Entry<Key, Entry>> iterator = entries.iterator();
		while (iterator.hasNext()) {
			final Map.Entry<Key, Entry> entry = iterator.next();
			if(entry.getKey().type == type) {
				iterator.remove();
			}
		}
	}

	@Override
	public void clear() {
		map.clear();
	}
}
