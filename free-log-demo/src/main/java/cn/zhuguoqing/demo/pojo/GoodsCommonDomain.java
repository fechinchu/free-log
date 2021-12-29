package cn.zhuguoqing.demo.pojo;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;

/**
 * 2021/12/29 19:08
 *
 * @author guoqing.zhu
 *     <p>description:
 */
@Data
public class GoodsCommonDomain {
  /** spuId */
  private String id;

  /** 商品名称 */
  private String name;

  /** 商品描述 */
  private String description;

  /** 1.上架;2.下架 3.删除*/
  private Integer shelve;
}
