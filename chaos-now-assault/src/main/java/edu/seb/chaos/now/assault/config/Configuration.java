package edu.seb.chaos.now.assault.config;

import edu.seb.chaos.now.assault.advice.Advice;
import edu.seb.chaos.now.assault.advice.KillableAdvice;
import edu.seb.chaos.now.assault.assaults.ExceptionAssault;
import edu.seb.chaos.now.assault.assaults.KillAssault;
import edu.seb.chaos.now.assault.components.ExceptionAssaultRequestScope;
import edu.seb.chaos.now.assault.components.annotation.Killable;
import edu.seb.chaos.now.assault.components.annotation.Targetable;
import edu.seb.chaos.now.assault.filters.ClassFilter;
import edu.seb.chaos.now.assault.filters.SpringHookMethodsFilter;
import edu.seb.chaos.now.assault.rest.AssaultController;
import org.springframework.aop.support.ClassFilters;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties({Properties.class, AssaultProperties.class})
public class Configuration {

    private final Properties properties;
    private final AssaultProperties assaultProperties;

    public Configuration(Properties properties, AssaultProperties assaultProperties) {
        this.properties = properties;
        this.assaultProperties = assaultProperties;
    }

    @Bean
    public Settings settings() {
        return new Settings(properties, assaultProperties);
    }

    @Bean
    public ExceptionAssaultRequestScope exceptionAssaultRequestScope() {
        return new ExceptionAssaultRequestScope(settings());
    }

    @Bean
    public ExceptionAssault exceptionAssault() {
        return new ExceptionAssault(assaultProperties.getException());
    }

    @Bean
    public KillAssault killAssault() {
        return new KillAssault();
    }

    @Bean
    public ClassFilter baseClassFilter() {
        return new ClassFilter();
    }

    @Bean
    public DefaultPointcutAdvisor targetablePointcutAdvisor(ClassFilter classFilter, ExceptionAssaultRequestScope requestScope) {
        return new DefaultPointcutAdvisor(
                new ComposablePointcut(ClassFilters.intersection(classFilter, new AnnotationClassFilter(Targetable.class)),
                        SpringHookMethodsFilter.INSTANCE), new Advice(requestScope));
    }

    @Bean
    public DefaultPointcutAdvisor killablePointcutAdvisor(ClassFilter classFilter) {
        return new DefaultPointcutAdvisor(
                new ComposablePointcut(ClassFilters.intersection(classFilter, new AnnotationClassFilter(Killable.class)),
                        SpringHookMethodsFilter.INSTANCE), new KillableAdvice(killAssault()));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnAvailableEndpoint
    public AssaultController assaultController() {
        return new AssaultController(settings());
    }
}
