package crud;

import java.util.Map;
import java.util.HashMap;

public abstract class CachedCRUD<T extends IdObject> implements CRUDInterface<T> {
	private Map<String, T> cache = new HashMap<>();
	
	public abstract void createCached(T object);
	
	public abstract T readCached(String id);
	
	public abstract void updateCached(String id, T object);
	
	public abstract void deleteCached(String id);

	
	@Override
	public void create(T object) {
		this.createCached(object);
		updateCache(object.getId(), object);
	}

	@Override
	public T read(String id) {
		if (!isInCache(id)) {
			T remote = readCached(id);
			updateCache(id, remote);
			return remote;
		}
		return cache.get(id);
	}
	
	@Override
	public void update(String id, T object) {
		this.updateCached(id, object);
		updateCache(id, object);		
	}
	
	@Override
	public void delete(String id) {
		this.deleteCached(id);
		if (isInCache(id)) {
			deleteFromCache(id);
		}
	}
	
	protected void updateCache(String key, T object) {
		this.cache.put(key, object);
	}
	
	protected void deleteFromCache(String key) {
		if(isInCache(key)) {
			this.cache.remove(key);
		}
	}

	protected boolean isInCache(String key) {
		return this.cache.containsKey(key);
	}
}
