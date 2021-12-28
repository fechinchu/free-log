package cn.zhuguoqing.demo.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/28 14:44
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
@Table(name="goods_spu")
public class GoodsDomain {

    /**
     * spuId
     */
    @Id
    @KeySql(useGeneratedKeys = true)
    private String id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;
}
