package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.ContentService;
/**
 * 对主页的内容进行处理
 * @author 胡园
 *
 */
@Service
public class ContentServiceImpl implements ContentService {
	@Value("${INDEX_CONTENT_REDIS_KEY}")
	private String INDEX_CONTENT_REDIS_KEY;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbContentMapper contentMapper;
	
	/**
	 * 根据内容分类id，取出一类内容记录
	 * 
	 * 实现缓存的添加，缓存的添加不能影响正常逻辑，所以需要对缓存进行try--catch处理
	 * 
	 * 对业务进行了缓存就要实现缓存同步，就是到业务需求进行修改，增加，删除的时候需要进行key的删除
	 */
	@Override
	public List<TbContent> getContentList(Long contentCategoryId) {
		// TODO Auto-generated method stub
		//从缓存中添加内容
		try{
			String result = jedisClient.hget(INDEX_CONTENT_REDIS_KEY, contentCategoryId+"");
			//判断内容是否是空值
			if(!StringUtils.isBlank(result)){
				//取到了内容
				List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
				return resultList;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		// 根据内容分类id查询内容列表
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(contentCategoryId);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		
		//向缓存中添加内容
		try{
			//将从数据库中查出的内容添加到redis
			String cacheString = JsonUtils.objectToJson(list);
			//向缓存中添加
			jedisClient.hset(INDEX_CONTENT_REDIS_KEY, contentCategoryId+"", cacheString);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}

}
