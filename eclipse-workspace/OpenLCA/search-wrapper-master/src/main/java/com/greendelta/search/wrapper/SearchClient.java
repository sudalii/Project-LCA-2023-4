package com.greendelta.search.wrapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface SearchClient {

	SearchResult<Map<String, Object>> search(SearchQuery searchQuery);

	Set<String> searchIds(SearchQuery searchQuery);

	void create(Map<String, String> settings);

	void index(String id, Map<String, Object> content);

	void index(Map<String, Map<String, Object>> contentsById);

	void update(String id, Map<String, Object> content);

	void update(String id, String script, Map<String, Object> parameters);

	void update(Set<String> ids, Map<String, Object> content);

	void update(Set<String> ids, String script, Map<String, Object> parameters);

	void update(Map<String, Map<String, Object>> contentsById);

	void remove(String id);

	void remove(Set<String> ids);

	boolean has(String id);

	Map<String, Object> get(String id);

	List<Map<String, Object>> get(Set<String> ids);

	void clear();
	
	void delete();

}
