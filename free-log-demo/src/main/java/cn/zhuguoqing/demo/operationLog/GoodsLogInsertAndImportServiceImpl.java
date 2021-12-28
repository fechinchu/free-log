package cn.zhuguoqing.demo.operationLog;

import cn.zhuguoqing.operationLog.service.impl.AbstractLogInsertAndImportService;

import org.springframework.stereotype.Service;

/**
 * 2021/12/28 15:43
 *
 * @author guoqing.zhu
 *     <p>description:告诉free-log日志记录以及日志详情该记录在什么地方
 *     <p>案例中选择MySQL,直接继承抽象类,并使用默认,不做修改
 */
@Service
public class GoodsLogInsertAndImportServiceImpl extends AbstractLogInsertAndImportService {}
