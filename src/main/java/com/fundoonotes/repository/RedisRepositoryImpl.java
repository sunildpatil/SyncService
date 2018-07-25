package com.fundoonotes.repository;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

	private static final Logger LOGGER = Logger.getLogger(RedisRepositoryImpl.class);

	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, Object> hashOperations;

	@Autowired
	public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}

	@Override
	public void save(String key, String id, Object object) {
		hashOperations.put(key, id, object);
		LOGGER.info("ADDED/UPDATED THE DATA INTO THE REDIS");
	}

	@Override
	public Map<String, Object> findAll(String key) {
		return hashOperations.entries(key);
	}

	@Override
	public Object find(String key, String id) {
		return hashOperations.get(key, id);
	}

	@Override
	public void delete(String key, String id) {
		hashOperations.delete(key, id);
		LOGGER.info("DELETED THE DATA SUCCESSFULLY FROM THE REDIS");
	}

	@Override
	public void update(String key, String id, Object object) {
		Object retrievedObject = find(key, id);
		if (retrievedObject != null) {
			hashOperations.put(key, id, object);
			LOGGER.info("UPDATED THE DATA INTO THE REDIS");
		}
	}

}
