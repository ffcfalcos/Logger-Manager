package com.github.ffcfalcos.logger.trace;

import com.github.ffcfalcos.logger.LogContent;
import com.github.ffcfalcos.logger.LogType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.List;

/**
 * @author Thomas Beauchataud
 * @since 24.02.2020
 * Handle Traceable annotation
 * Intercept methods with the Traceable and compare them to the rule list
 * If the methods is in the rule list, it will be logged as the rule wants it
 */
@Aspect
@SuppressWarnings("unused")
public class TraceableAnnotationHandler extends AbstractTraceAnnotationHandler {

    private static List<Rule> rules;
    private static AbstractRulesLoader rulesLoader;

    /**
     * TraceableAnnotationHandler Constructor
     */
    public TraceableAnnotationHandler() {
        AbstractRulesLoader rulesLoader = new FileWatcherRulesLoader(new CsvRulesStorageHandler());
        rules = rulesLoader.getRulesStorageHandler().getRules();
        rulesLoader.setRules(rules);
        new Thread(rulesLoader).start();
    }

    @Pointcut("@annotation(com.github.ffcfalcos.logger.trace.Traceable) && execution(* *(..))")
    public void traceablePointcut() { }

    /**
     * Handle the invocation of the ProceedingJoinPoint of log with the associated Rule if it exists
     *
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return Object
     * @throws Throwable if there is an exception during the method invocation
     */
    @Around("traceablePointcut()")
    public Object handle(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Rule rule = getRule(proceedingJoinPoint);
        if (rule != null) {
            if (rule.getEntry().equals(Entry.BEFORE)) {
                super.handleTraceBefore(proceedingJoinPoint, TraceAnnotationFactory.createTraceBefore(rule));
                return proceedingJoinPoint.proceed();
            }
            if (rule.getEntry().equals(Entry.AFTER)) {
                Object response = proceedingJoinPoint.proceed();
                super.handleTraceAfter(proceedingJoinPoint, TraceAnnotationFactory.createTraceAfter(rule));
                return response;
            }
            if (rule.getEntry().equals(Entry.AROUND)) {
                return super.handleTraceAround(proceedingJoinPoint, TraceAnnotationFactory.createTraceAround(rule));
            }
            if (rule.getEntry().equals(Entry.AFTER_THROWING)) {
                LogContent logContent = new LogContent(LogType.TRACE_AFTER_THROWING);
                try {
                    return proceedingJoinPoint.proceed();
                } catch (Exception e) {
                    super.handleTraceAfterThrowing(proceedingJoinPoint, e, TraceAnnotationFactory.createTraceAfterThrowing(rule));
                    throw e;
                }
            }
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * Return a the rule associated to the JointPoint or null if there is no rule corresponding
     *
     * @param proceedingJoinPoint ProceedingJoinPoint
     * @return Rule
     */
    private Rule getRule(ProceedingJoinPoint proceedingJoinPoint) {
        for (Rule rule : rules) {
            if (rule.validRule(proceedingJoinPoint)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * Return the RulesLoader
     *
     * @return AbstractRulesLoader
     */
    public static AbstractRulesLoader getRulesLoader() {
        return rulesLoader;
    }

    /**
     * Set and initialize a new AbstractRulesLoader
     *
     * @param newRulesLoader AbstractRulesLoader
     */
    public static void setRulesLoader(AbstractRulesLoader newRulesLoader) {
        rulesLoader = newRulesLoader;
        rules = rulesLoader.getRulesStorageHandler().getRules();
        rulesLoader.setRules(rules);
        new Thread(rulesLoader).start();
    }

}
