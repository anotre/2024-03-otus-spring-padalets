package ru.otus.hw.config.security;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.expression.DefaultHttpSecurityExpressionHandler;

import javax.sql.DataSource;

@Configuration
@EnableCaching
@EnableMethodSecurity
public class AclConfiguration {
    @Bean
    public MutableAclService mutableAclService(CacheManager cacheManager, DataSource dataSource) {
        var permissionGrantingStrategy = new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
        var aclAuthorizationStrategy = new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
        var aclCache = new SpringCacheBasedAclCache(
                cacheManager.getCache("security/acl"),
                permissionGrantingStrategy,
                aclAuthorizationStrategy);
        var lookupStrategy = new BasicLookupStrategy(
                dataSource,
                aclCache,
                aclAuthorizationStrategy,
                permissionGrantingStrategy);
        var mutableAclService = new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);

        return mutableAclService;
    }

    @Bean
    public AclPermissionEvaluator aclPermissionEvaluator(MutableAclService mutableAclService) {
        return new AclPermissionEvaluator(mutableAclService);
    }

    @Bean
    public DefaultHttpSecurityExpressionHandler httpSecurityExpressionHandler(
            AclPermissionEvaluator aclPermissionEvaluator,
            ApplicationContext applicationContext) {
        var httpSecurityExpressionHandler = new DefaultHttpSecurityExpressionHandler();
        httpSecurityExpressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        httpSecurityExpressionHandler.setApplicationContext(applicationContext);
        return httpSecurityExpressionHandler;
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(
            AclPermissionEvaluator aclPermissionEvaluator,
            ApplicationContext applicationContext) {
        var methodSecurityExpressionHandler = new DefaultMethodSecurityExpressionHandler();
        methodSecurityExpressionHandler.setPermissionEvaluator(aclPermissionEvaluator);
        methodSecurityExpressionHandler.setApplicationContext(applicationContext);
        return methodSecurityExpressionHandler;
    }
}
