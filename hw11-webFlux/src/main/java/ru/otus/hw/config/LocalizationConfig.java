package ru.otus.hw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.server.i18n.LocaleContextResolver;

@Configuration
public class LocalizationConfig extends DelegatingWebFluxConfiguration {
    @Override
    protected LocaleContextResolver createLocaleContextResolver() {
        return new CookieParamLocaleResolver("locale");
    }
}
