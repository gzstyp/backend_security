package com.fwtai.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.RenewalToken;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端请求|服务器端响应工具类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月11日 19:20:50
 * @QQ号码 444141300
 * @主页 http://www.fwtai.com
*/
public final class ToolClient implements Serializable{

	private static final long serialVersionUID = 1L;

    private static Logger logger = LoggerFactory.getLogger(ToolClient.class);

	/**
	 * 生成简单类型json字符串,仅用于查询返回,客户端只需判断code是否为200才操作,仅用于查询操作,除了list集合之外都可以用data.map获取数据,list的是data.listData,字符串或数字对应是obj
	 * @作者 田应平
	 * @注意 如果传递的是List则在客户端解析listData的key值,即data.listData;是map或HashMap或PageFormData解析map的key值,即data.map;否则解析obj的key值即data.obj或data.map
	 * @用法 解析后判断data.code == AppKey.code.code200 即可
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装,不可用于redis缓存value
	 * @创建时间 2017年1月11日 上午10:27:53
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String queryJson(final Object object){
        if(object == null || object.toString().trim().length() <= 0){
            return queryEmpty();
        }
        final JSONObject json = new JSONObject();
        if (object instanceof Exception) {
            json.put(ConfigFile.code,ConfigFile.code204);
            json.put(ConfigFile.msg,ConfigFile.msg204);
            json.put(ConfigFile.data,object);
            return json.toJSONString();
        }
        if(object instanceof Map<?,?>){
            final Map<?,?> map = (Map<?,?>) object;
            if(map == null || map.size() <= 0){
                queryEmpty();
            }else {
                json.put(ConfigFile.code,ConfigFile.code200);
                json.put(ConfigFile.msg,ConfigFile.msg200);
                json.put(ConfigFile.data,object);
                return json.toJSONString();
            }
        }
        if(object instanceof List<?>){
            final List<?> list = (List<?>) object;
            if(list == null || list.size() <= 0){
                return queryEmpty();
            }else {
                if (list.get(0) == null){
                    return queryEmpty();
                }else {
                    json.put(ConfigFile.code,ConfigFile.code200);
                    json.put(ConfigFile.msg,ConfigFile.msg200);
                    json.put(ConfigFile.data,object);
                    final String jsonObj = json.toJSONString();
                    final JSONObject j = JSONObject.parseObject(jsonObj);
                    final String listData = j.getString(ConfigFile.data);
                    if (listData.equals("[{}]")){
                        return queryEmpty();
                    }
                    return jsonObj;
                }
            }
        }
        if(String.valueOf(object).toLowerCase().equals("null") || String.valueOf(object).replaceAll("\\s*", "").length() == 0){
            return queryEmpty();
        }else {
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
            json.put(ConfigFile.data,object);
            final String jsonObj = json.toJSONString();
            final JSONObject j = JSONObject.parseObject(jsonObj);
            final String obj = j.getString(ConfigFile.data);
            if (obj.equals("{}")){
                return queryEmpty();
            }
            return jsonObj;
        }
	}

	/**
	 * 查询时得到的数据为空返回的json字符串
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月11日 下午9:40:21
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static final String queryEmpty(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code201);
		json.put(ConfigFile.msg,ConfigFile.msg201);
		return json.toJSONString();
	}

	/**
	 * 生成json字符串对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作时,判断当前的rows是否大于0来确定是否操作成功,一般在service调用,偷懒的人可以使用本方法
	 * @param rows 执行后受影响的行数
	 * @用法 解析后判断data.code == AppKey.code.code200即可操作
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:44:23
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static final String executeRows(final int rows){
		final JSONObject json = new JSONObject();
		if(rows > 0){
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,ConfigFile.msg200);
			json.put(ConfigFile.data,rows);
			return json.toJSONString();
		}else{
			json.put(ConfigFile.code,ConfigFile.code199);
			json.put(ConfigFile.msg,ConfigFile.msg199);
			json.put(ConfigFile.data,rows);
			return json.toJSONString();
		}
	}

    /**
     * 操作成功生成json字符串对象,失败信息是ConfigFile.msg199,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作,一般在service调用
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/1/19 11:31
    */
    public static final String executeRows(final int rows,final String success){
        final JSONObject json = new JSONObject();
        if(rows > 0){
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,success);
            json.put(ConfigFile.data,rows);
            return json.toJSONString();
        }else{
            json.put(ConfigFile.code,ConfigFile.code199);
            json.put(ConfigFile.msg,ConfigFile.msg199);
            json.put(ConfigFile.data,rows);
            return json.toJSONString();
        }
    }

	/**
	 * 生成自定义的json对象,直接采用JSONObject封装,执行效率会更高;适用于为增、删、改操作,一般在service调用
	 * @param rows 执行后受影响的行数
	 * @param success 执行成功的提示消息
	 * @param failure 执行失败的提示消息
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:50:22
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static final String executeRows(final int rows,final String success,final String failure){
		final JSONObject json = new JSONObject();
		if(rows > 0){
			json.put(ConfigFile.code,ConfigFile.code200);
			json.put(ConfigFile.msg,success);
			json.put(ConfigFile.data,rows);
			return json.toJSONString();
		}else{
			json.put(ConfigFile.code,ConfigFile.code199);
			json.put(ConfigFile.msg,failure);
			json.put(ConfigFile.data,rows);
			return json.toJSONString();
		}
	}

	/**
	 * 生成json格式字符串,code和msg的key是固定的,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
	 * @创建时间 2016年12月25日 18:11:16
	 * @QQ号码 444141300
	 * @param code 相关参数协议
	 * @主页 http://www.fwtai.com
	*/
	public static final String createJson(final int code,final String msg){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,code);
		json.put(ConfigFile.msg,msg);
		return json.toJSONString();
	}

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
     * @创建时间 2018年7月3日 09:20:05
     * @QQ号码 444141300
     * @param code 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static final String createJson(final String code,final String msg){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,code);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
     * @创建时间 2018年7月3日 09:20:17
     * @QQ号码 444141300
     * @param hashMap 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static final String createJson(final HashMap<String,Object> hashMap){
        final JSONObject json = new JSONObject();
        for(final String key : hashMap.keySet()){
            json.put(key,hashMap.get(key));
        }
        return json.toJSONString();
    }

    /**
     * 生成json格式字符串,直接采用JSONObject封装,执行效率会更高,用于增、删、改、查操作,一般在service层调用
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装
     * @用法 解析后判断data.code == AppKey.code.code200即可处理操作
     * @创建时间 2018年7月3日 09:20:31
     * @QQ号码 444141300
     * @param map 相关参数协议
     * @主页 http://www.fwtai.com
    */
    public static final String createJson(final Map<String,Object> map){
        final JSONObject json = new JSONObject();
        for(final String key : map.keySet()){
            json.put(key,map.get(key));
        }
        return json.toJSONString();
    }

    /**
     * 生成code为199的json格式数据且msg是提示信息
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/29 15:00
    */
    public final static String createJsonFail(final String msg){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code199);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }

    /**
     * 生成code为200的json格式数据且msg是提示信息
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/7/29 15:00
    */
    public final static String createJsonSuccess(final String msg){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code200);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }

	/**
	 * 验证密钥key的返回json格式专用,先调用方法validateKey(pageFormData)返回值false后再直接调用本方法返回json字符串
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @创建时间 2017年1月11日 下午7:38:48
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static final String jsonValidateKey(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code203);
		json.put(ConfigFile.msg,ConfigFile.msg203);
		return json.toJSONString();
	}

	/**
	 * 验证必要的参数字段是否为空的返回json格式专用,先调用方法validateField()返回值false后再直接调用本方法返回json字符串
	 * @作者 田应平
	 * @返回值类型 返回的是json字符串,内部采用JSONObject封装
	 * @创建时间 2017年1月11日 下午7:38:48
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static final String jsonValidateField(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code202);
		json.put(ConfigFile.msg,ConfigFile.msg202);
		return json.toJSONString();
	}

	/**
	 * 验证传入的值是否有值
	 * @param 
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2020/5/22 11:30
	*/
    public final static String validateField(final String... fields){
        if(fields == null || fields.length <= 0){
            return jsonValidateField();
        }
        boolean flag = false;
        for (final String p : fields){
            if(p == null || p.length() <= 0){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

    /**
     * 验证必要的字段是否为空,不验证ckey密钥,一般在service层调用,如果返回为 null 则验证成功,否则失败;适用于增、删、改、查操作!
     * @fields 需要验证的form字段
     * @用法1 final String validate = ToolClient.validateField(params,"kid");if(validate != null)return validate;
     * @用法2 final String validate = ToolClient.validateField(params,new String[]{"id"});if(validate != null)return validate;
     * @作者 田应平
     * @返回值类型 返回的是json字符串,内部采用JSONObject封装,如果返回为 null 则验证成功!
     * @创建时间 2017年2月23日 下午10:10:34
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public final static String validateField(final Map<String,?> params,final String... fields){
        if(ToolString.isBlank(params) || ToolString.isBlank(fields)){
            return jsonValidateField();
        }
        boolean flag = false;
        for (final String p : fields){
            if(ToolString.isBlank(params.get(p))){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

    /**
     * 验证必填字段,线程安全
     * @用法1 final String validate = ToolClient.validateForm(params,"kid");if(validate != null)return validate;
     * @用法2 final String validate = ToolClient.validateForm(params,new String[]{"id"});if(validate != null)return validate;
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/2 18:31
   */
    public final static String validateForm(final ConcurrentHashMap<String,String> formData,final String[] fields){
        if(ToolString.isBlank(formData) || ToolString.isBlank(fields)){
            return jsonValidateField();
        }
        boolean flag = false;
        for(String p : fields){
            if(ToolString.isBlank(formData.get(p))){
                flag = true;
                break;
            }
        }
        if(flag)return jsonValidateField();
        return null;
    }

    private static final String jsonValidateInteger(){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code199);
        json.put(ConfigFile.msg,"参数类型有误");
        return json.toJSONString();
    }

    /**
     * 验证所输入的数据是否是Integer类型,先验证是否必填后才调用本方法,一般在service调用
     * @用法1 final String fieldInteger = ToolClient.validateInteger(pageFormData,"type");if(fieldInteger != null)return fieldInteger;
     * @用法2 final String fieldInteger = ToolClient.validateInteger(pageFormData,new String[]{"category","subset","type"});if(fieldInteger != null)return fieldInteger;
     * @param
     * @作者 田应平
     * @QQ 444141300`
     * @创建时间 2020/4/2 13:04
    */
    public final static String validateInteger(final Map<String,?> params,final String... fields){
        for(int i = 0; i < fields.length;i++){
            try {
                final Object o = params.get(fields[i]);
                if(o != null){
                    final String value = String.valueOf(String.valueOf(o));
                    if(value.equalsIgnoreCase("null") || value.equalsIgnoreCase("undefined"))return jsonValidateInteger();
                    if(value == null){
                        return jsonValidateInteger();
                    }
                    Integer.parseInt(value);
                }else{
                    return jsonValidateInteger();
                }
            } catch (final Exception e) {
                return jsonValidateInteger();
            }
        }
        return null;
    }

	/**
	 * 生成|计算总页数
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月2日 下午1:20:53
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static Integer totalPage(final Integer total,final Integer pageSize){
		return (total%pageSize) == 0 ? (total/pageSize):(total/pageSize)+1; //总页数
	}

	/**
	 * 生成json对象
	 * @param map
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年7月30日 22:47:24
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonObj(final Map<String, Object> map){
		return JSON.toJSONString(map);
	}

	/**
	 * 生成json数组
	 * @param listData
	 * @return
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月12日 下午9:28:55
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonArray(final List<Map<String, Object>> listData){
		return JSONArray.toJSONString(listData);
	}


	/**
	 * 用于生成出现异常信息时的json固定code:204字符串提示,返回给controller层调用,一般在service层调用
	 * @作者 田应平
	 * @返回值类型 String,内部采用JSONObject封装,msg 为系统统一的‘系统出现错误’
	 * @创建时间 2017年1月10日 21:40:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String exceptionJson(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code204);
		json.put(ConfigFile.msg,ConfigFile.msg204);
		return json.toJSONString();
	}

	/**
	 * 用于生成出现异常信息时的json固定code:204字符串提示,返回给controller层调用,一般在service层调用
	 * @param msg 自定义提示的异常信息
	 * @作者 田应平
	 * @返回值类型 String,内部采用JSONObject封装
	 * @创建时间 2017年1月10日 21:40:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static String exceptionJson(final String msg){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code204);
		json.put(ConfigFile.msg,msg);
		return json.toJSONString();
	}

	/**
	 * 返回给客户端系统出现错误的提示信息,已返回给客户端,只能在controller层调用,用于增、删、改、查操作的异常返回给客户端
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:07:16
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static void responseException(final HttpServletResponse response){
		responseJson(exceptionJson(),response);
		return;
	}

	/**
	 * 返回给客户端系统出现错误的提示信息,已返回给客户端,只能在controller层调用,用于增、删、改、查操作的异常返回给客户端
	 * @param msg 自定义提示的异常信息
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年12月25日 下午5:07:16
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static void responseException(final HttpServletResponse response,final String msg){
		responseJson(exceptionJson(msg),response);
		return;
	}

	/**
	 * 未登录提示信息,json格式
	 * @作者 田应平
	 * @创建时间 2017年1月14日 上午12:46:08
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String jsonNotLogin(){
		final JSONObject json = new JSONObject();
		json.put(ConfigFile.code,ConfigFile.code205);
		json.put(ConfigFile.msg,ConfigFile.msg205);
		return json.toJSONString();
	}

    /**
     * 通用的响应json返回json对象,只能在是controller层调用
     * @param json json对象
     * @注意 不能在service层调用
     * @作者 田应平
     * @创建时间 2016年8月18日 17:53:18
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public final static void responseJson(final String json){
        try {
            final HttpServletResponse response = getResponse();
            responseJson(json,response);
        } catch (Exception e) {}
    }

	/**
	 * 通用的响应json返回json对象,只能在是controller层调用
	 * @param json json对象
	 * @param response
	 * @注意 不能在service层调用
	 * @作者 田应平
	 * @创建时间 2016年8月18日 17:53:18
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static void responseJson(final String json,final HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter writer = null;
        try {
			writer = response.getWriter();
            if(json == null){
                writer.write(createJson(ConfigFile.code201,ConfigFile.msg201));
                writer.flush();
            }else{
                final String token = RenewalToken.get();
                if(token != null){
                    writer.write(JSON.parseObject(json).fluentPut("renewal","token").toJSONString());
                    writer.flush();
                }else{
                    writer.write(String.valueOf(JSON.parse(json)));
                    writer.flush();
                }
            }
		}catch (IOException e){
			logger.error("类ToolClient的方法responseJson出现异常",e.getMessage());
		}finally{
			if(writer != null){
				writer.close();
                writer = null;
			}
		}
	}

    public final static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public final static HttpServletResponse getResponse(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

	/**
	 * 响应返回客户端字符串,该obj对象字符串不是标准的json字符串!
	 * @param
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月7日 17:31:10
	*/
	public final static void responseObj(final Object obj,final HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setHeader("Cache-Control","no-cache");
		PrintWriter writer = null;
		try {
			writer = response.getWriter();
			writer.write(String.valueOf(obj));
			writer.flush();
			writer.close();
		}catch (IOException e){
			e.printStackTrace();
			logger.error("类ToolClient的方法responseWrite出现异常",e);
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}

	/**
	 * 获取登录人的Session信息,根据key获取相应的值,有账号主键id(ConfigFile.LOGIN_KEY)、登录账号ConfigFile.LOGIN_USER
	 * @param request
	 * @return 返回登录人的Session信息,如userid
	 * @作者 田应平
	 * @创建时间 2015-8-26 下午2:30:01
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String loginKey(final HttpServletRequest request,String key){
		return (String) request.getSession(false).getAttribute(key);
	}

	/**
	 * 文件下载
	 * @param filePath 文件物理路径
	 * @param response
	 * @作者 田应平
	 * @创建时间 2015-10-17 下午6:01:36
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean download(final HttpServletResponse response,final String filePath){
		try {
			// filePath是指欲下载的文件的全路径。
			final File file = new File(filePath);
			if(!file.exists()){
				logger.info("类ToolClient.java下的方法download():文件不存在");
				return false;
			}
			// 取得文件名。
			final String filename = file.getName();
			// 取得文件的后缀名。
			final String ext = filename.substring(filename.lastIndexOf(".") + 1);
			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="+ new String((filename + ext).getBytes("utf-8"), "ISO-8859-1"));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			return true;
		} catch (IOException ex){
			ex.printStackTrace();
			logger.error("类ToolClient.java下的方法download():出现异常",ex);
			return false;
		}
	}

	/**
	 * 获取项目物理根路径
	 * @返回结果 {"code":"200","msg":"E:\workspace\manager"}
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月5日 12:32:51
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getWebRoot(){
		return RequestContext.class.getResource("/../../").getPath();
	}

	/**
	 * 获取项目所在的物理路径,推荐使用
	 * @param request
	 * @作者 田应平
	 * @创建时间 2017年9月25日 下午3:47:29
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getWebRoot(final HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath(File.separator);
	}

	/**
	 * 获取访问项目的网站域名,如http://api.yinlz.com
	 * @param request
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月16日 15:18:55
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String getDomainName(final HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName();
	}

	/**
	 * 统计处理
	 * @作者 田应平
	 * @参数 List 是数据的数据条数
	 * @参数 keyTotal是count字段或该字段别名
	 * @参数 decimalFormat是统计时的数据格式化,如0、0.0、0.00
	 * @创建时间 2016年9月12日 下午7:34:01
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static List<Map<String, Object>> statistics(final List<Map<String, Object>> list,final String keyTotal,final String decimalFormat){
		Integer total = 0;
		for(int i = 0; i < list.size(); i++){
			final Map<String, Object> map = list.get(i);
			for(String key : map.keySet()){
				if(key.equals(keyTotal)){
					total += Integer.parseInt(map.get(key).toString());//计算总数
				}
			}
		}
		final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for(int i = 0; i < list.size(); i++){
			final Map<String, Object> map = list.get(i);
			final Map<String, Object> m = new HashMap<String, Object>();
			for(String key : map.keySet()){
				if(key.equals(keyTotal)){
					float f = (float)(Integer.parseInt(map.get(key).toString()))/total * 100;//求平均数
					final DecimalFormat df = new DecimalFormat(decimalFormat);//格式化小数,如0.0或0或0.00
					m.put(key,Double.parseDouble(df.format(f)));
				}else {
					m.put(key,map.get(key));
				}
			}
			result.add(m);
		}
		return result;
	}

	/**
	 * 生成带分页的参数查询参数
	 * @param params
	 * @param pageSize 每页大小
	 * @param current 当前页
	 * @作者 田应平
	 * @返回值类型 HashMap<String,Object>
	 * @创建时间 2016年12月29日 下午10:06:03
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static HashMap<String, Object> pageParams(final HashMap<String, Object> params,final Integer pageSize,final Integer current){
		params.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
		params.put(ConfigFile.pageSize,pageSize);//每页大小
		return params;
	}

    /**
     * 数据库为Mysql的datatable组装固定分页参数,section读取区间;pageSize每页大小;
     * @作者 田应平
     * @返回值类型 PageFormData
     * @创建时间 2017年1月23日 下午3:45:06
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public final static PageFormData dataTableMysql(final PageFormData pageFormData) throws Exception{
        final String iDisplayLength = "iDisplayLength";
        final String iDisplayStart = "iDisplayStart";
        Integer rows = pageFormData.getInteger(iDisplayLength);
        final Integer start = pageFormData.getInteger(iDisplayStart);
        if(rows == null || start == null) return null;
        Integer current = (start / rows) + 1; //计算当前页
        final String sort = pageFormData.getString("sSortDir_0");//排序关键字
        final String index = pageFormData.getString("iSortCol_0");//排序的列索引
        final String column = pageFormData.getString("mDataProp_"+index);//排序的字段
        final int iColumns = pageFormData.getInteger("iColumns");
        pageFormData.remove("sColumns");
        pageFormData.remove("iColumns");
        pageFormData.remove("sSortDir_0");
        pageFormData.remove("iSortCol_0");
        pageFormData.remove(iDisplayLength);
        pageFormData.remove(iDisplayStart);
        pageFormData.remove("iSortingCols");
        for(int i = 0; i < iColumns; i++){
            pageFormData.remove("bSortable_"+i);
            pageFormData.remove("mDataProp_"+i);
        }
        if(rows == null || rows < 0){
            rows = 50;
        }
        if(current <= 0){
            current = 1;
        }
        final Integer pageSize = rows > 100 ? ConfigFile.size_default : rows;
        pageFormData.put(ConfigFile.section,(current - 1) * pageSize);//读取区间
        pageFormData.put(ConfigFile.pageSize,pageSize);//每页大小
        final String _column = ToolString.sqlInject(column);
        final String _sort = ToolString.sqlInject(sort);
        if(_column != null && _sort != null){
            pageFormData.put("column",_column);//排序字段 order by name desc
            pageFormData.put("order",_sort);//排序关键字(升序|降序)
        }
        return pageFormData;
    }

    /**
     * 获取表单的请求参数,不含文件域,返回的是HashMap<String,String>
     * @param request
     * @作者:田应平
     * @创建时间 2019年11月13日 19:14:15
     * @主页 www.fwtai.com
    */
    public final static HashMap<String,String> getFormParams(final HttpServletRequest request){
        final HashMap<String,String> params = new HashMap<String,String>();
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            if(key.equals("_"))continue;
            final String value = request.getParameter(key);
            if(value != null && value.length() >0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                if(value.equalsIgnoreCase("undefined"))
                    continue;
                if(value.equalsIgnoreCase("null"))
                    continue;
                params.put(key,value.trim());
            }
        }
        return params;
    }

    /**
     * 获取表单的请求参数,不含文件域,返回的是线程安全的ConcurrentHashMapString,String>
     * @param request
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/13 19:29
    */
    public final static ConcurrentHashMap<String,String> getFormParam(final HttpServletRequest request){
        final ConcurrentHashMap<String,String> params = new ConcurrentHashMap<String,String>();
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            if(key.equals("_"))continue;
            final String value = request.getParameter(key);
            if(value != null && value.length() > 0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                if(value.equalsIgnoreCase("undefined"))
                    continue;
                if(value.equalsIgnoreCase("null"))
                    continue;
                params.put(key,value.trim());
            }
        }
        return params;
    }

    /**
     * 获取表单的请求参数,不含文件域
     * @param request
     * @作者:田应平
     * @创建时间 2017年10月21日 16:03:16
     * @主页 www.fwtai.com
    */
    public final static PageFormData getFormData(final HttpServletRequest request){
        final PageFormData params = new PageFormData();
        final Enumeration<String> paramNames = request.getParameterNames();
        while(paramNames.hasMoreElements()){
            final String key = paramNames.nextElement();
            if(key.equals("_"))continue;
            final String value = request.getParameter(key);
            if(value != null && value.length() > 0){
                if(value.length() == 1 && value.equals("_"))
                    continue;
                if(value.equalsIgnoreCase("undefined"))
                    continue;
                if(value.equalsIgnoreCase("null"))
                    continue;
                params.put(key,value.trim());
            }
        }
        return params;
    }

	/**
	 * 获取表单的所有请求参数
	 * @param request
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2020/1/8 21:25
	*/
    public final static JSONObject getRequestData(final HttpServletRequest request){
        try {
            final BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(),"UTF-8"));
            final StringBuilder sb = new StringBuilder();
            String s = "";
            while((s = in.readLine()) != null){
                sb.append(s);
            }
            in.close();
            return JSONObject.parseObject(sb.toString().trim());
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

	/**
	 * 获取由HttpClient发送的数据的HttpServletRequest请求参数
	 * @作者 田应平
	 * @QQ 444141300
     * @param request 请求参数,默认的字符编码为"UTF-8"
	 * @创建时间 2018年7月3日 09:33:19
	*/
    public final static String getHttpClientRequest(final HttpServletRequest request) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = request.getInputStream();
        final InputStreamReader isr = new InputStreamReader(is,"UTF-8");
        final BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 获取由HttpClient发送的数据的HttpServletRequest请求参数
     * @作者 田应平
     * @QQ 444141300
     * @param request 请求参数
     * @param charsetName 字符编码,如 "UTF-8"
     * @创建时间 2018年7月3日 09:39:00
    */
    public final static String getHttpClientRequest(final HttpServletRequest request,final String charsetName) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final InputStream is = request.getInputStream();
        final InputStreamReader isr = new InputStreamReader(is,charsetName);
        final BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null){
            sb.append(s);
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 返回没有操作权限的code=401,msg=没有操作权限
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/3/1 0:13
     */
    public final static String notAuthorized(){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code401);
        json.put(ConfigFile.msg,ConfigFile.msg401);
        return json.toJSONString();
    }

    /**
     * token无效或已过期,请重新登录
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/4/19 21:00
    */
    public final static String tokenInvalid(){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code205);
        json.put(ConfigFile.msg,ConfigFile.TOKEN_INVALID);
        return json.toJSONString();
    }

    public final static String loginTimeout(){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code205);
        json.put(ConfigFile.msg,ConfigFile.msg205);
        return json.toJSONString();
    }

    /**
     * 账号或密码错误
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/1 0:16
     */
    public final static String invalidUserInfo(){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code206);
        json.put(ConfigFile.msg,ConfigFile.msg206);
        return json.toJSONString();
    }

    /**
     * 用于生成Easyui里的datagrid数据表格
     * @param total 总条数|总记录数
     * @param listData 数据库返回的list集合数据
     * @作者 田应平
     * @返回值类型 String
     * @创建时间 2020年3月22日 22:51:46
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
     */
    public final static String dataTableOK(List<Object> listData,Object total,final Object sEcho){
        final JSONObject json = new JSONObject();
        if(listData != null && listData.size() <= 0){
            listData = new ArrayList();
            total = 0;
            json.put(ConfigFile.code,ConfigFile.code201);
            json.put(ConfigFile.msg,ConfigFile.msg201);
        }else{
            json.put(ConfigFile.code,ConfigFile.code200);
            json.put(ConfigFile.msg,ConfigFile.msg200);
        }
        json.put("sEcho",sEcho);
        json.put(ConfigFile.recordsTotal,total);
        json.put(ConfigFile.recordsFiltered,total);
        json.put(ConfigFile.data,listData);
        return json.toJSONString();
    }

    public final static String dataTableException(final Object sEcho){
        final JSONObject json = new JSONObject();
        json.put(ConfigFile.code,ConfigFile.code204);
        json.put(ConfigFile.msg,ConfigFile.msg204);
        json.put("sEcho",sEcho);
        json.put(ConfigFile.recordsTotal,0);
        json.put(ConfigFile.recordsFiltered,0);
        json.put(ConfigFile.data,new ArrayList<Object>());
        return json.toJSONString();
    }

    /**
     * 从请求参数或者header获取token
     * @param request
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/23 23:26
    */
    public final static String getToken(final HttpServletRequest request){
        String token = ToolString.wipeString(request.getParameter("token"));
        if(token == null || token.length() <= 0){
            token = ToolString.wipeString(request.getHeader("token"));;
        }
        return token;
    }

    /**
     * 从请求参数或者header获取双token
     * @param request
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/5/23 23:26
    */
    public final static HashMap<String,String> getDoubleToken(final HttpServletRequest request){
        final HashMap<String,String> map = new HashMap<String,String>();
        final String refresh_token = ToolString.wipeString(request.getHeader(ConfigFile.REFRESH_TOKEN));
        final String access_token = ToolString.wipeString(request.getHeader(ConfigFile.ACCESS_TOKEN));
        final String url_refresh_token = ToolString.wipeString(request.getParameter(ConfigFile.REFRESH_TOKEN));
        final String url_access_token = ToolString.wipeString(request.getParameter(ConfigFile.ACCESS_TOKEN));
        final String refresh = refresh_token == null ? url_refresh_token : refresh_token;
        final String access = access_token == null ? url_access_token : access_token;
        if(refresh != null && access != null){
            map.put(ConfigFile.ACCESS_TOKEN,access);
            map.put(ConfigFile.REFRESH_TOKEN,refresh);
        }
        return map;
    }

    /**
     * 封装文件上传,指定上传的目录,返回值HashMap_String,Object>,files,params,error
     * @param baseDir 该值的结尾必须要带 /
     * @param limit 如果该值为null或为负数时则不限制文件数
     * @param verify 文件是否是必填项
     * @return HashMap_key,Object>,其中key可能为error,files,params，要做error判断再做页面处理,若 key 不为空时,那files则是　ArrayList_HashMap_String,String;params是PageFormData
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年6月1日 21:04:37
    */
    public final static HashMap<String,Object> uploadImage(final HttpServletRequest request,final String baseDir,final Integer limit,final boolean verify){
        //final PageFormData formData = new PageFormData().build(request);
        final PageFormData formData = new PageFormData(request);
        final HashMap<String,Object> objectHashMap = new HashMap<String,Object>(2);
        final String error = "error";
        MultipartHttpServletRequest mhsr = null;
        try {
            mhsr =  (MultipartHttpServletRequest) request;
        } catch (Exception e){
            if(verify){
                objectHashMap.put(error,"请上传文件");
                return objectHashMap;
            }
        }
        if(mhsr == null){
            if(verify){
                objectHashMap.put(error,"未上传文件");
                return objectHashMap;
            }
        }
        final Map<String,MultipartFile> map = mhsr.getFileMap();
        if(map == null || map.size() <=0){
            if(verify){
                objectHashMap.put(error,"请选择上传文件");
                return objectHashMap;
            }
        }
        final DiskFileItemFactory fac = new DiskFileItemFactory();
        final ServletFileUpload upload = new ServletFileUpload(fac);
        String originalPath = null;
        final ArrayList<HashMap<String,String>> fileList = new ArrayList<HashMap<String,String>>();
        try {
            upload.setHeaderEncoding("utf-8");
            mhsr.setCharacterEncoding("utf-8");
            /* 在 spring boot 的jar下这个方式行不通啦!!!
            final String sys = File.separator;
            String dirType = sys;
            final String savePath = mhsr.getSession().getServletContext().getRealPath(dirType);
            final File directory = new File(savePath);
            if(!directory.exists()){
                directory.mkdirs();
            }*/
            if(limit != null && limit > 0){
                if(map != null && map.size() > limit){
                    objectHashMap.put(error,"图片数量已超过限制");
                    return objectHashMap;
                }
            }
            boolean bl = false;
            for(final String key : map.keySet()){
                final MultipartFile mf = mhsr.getFile(key);
                if(mf.getSize() > 5242880l){
                    bl = true;
                    break;
                }
                final String name = mf.getOriginalFilename();
                final String extName = name.substring(name.lastIndexOf("."));
                final String fileName = ToolString.getIdsChar32() + extName;
                final File fileDir = new File(baseDir);
                if(!fileDir.exists()){
                    fileDir.mkdirs();
                }
                originalPath = baseDir + fileName;
                mf.transferTo(new File(originalPath));
                final HashMap<String,String> maps = new HashMap<String,String>(3);
                maps.put("originalName",name);
                maps.put("fileName",fileName);
                maps.put("path",originalPath);
                fileList.add(maps);
            }
            if(bl){
                for(int i = 0; i < fileList.size(); i++){
                    delFileByThread(fileList.get(i).get("path"));
                }
                objectHashMap.put(error,"操作失败,某个文件过大");
                return objectHashMap;
            }
            if(fileList.size() > 0){
                objectHashMap.put("files",fileList);
            }
            if(formData.size() > 0){
                objectHashMap.put("params",formData);
            }
            return objectHashMap;
        } catch (Exception e) {
            for(int i = 0; i < fileList.size(); i++){
                delFileByThread(fileList.get(i).get("path"));
            }
            objectHashMap.put(error,"操作失败,处理文件失败");
            return objectHashMap;
        }
    }

    /**
     * 开线程访问服务器删除图片
     * @date 2019年10月31日 16:41:40
     */
    public final static void delFileByThread(final String filePath) {
        try {
            new Thread(){
                public void run() {
                    final File file = new File(filePath);
                    if(file.isFile()){
                        file.delete();
                    }
                }
            }.start();
        } catch (Exception e){}
    }
}