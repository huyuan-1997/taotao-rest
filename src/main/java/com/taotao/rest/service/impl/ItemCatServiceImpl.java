package com.taotao.rest.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.pojo.TbItemCatExample.Criteria;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public CatResult getItemCatList() {
		// TODO Auto-generated method stub
		CatResult catResult = new CatResult();
		// 查询分类列表
		catResult.setData(getCatList(0));

		return catResult;
	}

	/**
	 * 拼装成分类列表所需要的数据格式
	 * 
	 * @param i
	 * @return
	 */
	private List<?> getCatList(long parentId) {
		// TODO Auto-generated method stub
		// 创建查询条件
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		// 执行查询,第一次是祖宗节点
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 返回创建返回值list就是list<CatNode>
		List resultList = new ArrayList<>();
		int count = 0;
		// 向list中添加节点
		for (TbItemCat tbItemCat : list) {
			// 判断是父节点,还是叶子节点
			if (tbItemCat.getIsParent()) {
				CatNode catNode = new CatNode();
				if (parentId == 0) {// 根节点
					catNode.setName(
							"<a href='/products/" + tbItemCat.getId() + ".html'>" + tbItemCat.getName() + "</a>");
				} else {
					catNode.setName(tbItemCat.getName());
				}
				catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
				catNode.setItem(getCatList(tbItemCat.getId()));

				resultList.add(catNode);
				count++;
				if (count >= 14) {
					break;
				}
			} else {// 如果是叶子节点
				resultList.add("/products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());
			}
		}
		return resultList;
	}
}
