import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

export default function Main() {
  const { t } = useTranslation();

  return (
    <>
      <h1>{t("title.startPage")}</h1>
      <Link to="/books">{t("control.link.bookList")}</Link>
    </>
  );
}
