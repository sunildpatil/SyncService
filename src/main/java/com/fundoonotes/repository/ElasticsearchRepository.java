package com.fundoonotes.repository;

import java.util.Map;

public interface ElasticsearchRepository {

	public void insertDocument(String index, String type, String id, Object document);

	public void updateDocument(String index, String type, String id, Object document);

	public void deleteDocument(String index, String type, String id);

	public Map<String, Object> getDocumentById(String index, String type, String id);

}
