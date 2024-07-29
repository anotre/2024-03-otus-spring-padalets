package ru.otus.hw.config;


import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.SimpleLocaleContext;
import org.springframework.http.ResponseCookie;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class CookieParamLocaleResolver implements LocaleContextResolver {
    public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = "locale";

    private String languageParameterName;

    public CookieParamLocaleResolver(String languageParameterName) {
        this.languageParameterName = languageParameterName;
    }

    @Override
    public LocaleContext resolveLocaleContext(ServerWebExchange exchange) {
        Locale defaultLocale = getLocaleFromCookie(exchange);
        List<String> referLang = exchange.getRequest().getQueryParams().get(this.languageParameterName);

        if (!CollectionUtils.isEmpty(referLang)) {
            var languageTag = referLang.get(0);
            defaultLocale = Locale.forLanguageTag(languageTag);
            setLocaleToCookie(languageTag, exchange);
        }

        return new SimpleLocaleContext(defaultLocale);
    }

    @Override
    public void setLocaleContext(ServerWebExchange exchange, LocaleContext localeContext) {
        throw new UnsupportedOperationException("Not Supported");
    }

    private void setLocaleToCookie(String languageValue, ServerWebExchange exchange) {
        var cookies = exchange.getRequest().getCookies();
        var localeCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);

        if (localeCookie != null || !languageValue.equals(localeCookie.getValue())) {
            var cookie = ResponseCookie.from(LOCALE_REQUEST_ATTRIBUTE_NAME, languageValue)
                    .maxAge(Duration.ofMinutes(5))
                    .build();
            exchange.getResponse().addCookie(cookie);
        }
    }

    private Locale getLocaleFromCookie(ServerWebExchange exchange) {
        var cookies = exchange.getRequest().getCookies();
        var localeCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);

        if (localeCookie == null) {
            return Locale.getDefault();
        }

        return Locale.forLanguageTag(localeCookie.getValue());
    }
}
