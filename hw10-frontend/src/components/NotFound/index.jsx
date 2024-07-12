import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const NotFound = () => {
  const { t } = useTranslation();
  return (
    <>
      <h1>{t("title.notFoundPage")}</h1>
      <Link to="/books">{t("control.link.bookList")}</Link>
    </>
  );
};

export default NotFound;
