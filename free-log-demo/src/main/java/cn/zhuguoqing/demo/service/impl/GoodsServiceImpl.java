package cn.zhuguoqing.demo.service.impl;

import cn.zhuguoqing.demo.mapper.GoodsMapper;
import cn.zhuguoqing.demo.pojo.GoodsDomain;
import cn.zhuguoqing.demo.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/28 14:39
 * @Desription: TODO
 * @Version: 1.0
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsDao;

    @Override
    public void addGoods(GoodsDomain goodsDomain) {
        String description = goodsDomain.getDescription();
        if (StringUtils.isEmpty(description)) {
            throw new RuntimeException("商品描述不能为空");
        }
        int insert = goodsDao.insert(goodsDomain);
        if (insert != 1) {
            throw new RuntimeException("插入商品失败");
        }
    }

    @Override
    public void updateGoods(GoodsDomain goodsDomain) {
        goodsDao.updateByPrimaryKey(goodsDomain);
    }

    @Override
    public void deleteGoodsById(String id) {
        goodsDao.deleteByPrimaryKey(id);
    }
}
