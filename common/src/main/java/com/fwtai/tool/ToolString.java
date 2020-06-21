package com.fwtai.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fwtai.bean.PageFormData;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串的工具类|含json对象或json数组解析
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2015年11月27日 18:25:12
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ToolString implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 转码
	 * @param object
	 * @return
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2015-9-6 下午5:23:06 
	 * @QQ号码 444141300
	 * @官网 http://www.blidian.com
	*/
	public final static String transCoding(final Object object){
		if(object != null){
			try {
				return new String(object.toString().getBytes("ISO8859-1"),"UTF-8");
			} catch (Exception e){
				return null;
			}
		}
		return null;
	}

    /**多线程下生成32位唯一的字符串*/
    public final static String getIdsChar32(){
        final ThreadLocalRandom random = ThreadLocalRandom.current();
        return new UUID(random.nextInt(),random.nextInt()).toString().replaceAll("-","");
    }

    /**
	 * 验证是否为空,为空时返回true,否则返回false,含obj是否为 _单独的下划线特殊字符,是则返回true,否则返回false
	 * @作者: 田应平
	 * @主页 www.fwtai.com
	 * @创建日期 2016年8月18日 17:34:24
	*/
	public final static boolean isBlank(final Object obj){
		if(obj == null)return true;
		final String temp = String.valueOf(obj);
		if(temp.toLowerCase().equals("null"))return true;
		final String key = obj.toString().replaceAll("\\s*", "");
		if(key.equals("") || key.length() <= 0 )return true;
		if(key.length() == 1 && key.equals("_"))return true;
		if(obj instanceof List<?>){
			final List<?> list = (List<?>) obj;
            return list == null || list.size() <= 0;
        }
		if(obj instanceof Map<?,?>){
			final Map<?,?> map = (Map<?,?>) obj;
            return map == null || map.size() <= 0;
        }
		if(obj instanceof String[]){
			boolean flag = false;
			final String[] require = (String[])obj;
			for(int i = 0; i < require.length; i++){
				if(require[i] == null || require[i].length() == 0 || require[i].replaceAll("\\s*", "").length() == 0){
					flag = true;
					break;
				}
			}
			return flag;
		}
		if(obj instanceof HashMap<?,?>){
			final HashMap<?, ?> hashMap = (HashMap<?,?>) obj;
            return hashMap == null || hashMap.size() <= 0;
        }
		if(obj instanceof PageFormData){
			final PageFormData hashMap = (PageFormData)obj;
            return hashMap == null || hashMap.size() <= 0;
        }
		if(obj instanceof JSONObject){
			final JSONObject json = (JSONObject)obj;
			return json.isEmpty();
		}
		return false;
	}
	
	/**如果为空则赋值为""*/
	public final static String setBlank(final Object obj){
		if (isBlank(obj))return "";
		return String.valueOf(obj);
	}
	
	/**如果为空则赋值为defaultValue*/
	public final static String setBlank(final Object obj,final String defaultValue){
		if (isBlank(obj))return defaultValue;
		return String.valueOf(obj);
	}
	
	/**
	 * 去除空格,如果为空则返回null,若有且只有_也只返回null否则去除前后空格后返回
	 * @param obj
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2015-8-18 下午2:03:25 
	 * @QQ号码 444141300
	 * @注释 \s 匹配任何空白字符，包括空格、制表符、换页符,* 匹配前面的子表达式零次或多次。
	 * @官网 http://www.fwtai.com
	*/
	public final static String wipeObject(final Object obj){
		if(isBlank(obj))return null;
		final String key = obj.toString().replaceAll("\\s*", "");
		if(key.length() == 1 && key.equals("_"))return null;
		return key;
	}

    /**
     * 去除空格,如果为空则返回null,若有且只有_也只返回null否则去除前后空格后返回
     * @param obj
     * @作者 田应平
     * @返回值类型 String
     * @创建时间 2015-8-18 下午2:03:25
     * @QQ号码 444141300
     * @注释 \s 匹配任何空白字符，包括空格、制表符、换页符,* 匹配前面的子表达式零次或多次。
     * @官网 http://www.fwtai.com
     */
	public final static String wipeString(final String obj){
		if(obj == null || obj.length() <= 0)return null;
		final String key = obj.replaceAll("\\s*", "");
		if(key.length() == 1 && key.equals("_"))return null;
		return key;
	}
	
	/**
	 * 是否大于指定长度,若大于返回true否则返回false
	 * @param value
	 * @param length
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015-12-22 上午3:25:17 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean isTooLong(final String value,final int length){
        return value.length() > length;
    }
	
	/**
	 * 验证输入值是否是正整数,是正整数返回true,否则返回false
	 * @param value
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015-12-9 下午6:11:36 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static boolean checkNumber(final String value){
		String reg = "^\\d*[1-9]\\d*$";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(value); 
		return m.matches(); 
	}
	
	/**
	 * 生成时间格式:yyyy-MM-dd HH:mm:ss
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2017年1月10日 09:35:02 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final String getTime(){
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());//设置日期格式
	}

    /**
     * ids字符串数组转换为ArrayList<String>,默认是以逗号,隔开
     * @param ids 格式如:12958292,12951500,12977780,12997129
     * @作者 田应平
     * @返回值类型 ArrayList<String>
     * @创建时间 2020年4月10日 01:39:15
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
     */
    public final static ArrayList<String> keysToList(final String ids){
        return keysToList(ids,",");
    }

	/**
	 * ids字符串数组转换为ArrayList<String>
	 * @param ids 格式如:12958292,12951500,12977780,12997129
	 * @param splitFlag 以 splitFlag 为拆分标识
	 * @作者 田应平
	 * @返回值类型 ArrayList<String>
	 * @创建时间 2016年1月21日 12:20:36
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static ArrayList<String> keysToList(final String ids,String splitFlag){
		if(ToolString.isBlank(ids))return null;
		final ArrayList<String> list = new ArrayList<String>();
		if (isBlank(splitFlag)){
			splitFlag = ",";
		}
		final String[] arr = ids.split(splitFlag);
		if(arr.length == 0)return null;
		for(int i = 0; i < arr.length; i++){
			list.add(arr[i]);
		}
		return list;
	}
	
	/**
	 * ids字符串数组转换为String
	 * @param ids 格式如:12958292,12951500,12977780,12997129
	 * @param splitFlag 以 splitFlag 为拆分标识
	 * @作者 田应平
	 * @返回值类型 List< String >
	 * @创建时间 2017年1月10日 09:34:24
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static String[] keysToArarry(final String ids,String splitFlag){
		if(ToolString.isBlank(ids))return null;
		if (isBlank(splitFlag)){
			splitFlag = ",";
		}
		final String[] arr = ids.split(splitFlag);
		if(arr.length == 0)return null;
		return arr;
	}
	
	/**
	 * 根据完整文件路径删除文件
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2017年1月10日 09:34:49 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean deleFileByRealPath(final String filePath){
		if(filePath != null && filePath.length() > 0){
			try {
				File f = new File(filePath);
				if(f.exists()){
					f.delete();
				}
				return true ;
			} catch (Exception e){}
		}
		return false ;
	}
	
	/**
	 * 删除图片
	 * @param request
	 * @param webImageUrl url相对路径,如group_images/1445610757864195059.jpg
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015年11月13日 13:13:08 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean deleFileByWebPath(final HttpServletRequest request,final String webImageUrl){
		final String sys = File.separator;
		final String projectRealPath = request.getSession().getServletContext().getRealPath(sys);
		final String realImagePath = projectRealPath + webImageUrl;
		try {
			File f = new File(realImagePath);
			if(f.exists()){
				f.delete();
			}
			return true ;
		} catch (Exception e){
			return false ;
		}
	}
	
	/**
	 * 删除图片(含压缩路径里的文件)
	 * @param request
	 * @param webImageUrl 图片的网页路径如 help_images/1446277143126167930.jpg
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2015-10-31 下午4:23:10 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean deleFileByRealPath(final HttpServletRequest request,final String webImageUrl){
		final String webImageResized = webImageUrl.substring(0,webImageUrl.lastIndexOf("/")+1)+"resized"+webImageUrl.substring(webImageUrl.lastIndexOf("/"),webImageUrl.length());//压缩图片路径
		final String sys = File.separator;
		final String projectRealPath = request.getSession().getServletContext().getRealPath(sys);//获取路径路径D:\MyTools\tomcat7065\webapps\api\,项目路径
		final String realImagePath = projectRealPath + webImageUrl;//D:\MyTools\tomcat7065\webapps\api\help_images/1446277143126167930.jpg//原图片路径
		final String realImageResizedPath = projectRealPath + webImageResized;
		final boolean flagRealImagePath = ToolString.deleFileByRealPath(realImagePath);//删除原图片
		final boolean flagRealImageResizedPath = ToolString.deleFileByRealPath(realImageResizedPath);//删除压缩图片
        return flagRealImagePath && flagRealImageResizedPath;
    }
	
	/**
	 * html网页的img替换规则
	 * @param html
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年1月23日 17:33:21
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	 * @示例 html = html.replaceAll("src=\"/upimg","src=\""+url+"/upimg");
	*/
	public final static String htmlReplace(final String html,final String flagOld,final String flagNew){
		return html.replaceAll(flagOld,flagNew);
	}
	
	/**
	 * 判断一个字符串是否是纯数字字符串
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2017年3月8日 上午8:23:11
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static boolean isNumberStr(final String str){
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e){
			return false;
		}
	}
	
	/**获取文件的后缀名|扩展名*/
	public final static String getFileExt(final String str){
		return str.substring(str.lastIndexOf('.')+1,str.length());
	}
	
	/**验证指定的key字符串是否在以逗号,隔开的字符串里,存在返回true,为false时说明不存在,用法:if(!ToolString.existKey("xls,xlsx",key)){}*/
	public final static boolean existKey(final String keys,final String key){
		final String[] array = keys.split(",");
		boolean b = false;
		for(int i = 0; i < array.length; i++){
			if(array[i].equalsIgnoreCase(key)){
				b = true;
				break;
			}
		}
		return b;
	}
	
	/**获取文件的文件名,不含扩展名后缀名*/
	public final static String getFileName(final String str){
		return str.substring(str.lastIndexOf('/')+1,str.lastIndexOf('.'));
	}
	
	/**获取文件的文件名,含扩展名后缀名*/
	public final static String getFileNameExt(final String str){
		return str.substring(str.lastIndexOf('/')+1);
	}
	
	/**
	 * <strong style='color:#f69;'>判断json类型;返回1是json对象;返回2json数组;返回0不是有效的json数据,如要判断是否是json对象判断是否等1或json数组等于2即可</strong>
	 * @param json 字符串
	 * @作者 田应平
	 * @返回值类型 int <strong style='color:#f69;'>返回1是json对象;返回2json数组;返回0不是有效的json数据</strong>
	 * @创建时间 2017年3月7日 下午8:46:18
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
    public final static int jsonType(final String json){
        if(json == null || json.length() <= 0){
            return 3;
        }
        try {
            if(Pattern.matches("^\\{\".*\\}$",json)){
                JSONObject.parseObject(json);
                return 1;
            }else if(Pattern.matches("^\\[\\{\".*\\]$",json)){
                JSONArray.parseArray(json);
                return 2;
            }else {
                return 0;
            }
        } catch (Exception e){
            return 4;
        }
    }

    /**
     * <strong style='color:#f69;'>解析json对象字符串,HashMap里的key未做处理</strong>
     * @提示 <strong style='color:#369;'>json对象就是以{"开头,即HashMap<String,String>;json数组就是以[{"开头,即ArrayList<HashMap<String,String>>;json全都是String</strong>
     * @作者 田应平
     * @创建时间 2017年3月7日 20:42:05
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public final static HashMap<String,String> parseJsonObject(final Object json){
        final HashMap<String,String> jsonMap = new HashMap<String,String>();
        if(json == null || json.toString().length() <= 0) return jsonMap;
        try {
            final HashMap<String,String> map = JSON.parseObject(json.toString(),new TypeReference<HashMap<String,String>>(){});
            return map == null ? jsonMap : map;
        } catch (Exception e){
            return jsonMap;
        }
    }

    /**
     * 解析json对象字符串,HashMap里的key未做处理
     * @param json
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年5月15日 18:26:29
    */
    public final static JSONObject parseJsonObj(final String json){
        final JSONObject jsonObject = new JSONObject();
        if(json == null || json.length() <= 0) return jsonObject;
        try {
            return JSONObject.parseObject(json);
        } catch (Exception e){
            return jsonObject;
        }
    }

    /**
     * <strong style='color:#f69;'>解析json数组字符串,ArrayList里的HashMap的key未做处理</strong>
     * @提示 <strong style='color:#369;'>json对象就是以{"开头,即HashMap<String,String>;json数组就是以[{"开头,即ArrayList<HashMap<String,String>>;json全都是String</strong>
     * @作者 田应平
     * @创建时间 2018年6月14日 23:14:02
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public final static ArrayList<HashMap<String,String>> parseJsonArray(final Object jsonArray){
        final ArrayList<HashMap<String,String>> listResult = new ArrayList<HashMap<String,String>>();//初始化,以免出现空指针异常
        if(jsonArray == null || jsonArray.toString().length() <= 0) return listResult;
        try {
            ArrayList<HashMap<String,String>> result = JSON.parseObject(jsonArray.toString(),new TypeReference<ArrayList<HashMap<String,String>>>(){});
            return result == null ? listResult : result;
        } catch (Exception e){
            return listResult;
        }
    }

    /**
     * 解析json数组字符串,List里的PageFormData的key未做处理
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月29日 13:52:54
    */
    public final static List<PageFormData> parseArrayPageFormData(final Object array){
        final List<PageFormData> list = new ArrayList<PageFormData>();
        if(isBlank(array)){return list;}
        final JSONArray jsonArray = JSONArray.parseArray(array.toString());
        for(int i = 0; i < jsonArray.size(); i++){
            final JSONObject json = jsonArray.getJSONObject(i);
            final PageFormData pageData = new PageFormData();
            for(final String key : json.keySet()){
                pageData.put(key,json.get(key));
            }
            list.add(pageData);
        }
        return list;
    }

    /**
     * 解析json数组字符串,List里的Map<String,Object>的key未做处理
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月29日 13:52:54
    */
    public final static List<Map<String,Object>> parseArrayMap(final Object array){
        final List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        if(isBlank(array)){return list;}
        final JSONArray jsonArray = JSONArray.parseArray(array.toString());
        for(int i = 0; i < jsonArray.size(); i++){
            final JSONObject json = jsonArray.getJSONObject(i);
            final Map<String,Object> map = new HashMap<String,Object>();
            for(final String key : json.keySet()){
                map.put(key,json.get(key));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 解析json数组字符串,List里的HashMap<String,Object>的key未做处理
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年12月3日 09:19:26
    */
    public final static List<HashMap<String,Object>> parseListHashMap(final Object array){
        final List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        if(isBlank(array)){return list;}
        final JSONArray jsonArray = JSONArray.parseArray(array.toString());
        for(int i = 0; i < jsonArray.size(); i++){
            final JSONObject json = jsonArray.getJSONObject(i);
            final HashMap<String,Object> map = new HashMap<String,Object>();
            for(final String key : json.keySet()){
                map.put(key,json.get(key));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * 解析json数组字符串,ArrayList里的HashMap<String,Object>的key未做处理
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年12月3日 09:21:02
    */
    public final static ArrayList<HashMap<String,Object>> parseArrayListHashMap(final Object array){
        final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        if(isBlank(array)){return list;}
        final JSONArray jsonArray = JSONArray.parseArray(array.toString());
        for(int i = 0; i < jsonArray.size(); i++){
            final JSONObject json = jsonArray.getJSONObject(i);
            final HashMap<String,Object> map = new HashMap<String,Object>();
            for(final String key : json.keySet()){
                map.put(key,json.get(key));
            }
            list.add(map);
        }
        return list;
    }

    /**
     * <strong style='color:#f69;'>将json字符串解析为一个 JavaBean 对象</strong>
     * @param cls 转换的目标对象
     * @用法 调用时写 StringTools.jsonToObject(json, javaBean.class);
     * @作者 田应平
     * @返回值类型 T
     * @创建时间 2017年3月7日 下午9:12:19
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public final static <T> T parseJsonToBean(final String json,final Class<T> cls){
		T t = null;
		try {
			t = JSON.parseObject(json,cls);
		} catch (Exception e){
			e.printStackTrace();
		}
		return t;
	}
    
    /**
     * <strong style='color:#f69;'>将json数组字符串 解析成为一个 List< JavaBean ></strong>
     * @param cls 转换的目标对象
     * @用法 如果是实体类则在调用时写 jsonToListObject(json, javaBean.class);或 jsonToListObject(json,String.class);
     * @作者 田应平
     * @返回值类型 List< T >
     * @创建时间 2017年3月7日 21:15:00
     * @QQ号码 444141300
     * @官网 http://www.fwtai.com
    */
    public final static <T> List<T> parseJsonToListBean(final String jsonArray,final Class<T> cls){
		List<T> list = new ArrayList<T>();//初始化,以免出现空指针异常
		try {
			list = JSON.parseArray(jsonArray,cls);
		} catch (Exception e){
		}
		return list;
	}
    
    /**
     * 验证中文
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
    */ 
    public final static boolean checkChinese(String chinese) { 
        String regex = "^[\u4E00-\u9FA5]+$"; 
        return Pattern.matches(regex,chinese); 
    }
    
    /**判断是否还有中文或英文字符串*/
    public final static int checkString(final String str){
		int res = -1;
		if(str != null){
			for (int i = 0; i < str.length(); i++){
				// 只要字符串中有中文则为中文
				if(!checkChar(str.charAt(i))){
					res = 0;// "中文";
					break;
				} else {
					res = 1;// "英文";
				}
			}
		}
		return res;
	}

    /**判断是否还有中文或英文字符串*/
	public final static boolean checkChar(final char ch){
        // 英文
// 中文
        return (ch + "").getBytes().length == 1;
	}
	
	/**编辑时判断数据库是否已经存在,queryResultKey是根据要编辑的数据去查询数据库查询出来的主键数值,一般指的是主键;keyEdit是要做对比的对象,一般的是主键,即是根据该keyEdit主键去编辑的数据行*/
	public final static boolean editExistKey(final String queryResultKey,final String keyEdit){
		return !isBlank(queryResultKey) && queryResultKey.equals(keyEdit);
	}
	
	/**判断是否是合格的yyyy-MM-dd时间格式*/
	public final static boolean checkDate(final String date){
		if (isBlank(date))return false;
		try{
			new SimpleDateFormat("yyyy-MM-dd").parse(date);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 判断是否是合格的指定时间格式
	 * @param date
	 * @param expression  指定日期格式:yyyy-MM-dd HH:mm:ss
	 * @作者 田应平
	 * @返回值类型 boolean
	 * @创建时间 2017年9月14日 上午11:39:46
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean checkDate(final String date,final String expression){
		if (isBlank(date) || isBlank(expression))return false;
		try {
			new SimpleDateFormat(expression).parse(date);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 把字符串写入到指定物理路径文件
	 * @作者 田应平
	 * @创建时间 2017年10月29日 上午11:04:43
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public final static boolean writeFile(final String content,final String filePath){
		final File file = new File(filePath);
		try {
			final FileWriter fw = new FileWriter(file);
	    	fw.write(content);
	    	fw.close();
	    	return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * 获取window 用户桌面路径
	 * @作者 田应平
	 * @创建时间 2017年11月2日 上午8:29:20
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
    public static String getOperationSystemByUserDirectory() {
        final javax.swing.filechooser.FileSystemView fsv = javax.swing.filechooser.FileSystemView.getFileSystemView();
        return fsv.getHomeDirectory().getPath();
    }

	/**
	 * 替换关键字,防止SQL注入过滤
	 * @param sqlKey
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月1日 14:24:50
	*/
    public final static String sqlInject(String sqlKey){
        if(sqlKey == null || sqlKey.length() <= 0)return null;
        //去掉'|"|;|\字符
        sqlKey = sqlKey.replaceAll("'","").replaceAll("\"","").replaceAll(";", "").replaceAll("--", "").replace( "\\", "");
        //转换成小写
        sqlKey = sqlKey.toLowerCase();
        //非法字符
        final String[] keywords = {"master","truncate","insert","select","delete","update","declare","alert","drop"};
        //判断是否包含非法字符
        for(final String keyword : keywords){
            if(sqlKey.contains(keyword)){
                return null;
            }
        }
        return sqlKey;
    }

	/**
	 * 判断运行系统是linux还是windows,是linux返回true,否则返回false
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018年1月23日 15:50:19
	*/
	public static boolean isLinuxOS(){
		final String os = System.getProperties().getProperty("os.name");
		if (os != null && os.toLowerCase().indexOf("linux") > -1)return true;
		return false;
	}

    /**
     * json 字符转换为 HashMap 对象
     * @param json 字符串
     * @return
    */
    public final static HashMap<String,String> jsonConvertHashMap(final String json){
        return JSON.parseObject(json,HashMap.class);
    }

    /**
     * 判断某个字符串中出现了几次
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/8/14 20:57
    */
    public final static int count(final String text,final String sub){
        int count = 0, start = 0;
        while((start=text.indexOf(sub,start))>=0){
            start += sub.length();
            count ++;
        }
        return count;
    }

    /**
     * 将请求参数转换为xml格式的string
     * @param parameters 请求参数
     * @return
    */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**检测传入的参数是否排序关键字,true说明是排序关键字*/
    public final static boolean checkInjectKey(final String key){
        final String[] arr = {"ASC","DESC"};
        for(int i = 0; i < arr.length; i++){
            if(key.equalsIgnoreCase(arr[i])){
                return true;
            }
        }
        return false;
    }

    /**检测传入的参数是否带sql的注释符号*/
    public final static boolean checkInject(final String key){
        return key.contains("--");
    }

    /**
     * (高级筛选)检测传入指定字段[示例]
     * @param column 需要检测的列
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/30 12:41
    */
    public final static boolean checkAdvancedFiltrate(final String column){
        if(column != null && column.length() > 0){
            return checkExistColumn(column,new String[]{"eval_level","money"});
        }
        return false;
    }

    /**
     * 检测传入指定字段
     * @param column 需要检测的列
     * @param columns 需要检测的列的集合
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/11/30 12:40
    */
    public final static boolean checkExistColumn(final String column,final String... columns){
        if(column == null )return false;
        for(int x = 0; x < columns.length; x++){
            if(column.equals(columns[x])){
                return true;
            }
        }
        return false;
    }

    /**处理手机号中间4位替换成**/
    public final String replacePhone(final String phone){
        return phone.replace(phone.substring(3,7),"****");
    }

    /** Blob转为String类型 */
    public final static String blobConvertStr(final byte[] bytes){
        return new String(bytes);
    }

    /** Blob转为String类型 */
    public final static String blobConvertStr(final byte[] bytes,final String charset){
        return new String(bytes,Charset.forName(charset));
    }

    /**
     * <strong style='color:#f69;'>解析json对象字符串,HashMap里的key未做处理</strong>,final
     * @提示 <strong style='color:#369;'>json对象就是以{"开头,即HashMap<String,String>;json数组就是以[{"开头,即ArrayList<HashMap<String,String>>;json全都是String</strong>
     * @作者 田应平
     * @创建时间 2017年3月7日 20:42:05
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public final static HashMap<String,String> parseJsonObjectOriginal(final Object json){
        final HashMap<String, String> jsonMap = new HashMap<String, String>();
        if(isBlank(json)) return jsonMap;
        try {
            final JSONObject jsonObject = JSONObject.parseObject(json.toString());
            for (final String key : jsonObject.keySet()){
                final Object obj = jsonObject.get(key);
                if(obj != null){
                    final String value = obj.toString().trim();
                    if(value.length() <= 0)
                        continue;
                    if(value.length() == 1 && value.equals("_"))
                        continue;
                    jsonMap.put(key,value);
                }
            }
            return jsonMap;
        } catch (Exception e){
            return jsonMap;
        }
    }

    /**
     * <strong style='color:#f69;'>解析json数组字符串,ArrayList里的HashMap的key未做处理</strong>,final
     * @提示 <strong style='color:#369;'>json对象就是以{"开头,即HashMap<String,String>;json数组就是以[{"开头,即ArrayList<HashMap<String,String>>;json全都是String</strong>
     * @作者 田应平
     * @创建时间 2018年6月14日 23:14:02
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public final static ArrayList<HashMap<String,String>> parseJsonArrayOriginal(final Object jsonArray){
        final ArrayList<HashMap<String, String>> listResult = new ArrayList<HashMap<String, String>>();
        if(isBlank(jsonArray)) return listResult;
        try {
            ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();//初始化,以免出现空指针异常
            list = JSON.parseObject(jsonArray.toString(), new TypeReference<ArrayList<HashMap<String, String>>>(){});
            if(list != null && list.size() > 0){
                for (int i = 0; i < list.size(); i++){
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    final HashMap<String,String> map = list.get(i);
                    for(final String key : map.keySet()){
                        final Object obj = map.get(key);
                        if(obj != null){
                            final String value = obj.toString().trim();
                            if(value.length() <= 0)
                                continue;
                            if(value.length() == 1 && value.equals("_"))
                                continue;
                            hashMap.put(key,value);
                        }
                    }
                    listResult.add(hashMap);
                }
                return listResult;
            } else {
                return listResult;
            }
        } catch (Exception e){
            return listResult;
        }
    }

    /**Unicode转中文汉字*/
    public final static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }

    /**
     * 适用于element-ui的批量删除操作,返回值可能是list<String>也可能是list<Bean>
     * @param jsonArray
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020年5月28日 21:04:08
    */
    public final static ArrayList<Object> jsonArrayToList(final Object jsonArray){
        final ArrayList<Object> lists = new ArrayList<>();
        final JSONArray arrays = parseJSONArray(String.valueOf(jsonArray));
        for(int x = 0; x < arrays.size(); x++){
            lists.add(arrays.get(x));
        }
        return lists;
    }

    /**
     * 解析json对象字符串
    */
    public final static JSONArray parseJSONArray(final String json){
        final JSONArray array = new JSONArray();
        if(json == null || json.length() <= 0) return array;
        try {
            return JSONObject.parseArray(json.trim());
        } catch (Exception e){
            return array;
        }
    }
}