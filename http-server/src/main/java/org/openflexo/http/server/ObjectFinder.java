package org.openflexo.http.server;

public interface ObjectFinder {

	<T> T find(Class<T> type, String resourceId, String objectId);
}
