package com.fwtai.datasource;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**dao底层操作处理工具类*/
@Repository
public class DaoHandle{

    @Resource(name="sqlSessionHandle")
    private SqlSessionTemplate sqlSession;

	/**
	 * 通用的更新;删除;插入添加
	 * @作者 田应平
	 * @返回值类型 int
	 * @创建时间 2016年12月24日 23:00:14
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public int execute(final String sqlMapId){
		return sqlSession.update(sqlMapId);
	}

	/**
	 * 通用的更新;删除;插入添加
	 * @作者 田应平
	 * @返回值类型 int
	 * @创建时间 2016年12月24日 23:00:09
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public int execute(final String sqlMapId,final Object objParam){
		return sqlSession.update(sqlMapId,objParam);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:00:55
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Integer queryForInteger(final String sqlMapId){
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:01:35
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Integer queryForInteger(final String sqlMapId, final Object objParam){
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:00:55
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Long queryForLong(final String sqlMapId){
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回Integer
	 * @作者 田应平
	 * @返回值类型 Integer
	 * @创建时间 2016年12月24日 23:01:35
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Long queryForLong(final String sqlMapId, final Object objParam){
		return sqlSession.selectOne(sqlMapId,objParam);
	}

    /**
     * 用于金额查询返回BigDecimal
     * @作者 田应平
     * @返回值类型 Integer
     * @创建时间 2016年12月24日 23:01:35
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public BigDecimal queryForBigDecimal(final String sqlMapId){
        return sqlSession.selectOne(sqlMapId);
    }

    /**
     * 用于金额查询返回BigDecimal
     * @作者 田应平
     * @返回值类型 Integer
     * @创建时间 2016年12月24日 23:01:35
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public BigDecimal queryForBigDecimal(final String sqlMapId,final Object objParam){
        return sqlSession.selectOne(sqlMapId,objParam);
    }

	/**
	 * 用于查询返回String
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年11月20日 下午2:24:52
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public String queryForString(final String sqlMapId){
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 用于查询返回String
	 * @作者 田应平
	 * @返回值类型 String
	 * @创建时间 2016年12月25日 00:44:39
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public String queryForString(final String sqlMapId, final Object objParam){
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 根据id去查询,或必须保证sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 Map<String,Object>
	 * @创建时间 2016年12月24日 23:03:07
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Map<String, Object> queryForMap(final String sqlMapId){
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 必须保存sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 Map<String,Object>
	 * @创建时间 2016年12月24日 23:03:49
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public Map<String, Object> queryForMap(final String sqlMapId, final Object objParam){
		return sqlSession.selectOne(sqlMapId,objParam);
	}
	
	/**
	 * 根据id去查询,或必须保证sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 HashMap《String,Object》
	 * @创建时间 2016年12月24日 23:03:07
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String,Object> queryForHashMap(final String sqlMapId){
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 必须保存sql所查询的结果只有一条或限制返回一条数据
	 * @作者 田应平
	 * @返回值类型 HashMap《String,Object》
	 * @创建时间 2016年12月24日 23:03:49
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String, Object> queryForHashMap(final String sqlMapId, final Object objParam){
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 查询返回List《Map》,含分页
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月24日 23:04:14
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public List<Map<String, Object>> queryForListMap(final String sqlMapId){
		return sqlSession.selectList(sqlMapId);
	}

	/**
	 * 查询返回List《Map》,含分页
	 * @param sqlMapId
	 * @param objParam
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月25日 上午12:47:44
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<Map<String, Object>> queryForListMap(final String sqlMapId, final Object objParam){
		return sqlSession.selectList(sqlMapId,objParam);
	}
	
	/**
	 * 查询返回List《HashMap》,含分页
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月24日 23:04:14
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public List<HashMap<String, Object>> queryForListHashMap(final String sqlMapId){
		return sqlSession.selectList(sqlMapId);
	}

	/**
	 * 查询返回List《HashMap》,含分页
	 * @param sqlMapId
	 * @param objParam
	 * @作者 田应平
	 * @返回值类型 List<Map<String,Object>>
	 * @创建时间 2016年12月25日 上午12:47:44
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<HashMap<String,Object>> queryForListHashMap(final String sqlMapId, final Object objParam){
		return sqlSession.selectList(sqlMapId,objParam);
	}

    /**
     * 查询对象数据对象
     * @param sqlMapId
     * @作者 田应平
     * @返回值类型 PageFormData
     * @创建时间 2016年12月24日 下午11:12:57
     * @QQ号码 444141300
     * @官网 http://www.yinlz.com
    */
    public <T> T queryForEntity(final String sqlMapId){
        return sqlSession.selectOne(sqlMapId);
    }

    /**
     * 查询对象数据对象
     * @param sqlMapId
     * @param objParam
     * @作者 田应平
     * @返回值类型 PageFormData
     * @创建时间 2016年12月25日 上午12:46:20
     * @QQ号码 444141300
     * @官网 http://www.yinlz.com
    */
    public <T> T queryForEntity(final String sqlMapId, final Object objParam){
        return sqlSession.selectOne(sqlMapId,objParam);
    }

    /**
     * 返回List集合
     * @作者 田应平
     * @返回值类型 int
     * @创建时间 2016年12月24日 23:00:14
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public <T> List<T> queryListEntity(final String sqlMapId){
        return sqlSession.selectList(sqlMapId);
    }

    /**
     * 带参数的LIST
     * @作者 田应平
     * @返回值类型 int
     * @创建时间 2016年12月24日 23:00:14
     * @QQ号码 444141300
     * @主页 http://www.yinlz.com
    */
    public <T> List<T> queryListEntity(final String sqlMapId, final Object objParam){
        return sqlSession.selectList(sqlMapId, objParam);
    }

    public List<String> queryListString(final String sqlMapId){
        return sqlSession.selectList(sqlMapId);
    }

    public List<String> queryListString(final String sqlMapId, final Object objParam){
        return sqlSession.selectList(sqlMapId, objParam);
    }

    /**
     * 请谨慎使用,如果报错则换成返回List<HashMap<String,Object>>
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月14日 13:55
    */
    public List<HashMap<String,String>> queryForListString(final String sqlMapId){
        return sqlSession.selectList(sqlMapId);
    }
    
    /**
     * 请谨慎使用,如果报错则换成返回List<HashMap<String,Object>>
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月14日 13:55
    */
    public List<HashMap<String,String>> queryForListString(final String sqlMapId,final Object objParam){
        return sqlSession.selectList(sqlMapId,objParam);
    }

    public HashMap<String,String> queryForHashMapString(final String sqlMapId){
        return sqlSession.selectOne(sqlMapId);
    }

    public HashMap<String,String> queryForHashMapString(final String sqlMapId,final Object objParam){
        return sqlSession.selectOne(sqlMapId,objParam);
    }
	
	/**
	 * 查询PageFormData数据对象
	 * @param sqlMapId
	 * @作者 田应平
	 * @返回值类型 PageFormData
	 * @创建时间 2016年12月24日 下午11:12:57
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public PageFormData queryForPageFormData(final String sqlMapId){
		return sqlSession.selectOne(sqlMapId);
	}

	/**
	 * 查询PageFormData数据对象
	 * @param sqlMapId
	 * @param objParam
	 * @作者 田应平
	 * @返回值类型 PageFormData
	 * @创建时间 2016年12月25日 上午12:46:20
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public PageFormData queryForPageFormData(final String sqlMapId,final Object objParam){
		return sqlSession.selectOne(sqlMapId,objParam);
	}

	/**
	 * 查询List《PageFormData》数据对象 ,含分页
	 * @param sqlMapId
	 * @作者 田应平
	 * @返回值类型 List<PageFormData>
	 * @创建时间 2016年12月25日 上午12:46:48
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<PageFormData> queryForListPageFormData(final String sqlMapId){
		return sqlSession.selectList(sqlMapId);
	}

	/**
	 * 查询List《PageFormData》数据对象 ,含分页
	 * @param sqlMapId
	 * @param objParam
	 * @作者 田应平
	 * @返回值类型 List<PageFormData>
	 * @创建时间 2016年12月25日 上午12:46:53
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public List<PageFormData> queryForListPageFormData(final String sqlMapId, final Object objParam){
		return sqlSession.selectList(sqlMapId,objParam);
	}
	
	/**
	 * 带分页查询功能;返回key为total总条数总记录数,key为listData返回的list数据集合
	 * @param params 参数
     * @param sqlMapIdListData 总条数总记录数的sql映射
     * @param sqlMapIdTotal 返回的list数据集合的sql映射
	 * @作者 田应平
	 * @返回值类型 HashMap< String,Object >,含total总条数总记录数;listData分页的数据
	 * @创建时间 2017年1月10日 下午5:56:08
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String,Object> queryForPage(final HashMap<String,Object> params,final String sqlMapIdListData,final String sqlMapIdTotal){
		final HashMap<String,Object> map = new HashMap<String,Object>(2);
		map.put(ConfigFile.total,sqlSession.selectOne(sqlMapIdTotal,params));
		map.put(ConfigFile.rows,sqlSession.selectList(sqlMapIdListData,params));
		return map;
	}
	
	/**
	 * 带分页查询功能;返回key为total总条数总记录数,key为listData返回的list数据集合
	 * @param params 参数
     * @param sqlMapIdListData 总条数总记录数的sql映射
     * @param sqlMapIdTotal 返回的list数据集合的sql映射
	 * @作者 田应平
	 * @返回值类型 HashMap<String,Object>,含total总条数总记录数;listData分页的数据
	 * @创建时间 2017年1月10日 下午5:56:08
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public HashMap<String,Object> queryForPage(final PageFormData params,final String sqlMapIdListData,final String sqlMapIdTotal){
        final HashMap<String,Object> map = new HashMap<String,Object>(2);
		map.put(ConfigFile.total,sqlSession.selectOne(sqlMapIdTotal,params));
		map.put(ConfigFile.rows,sqlSession.selectList(sqlMapIdListData,params));
		return map;
	}
}