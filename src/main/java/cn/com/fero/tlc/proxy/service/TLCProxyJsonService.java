package cn.com.fero.tlc.proxy.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by wanghongmeng on 2015/6/24.
 */
@Service
public class TLCProxyJsonService {

    public String object2Json(Object obj) {
        if (null == obj) {
            return StringUtils.EMPTY;
        }

        return JSONObject.fromObject(obj).toString();
    }

    public String array2Json(List list) {
        if (CollectionUtils.isEmpty(list)) {
            return StringUtils.EMPTY;
        }

        return JSONArray.fromObject(list).toString();
    }

    public Object json2Object(String jsonStr, Class clazz, String... excludePropery) {
        if (StringUtils.isEmpty(jsonStr)) {
            return null;
        }

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(excludePropery);
        jsonConfig.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {
            @Override
            public String transformToJavaIdentifier(String key) {
                char[] chars = key.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
        });
        JSONObject jsonObject = JSONObject.fromObject(jsonStr, jsonConfig);

        JsonConfig javaConfig = new JsonConfig();
        javaConfig.setRootClass(clazz);
        javaConfig.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {
            @Override
            public String transformToJavaIdentifier(String key) {
                char[] chars = key.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                return new String(chars);
            }
        });
        return JSONObject.toBean(jsonObject, javaConfig);
    }

    public String getString(String jsonStr, String key) {
        if (StringUtils.isEmpty(jsonStr) || StringUtils.isEmpty(key)) {
            return StringUtils.EMPTY;
        }

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        return jsonObject.get(key).toString();
    }

    public List json2Array(String jsonStr, String key, Class clazz, String... excludeProperty) {
        if (StringUtils.isEmpty(jsonStr) || StringUtils.isEmpty(key)) {
            return Collections.EMPTY_LIST;
        }

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        JSONArray array = jsonObject.getJSONArray(key);
        Iterator iterator = array.iterator();
        List objectList = new ArrayList();

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            objectList.add(json2Object(obj.toString(), clazz, excludeProperty));
        }

        return objectList;
    }

    public List json2Array(String jsonStr, Class clazz) {
        if (StringUtils.isEmpty(jsonStr)) {
            return Collections.EMPTY_LIST;
        }

        JSONObject jsonObject = JSONObject.fromObject(jsonStr);
        JSONArray array = jsonObject.getJSONArray(jsonStr);
        Iterator iterator = array.iterator();
        List objectList = new ArrayList();

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            objectList.add(json2Object(obj.toString(), clazz));
        }

        return objectList;
    }
}
