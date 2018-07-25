package com.fundoonotes.repository;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class ElasticsearchRepositoryImpl implements ElasticsearchRepository {

	private static final Logger LOGGER = Logger.getLogger(ElasticsearchRepositoryImpl.class);

	private RestHighLevelClient restHighlevelClient;
	private ObjectMapper objectMapper;

	public ElasticsearchRepositoryImpl(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
		this.objectMapper = objectMapper;
		this.restHighlevelClient = restHighLevelClient;
	}

	@Override
	public void insertDocument(String index, String type, String id, Object document) {
		Map<?, ?> dataMap = objectMapper.convertValue(document, Map.class);
		IndexRequest indexRequest = new IndexRequest(index, type, id).source(dataMap);
		try {
			restHighlevelClient.index(indexRequest);
			LOGGER.info("DOCUMENT ADDEDED SUCCESSFULLY TO THE INDEX");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateDocument(String index, String type, String id, Object document) {
		Map<String, Object> record = getDocumentById(index, type, id);
		if (record != null) {
			UpdateRequest updateRequest = new UpdateRequest(index, type, id).fetchSource(true);
			try {
				String noteJson = objectMapper.writeValueAsString(document);
				updateRequest.doc(noteJson, XContentType.JSON);
				restHighlevelClient.update(updateRequest);
				LOGGER.info("DOCUMENT UPDATED SUCCESSFULLY TO THE INDEX");
				return;
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOGGER.info("ERROR IN UPDATING THE UNAVAILABLE DOCUMENT");
	}

	@Override
	public void deleteDocument(String index, String type, String id) {
		Map<String, Object> record = getDocumentById(index, type, id);
		if (record != null) {
			DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
			try {
				restHighlevelClient.delete(deleteRequest);
			} catch (IOException e) {
				e.printStackTrace();
			}
			LOGGER.info("DOCUMENT DELETED SUCCESSFULLY FROM THE INDEX");
			return;
		}
		LOGGER.info("ERROR IN DELETING THE UNAVAILABLE DOCUMENT");
	}

	@Override
	public Map<String, Object> getDocumentById(String index, String type, String id) {
		Map<String, Object> document = null;
		try {
			GetRequest getRequest = new GetRequest(index, type, id);
			GetResponse getResponse = restHighlevelClient.get(getRequest);
			document = getResponse.getSourceAsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return document;
	}

}
