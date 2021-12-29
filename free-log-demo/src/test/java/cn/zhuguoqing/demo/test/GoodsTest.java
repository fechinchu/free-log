package cn.zhuguoqing.demo.test;

import cn.zhuguoqing.demo.FreeLogDemoApplication;
import cn.zhuguoqing.demo.controller.GoodsController;
import cn.zhuguoqing.demo.pojo.GoodsCommonDomain;
import cn.zhuguoqing.demo.pojo.GoodsDomain;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>2021/12/28 16:37</p>
 *
 * @author guoqing.zhu
 * <P>description:</p>
 */
@SpringBootTest(classes = FreeLogDemoApplication.class)
@RunWith(SpringRunner.class)
public class GoodsTest {

    @Autowired
    private GoodsController goodsController;

    @Test
    public void testAddGoods(){
        GoodsDomain goodsDomain = new GoodsDomain();
        goodsDomain.setName("商品1");
        //goodsDomain.setDescription("我是一个商品");
        goodsController.addGoods(goodsDomain);
    }

    @Test
    public void testUpdateGoods(){
        GoodsDomain goodsDomain = new GoodsDomain();
        goodsDomain.setId("1");
        goodsDomain.setName("商pi");
        goodsDomain.setDescription("我di个商品");
        goodsController.updateGoods(goodsDomain);
    }

    @Test
    public void testDeleteGoods(){
        goodsController.deleteGoodsById("9");
    }

    @Test
    public void testOperation(){
        goodsController.commonOperation("1");
    }

    @Test
    public void testOperation02(){
        GoodsCommonDomain goodsCommonDomain = new GoodsCommonDomain();
        goodsCommonDomain.setShelve(1);
        goodsCommonDomain.setName("我的小商品");
        goodsController.commonOperation02(goodsCommonDomain);
    }

    @Test
    public void ifSuccess(){
        goodsController.ifSuccess();
    }

}
