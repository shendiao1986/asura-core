package org.asura.core.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;

public class JsonUtil {

	public static String fromObject(Object obj) {
		return JSONObject.toJSONString(obj);
	}

	public static <T> T json2Object(String json, Class<T> clazz) {
		return JSONObject.parseObject(json, clazz);
	}

	public static <T> T json2Object(String json, T type) {
		return JSONObject.parseObject(json, new TypeReference<T>() {
		});
	}
	
	public static String[] getValues(String content, String path) {
		List<?> objs = (List<?>) JsonPath.read(content, path, new Filter[0]);

		List<String> list = new ArrayList<>();
		for (Iterator<?> its = objs.iterator(); its.hasNext();) {
			Object obj = its.next();
			list.add(obj.toString());
		}

		return list.toArray(new String[0]);
	}

	public static String getValue(String content, String path) {
		Object obj = JsonPath.read(content, path, new Filter[0]);
		return obj.toString();
	}
}
