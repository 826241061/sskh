package controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.EchartsService;

@Controller
public class EchartsController {
	
	@Autowired
	EchartsService service;


	
	@ResponseBody
	@RequestMapping("getPosition")
	public Map<String,Object> getPosition() {
		return service.getExcelPosition();
    }

	
	@ResponseBody
	@RequestMapping("getClass")
	public Map<String,Object> getClassifier(Integer divide, Integer isDistinct) {
		return service.getClassifier(divide, isDistinct);
    }
	
	
}
