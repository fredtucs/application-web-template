package org.wifry.fooddelivery.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.wifry.fooddelivery.base.BaseRepositoryFactoryBean;

/**
 * Created by wtuco on 16/03/2016.
 * <p>
 * Config Spring
 */
@Configuration
@EnableJpaRepositories(basePackages = "org.wifry.fooddelivery.repository", repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
@ComponentScan(basePackages = {"org.wifry.fooddelivery.services", "org.wifry.fooddelivery.repository",
        "org.wifry.fooddelivery.security", "org.wifry.fooddelivery.web", "org.wifry.fooddelivery.audit", "org.wifry.fooddelivery.util.converts"})
@ImportResource(value = {"classpath:applicationContext-database.xml", "classpath*:applicationContext-servlet.xml"})
@Import({SpringSecurityConfig.class})
public class SpringConfig {
}
