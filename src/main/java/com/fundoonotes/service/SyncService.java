package com.fundoonotes.service;

import java.util.Map;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fundoonotes.repository.ElasticsearchRepository;
import com.fundoonotes.repository.RedisRepository;

@Service
public class SyncService {

	@Autowired
	RedisRepository redisRepository;

	@Autowired
	ElasticsearchRepository elasticSearchRepository;

	private static final Logger LOGGER = Logger.getLogger(SyncService.class);

	public void sendRedisDataToSyncService(Map<String, Object> message) {
		LOGGER.info("****PROCESSING THE REDIS DATA****");
		LOGGER.info("REDIS DATA FROM SYNC SERVICE" + message.toString());
		String key = (String) message.get("KEY");
		String hkey = (String) message.get("HK");
		Object hvalue = message.get("object");
		String operation = (String) message.get("operation");
		if (operation.equalsIgnoreCase("insert")) {
			redisRepository.save(key, hkey, hvalue);
		} else if (operation.equalsIgnoreCase("update")) {
			redisRepository.update(key, hkey, hvalue);
		} else if (operation.equalsIgnoreCase("delete")) {
			redisRepository.delete(key, hkey);
		}
	}

	public void sendElasticDataToSyncService(Map<String, Object> message) {
		LOGGER.info("****PROCESSING THE ELASTIC SEARCH DATA****");
		LOGGER.info("ELASTIC DATA FROM SYNC SERVICE" + message.toString());
		String index = (String) message.get("index");
		String type = (String) message.get("type");
		String id = (String) message.get("id");
		Object document = message.get("document");
		String operation = (String) message.get("operation");
		if (operation.equalsIgnoreCase("index")) {
			elasticSearchRepository.insertDocument(index, type, id, document);
		} else if (operation.equalsIgnoreCase("update")) {
			elasticSearchRepository.updateDocument(index, type, id, document);
		} else if (operation.equalsIgnoreCase("delete")) {
			elasticSearchRepository.deleteDocument(index, type, id);
		}
	}

}
