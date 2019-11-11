package service;

import java.util.Map;

public interface EchartsService {

	public Map<String, Object> getExcelPosition();

	public Map<String, Object> getClassifier(Integer divide, Integer isDistinct);
}
