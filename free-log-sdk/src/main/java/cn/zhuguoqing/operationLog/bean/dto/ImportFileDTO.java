package cn.zhuguoqing.operationLog.bean.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @Author:guoqing.zhu
 * @Date：2021/12/6 10:07
 * @Desription: TODO
 * @Version: 1.0
 */
@Data
@ToString
public class ImportFileDTO {

    /**
     * 文件id
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * inputStream不能用,只能用这玩意,会报异常
     */
    private byte[] bytes;
}
