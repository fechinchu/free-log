package cn.zhuguoqing.operationLog.support.context;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @Author:guoqing.zhu
 * @Date：2021/11/30 18:54
 * @Desription: TODO
 * @Version: 1.0
 */
public class LogRecordContext {

    /**
     * 使用InheritableThreadLocal解决异步方法获取不到数据的问题
     */
    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();

    public static void putVariable(String name, Object value) {
        if (variableMapStack.get() == null) {
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack.size() == 0) {
            variableMapStack.get().push(new HashMap<>());
        }
        variableMapStack.get().peek().put(name, value);
    }

    public static Object getVariable(String key) {
        Map<String, Object> variableMap = variableMapStack.get().peek();
        return variableMap.get(key);
    }

    public static Object removeVariable(String key){
        Map<String, Object> variableMap = variableMapStack.get().peek();
        return variableMap.remove(key);
    }

    public static Map<String, Object> getVariables() {
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        return mapStack.peek();
    }

    public static void clear() {
        if (variableMapStack.get() != null) {
            variableMapStack.get().pop();
        }
    }

    /**
     * 日志使用方不需要使用到这个方法
     * 每进入一个方法初始化一个 span 放入到 stack中，方法执行完后 pop 掉这个span
     */
    public static void putEmptySpan() {
        Stack<Map<String, Object>> mapStack = variableMapStack.get();
        if (mapStack == null) {
            Stack<Map<String, Object>> stack = new Stack<>();
            variableMapStack.set(stack);
        }
        variableMapStack.get().push(Maps.newHashMap());

    }
}
