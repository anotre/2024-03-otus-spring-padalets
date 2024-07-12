import { useEffect, useState } from "react";
import i18next from "../../config/i18n/i18next";
import Cookies from "js-cookie";
import { useTranslation } from "react-i18next";

export default function LanguageSwitch() {
  const langCookieName = "locale";
  const ruLang = "ru_RU";
  const enLang = "en_US";
  const [locale, setLocaleState] = useState(enLang);
  const { t } = useTranslation();

  const switchLanguage = (locale) => {
    i18next.changeLanguage(locale);
    setLocaleState(locale);
    Cookies.set(langCookieName, locale, { expires: 365 });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
  };

  useEffect(() => {
    const currentLocale = Cookies.get(langCookieName);
    if (currentLocale) {
      switchLanguage(currentLocale);
    } else {
      switchLanguage(enLang);
    }
  }, []);

  return (
    <>
      <p>{t("title.i18n")}:</p>
      <form action="" onSubmit={handleSubmit}>
        <label htmlFor="en_US">{t("control.i18n.en_US")}</label>
        <input
          type="radio"
          name="lang"
          id="en_US"
          value="en_US"
          onChange={() => switchLanguage(enLang)}
          checked={locale === enLang}
        />
        <br />
        <label htmlFor="ru_RU">{t("control.i18n.ru_RU")}</label>
        <input
          type="radio"
          name="lang"
          id="ru_RU"
          value="ru_RU"
          onChange={() => switchLanguage(ruLang)}
          checked={locale === ruLang}
        />
      </form>
    </>
  );
}
