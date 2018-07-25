package com.fundoonotes.repository;

import java.util.Map;

public interface RedisRepository {

	public void save(String key, String id, Object object);

	public void update(String key,String id,Object object);
	
	public Map<String, Object> findAll(String key);

	public Object find(String key, String id);

	public void delete(String key, String id);
}
