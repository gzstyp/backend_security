package com.fwtai.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 通用的键值对数据结构
 * @作者 田应平
 * @版本 v1.0
 * @提示 表单提交的字段和数据库字段(字段大小写一致)的话，处理表单更为方便
 * @创建时间 2017年1月12日 13:13:05
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class PageFormData extends HashMap<String,Object>{

	private static final long serialVersionUID = 1L;

	private HashMap<String,Object> map = null;
	
	/**无参数的构造方法 */
	public PageFormData(){
		map = new HashMap<String,Object>(20);
	}
	
	/**有参数的构造方法,能获取到表单或ajax提交传参数和值或参数不传值的方法,用法 new PageFormData(request); */
	public PageFormData(final HttpServletRequest request){
		map = new HashMap<String,Object>(20);
		final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            if(key.equals("_"))continue;
            final String value = request.getParameter(key);
            if(value != null && value.length() > 0){
                if(value.length() == 1 && value.equals("_"))continue;
                if(value.equalsIgnoreCase("undefined"))continue;
                if(value.equalsIgnoreCase("null"))continue;
                map.put(key,value.trim());
            }
        }
	}

	/**构建获取POST方式的参数,用法:new PageFormData().build(request);*/
    public PageFormData build(final HttpServletRequest request){
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            if(sb.length() > 0){
                final String str = sb.toString().trim();
                final JSONObject json = JSONObject.parseObject(str);
                if(!json.isEmpty()){
                    for(final String key : json.keySet()){
                        if(key.equals("_"))continue;
                        final Object obj = json.get(key);
                        if(obj != null){
                            if(obj instanceof String){
                                final String value = obj.toString().trim();
                                if(value.length() <= 0)continue;
                                if(value.length() == 1 && value.equals("_"))continue;
                                if(value.equalsIgnoreCase("undefined"))continue;
                                if(value.equalsIgnoreCase("null"))continue;
                                map.put(key,value);
                            }else{
                                map.put(key,obj);
                            }
                        }
                    }
                }
            }
        } catch (Exception e){}
        return this;
    }

    public final JSONObject buildJSONObject(final HttpServletRequest request){
        JSONObject json = new JSONObject(20);
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            if(sb.length() > 0){
                json = JSONObject.parseObject(sb.toString().trim());
                if(!json.isEmpty()){
                    for(final String key : json.keySet()){
                        if(key.equals("_"))continue;
                        final Object obj = json.get(key);
                        if(obj != null){
                            if(obj instanceof String){
                                final String value = obj.toString().trim();
                                if(value.length() <= 0)continue;
                                if(value.length() == 1 && value.equals("_"))continue;
                                if(value.equalsIgnoreCase("undefined"))continue;
                                if(value.equalsIgnoreCase("null"))continue;
                                json.put(key,value);
                            }else {
                                json.put(key,obj);
                            }
                        }
                    }
                    return json;
                }
            }
        } catch (Exception e){
        }
        return json;
    }

    public final JSONArray buildJSONArray(final HttpServletRequest request){
        final JSONArray jsonArray = new JSONArray();
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            if(sb.length() > 0){
                final JSONArray array = JSONObject.parseArray(sb.toString().trim());
                for(int i = 0; i < array.size(); i++){
                    final JSONObject object = array.getJSONObject(i);
                    final JSONObject objectObject = new JSONObject();
                    for(final String key : object.keySet()){
                        if(key.equals("_"))continue;
                        final Object obj = object.get(key);
                        if(obj != null){
                            if(obj instanceof String){
                                final String value = obj.toString().trim();
                                if(value.length() <= 0)continue;
                                if(value.length() == 1 && value.equals("_"))continue;
                                if(value.equalsIgnoreCase("undefined"))continue;
                                if(value.equalsIgnoreCase("null"))continue;
                                objectObject.put(key,value);
                            }else{
                                objectObject.put(key,obj);
                            }
                        }
                    }
                    if(!objectObject.isEmpty())jsonArray.add(objectObject);
                }
            }
        } catch (Exception e){}
        return jsonArray;
    }

    public final static Map<String, String> build(final ServletRequest request){
        final Map<String ,String > dataMap = new HashMap<>();
        // 判断是否已经将 inputStream 流中的 body 数据读出放入 attribute
        if (request.getAttribute("body") != null) {
            // 已经读出则返回attribute中的body
            return (Map<String,String>)request.getAttribute("body");
        } else {
            try {
                Map<String,String > maps = JSON.parseObject(request.getInputStream(),Map.class);
                dataMap.putAll(maps);
                request.setAttribute("body",dataMap);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return dataMap;
        }
    }

    /**
     * 获取请求体的字符串,拿到该字符串后可调用该字符串是否是json对象|json数组|纯字符串再做业务处理
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年5月23日 20:27:44
    */
    public final static String getRequest(final HttpServletRequest request){
        final StringBuilder sb = new StringBuilder();
        try {
            final InputStream is = request.getInputStream();
            final InputStreamReader isr = new InputStreamReader(is,"UTF-8");
            final BufferedReader br = new BufferedReader(isr);
            String s = "";
            while ((s = br.readLine()) != null){
                sb.append(s);
            }
            return sb.length() > 0 ? sb.toString() : null;
        } catch (Exception e) {}
        return null;
    }

    public final String getString(final String key){
        final Object value = get(key);
        if(value == null)return null;
        final String strVal = (String) value;
        if(strVal.length() == 0 || "null".equalsIgnoreCase(strVal))return null;
        return String.valueOf(value).trim();
    }

    public final Integer getInteger(final String key){
        final Object value = get(key);
        if(value == null)return null;
        if(value instanceof Integer)return (Integer) value;
        if(value instanceof String){
            final String strVal = (String) value;
            if(strVal.length() == 0 || "null".equalsIgnoreCase(strVal))return null;
        }
        return Integer.parseInt(String.valueOf(value));
    }

    public final Long getLong(final String key){
        final Object value = get(key);
        if(value == null)return null;
        if(value instanceof Long)return (Long) value;
        if(value instanceof String){
            final String strVal = (String) value;
            if(strVal.length() == 0 || "null".equalsIgnoreCase(strVal))return null;
        }
        return Long.parseLong(String.valueOf(value));
    }

    public final Double getDouble(final String key){
        final Object value = get(key);
        if(value == null)return null;
        if(value instanceof Double)return (Double) value;
        if(value instanceof String){
            final String strVal = (String) value;
            if(strVal.length() == 0 || "null".equalsIgnoreCase(strVal))return null;
        }
        return Double.parseDouble(String.valueOf(value));
    }

    public final BigDecimal getBigDecimal(final String key){
        final Object value = get(key);
        if(value == null)return null;
        if(value instanceof BigDecimal)return (BigDecimal) value;
        if(value instanceof BigInteger)return new BigDecimal((BigInteger) value);
        final String strVal = value.toString();
        if(strVal.length() == 0)return null;
        return new BigDecimal(strVal);
    }

	@Override
	public int size(){
		return map.size();
	}

	@Override
	public boolean isEmpty(){
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(final Object key){
		return map.containsKey(key);
	}

	@Override
	public Object put(final String key,final Object value){
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m){
		map.putAll(m);
	}

	@Override
	public Object remove(final Object key){
		return map.remove(key);
	}

	@Override
	public void clear(){
		map.clear();
	}

	@Override
	public boolean containsValue(final Object value){
		return map.containsValue(value);
	}

	@Override
	public Object clone(){
		return map.clone();
	}

	@Override
	public Set<String> keySet(){
		return map.keySet();
	}

	@Override
	public Collection<Object> values(){
		return map.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet(){
		return map.entrySet();
	}

	@Override
	public Object get(final Object key){
		return map.get(key);
	}
}