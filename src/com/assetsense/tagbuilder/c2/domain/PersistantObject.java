package com.assetsense.tagbuilder.c2.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PersistantObject implements Serializable {
	private static final long serialVersionUID = 1L;

	private long persistantId = 0;

	public PersistantObject() {

	}

	public long getPersistantId() {
		return persistantId;
	}

	public void setPersistantId(long id) {
		this.persistantId = id;
	}

	public void detach() {

	}

	@Override
	public boolean equals(Object compare) {

		if (compare == null || !(compare instanceof PersistantObject)) {
			return false;
		}

		return persistantId == ((PersistantObject) compare).persistantId;

	}

	public static <T> List<T> detachList(List<T> values) {

		if (values == null) {
			return values;
		}
		List<T> detached = new ArrayList<T>(values);

		detachCollection(detached);

		return detached;
	}

	public static <T> void detachCollection(Collection<T> values) {

		if (values == null) {
			return;
		}
		for (Object value : values) {
			detach(value);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Object detach(Object value) {
		if (value == null) {
			return null;
		}
		if (value instanceof PersistantObject) {
			((PersistantObject) value).detach();
			return value;
		}
		if (value instanceof List) {
			return detachList((List<T>) value);
		} else {
			return value;
		}
	}
}
