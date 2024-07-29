import { useEffect, useState } from "react";
import i18next from "../../config/i18n/i18next";
import Cookies from "js-cookie";
import { useTranslation } from "react-i18next";

export default function LanguageSwitch() {
  const langCookieName = "locale";
  const ruLang = "ru";
  const enLang = "en";
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
        <label htmlFor="en">{t("control.i18n.en")}</label>
        <input
          type="radio"
          name="lang"
          id="en"
          value="en"
          onChange={() => switchLanguage(enLang)}
          checked={locale === enLang}
        />
        <br />
        <label htmlFor="ru">{t("control.i18n.ru")}</label>
        <input
          type="radio"
          name="lang"
          id="ru"
          value="ru"
          onChange={() => switchLanguage(ruLang)}
          checked={locale === ruLang}
        />
      </form>
    </>
  );
}
