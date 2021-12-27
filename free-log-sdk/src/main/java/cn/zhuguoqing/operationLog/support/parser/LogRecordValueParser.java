package cn.zhuguoqing.operationLog.support.parser;


import cn.zhuguoqing.operationLog.support.service.IFunctionService;
import com.google.common.base.Strings;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author:guoqing.zhu @Date：2021/12/01 @Desription:解析需要存储的日志里面的SpeEL表达式 @Version: 1.0
 */
@Component
public class LogRecordValueParser implements BeanFactoryAware {

    protected BeanFactory beanFactory;

    private final LogRecordExpressionEvaluator expressionEvaluator = new LogRecordExpressionEvaluator();

    @Autowired
    private IFunctionService functionService;

    private static final Pattern pattern = Pattern.compile("\\{\\s*(\\w*)\\s*\\{(.*?)}}");

    /**
     * 主要用于获取参数的数据.在procceed执行之前执行
     * @param templates 需要执行的spEL表达式
     * @param targetClass 目标类
     * @param method 目标方法
     * @param args 参数
     * @return
     */
    public Map<String,String> processBeforeExecuteTemplate(Collection<String> templates,  Class<?> targetClass, Method method, Object[] args){
        Map<String, String> expressionValues = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, null, null, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    //String functionReturnValue = getFunctionReturnValue(null, value, matcher.group(1));
                    matcher.appendReplacement(parsedStr, Strings.nullToEmpty(value));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, parsedStr.toString());
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }
        }
        return expressionValues;
    }

    /**
     * 执行切面procceed之前
     * @param templates
     * @param targetClass
     * @param method
     * @param args
     * @return
     */
    public Map<String, String> processBeforeExecuteFunctionTemplate(Collection<String> templates, Class<?> targetClass, Method method, Object[] args) {
        Map<String, String> functionNameAndReturnValueMap = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, null, null, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    if (expression.contains("#_ret") || expression.contains("#_errorMsg")) {
                        continue;
                    }
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String functionName = matcher.group(1);
                    if (functionService.beforeFunction(functionName)) {
                        String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                        String functionReturnValue = getFunctionReturnValue(null, value, functionName);
                        functionNameAndReturnValueMap.put(functionName, functionReturnValue);
                    }
                }
            }
        }
        return functionNameAndReturnValueMap;
    }

    /**
     * 执行模板,将模板替换为实际的值
     * @param templates 需要执行的模板
     * @param ret procceed的结果
     * @param targetClass 目标类
     * @param method 目标方法
     * @param args 参数
     * @param errorMsg 错误信息
     * @param beforeFunctionNameAndReturnMap  procceed执行前的自定义方法map
     * @return
     */
    public Map<String, String> processTemplate(Collection<String> templates, Object ret, Class<?> targetClass,
                                               Method method, Object[] args, String errorMsg, Map<String, String> beforeFunctionNameAndReturnMap) {
        Map<String, String> expressionValues = new HashMap<>();
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(method, args, targetClass, ret, errorMsg, beanFactory);

        for (String expressionTemplate : templates) {
            if (expressionTemplate.contains("{")) {
                Matcher matcher = pattern.matcher(expressionTemplate);
                StringBuffer parsedStr = new StringBuffer();
                while (matcher.find()) {
                    String expression = matcher.group(2);
                    AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClass);
                    String value = expressionEvaluator.parseExpression(expression, annotatedElementKey, evaluationContext);
                    String functionReturnValue = getFunctionReturnValue(beforeFunctionNameAndReturnMap, value, matcher.group(1));
                    matcher.appendReplacement(parsedStr, Strings.nullToEmpty(functionReturnValue));
                }
                matcher.appendTail(parsedStr);
                expressionValues.put(expressionTemplate, parsedStr.toString());
            } else {
                expressionValues.put(expressionTemplate, expressionTemplate);
            }
        }
        return expressionValues;
    }

    /**
     * 如果procceed之前已经解析过,那么直接从传递过来的map中获取
     * @param beforeFunctionNameAndReturnMap
     * @param value
     * @param functionName
     * @return
     */
    private String getFunctionReturnValue(Map<String, String> beforeFunctionNameAndReturnMap, String value, String functionName) {
        String functionReturnValue = "";
        if (beforeFunctionNameAndReturnMap != null) {
            functionReturnValue = beforeFunctionNameAndReturnMap.get(functionName);
        }
        if (StringUtils.isEmpty(functionReturnValue)) {
            functionReturnValue = functionService.apply(functionName, value);
        }
        return functionReturnValue;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
