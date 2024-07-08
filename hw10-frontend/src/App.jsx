import { BrowserRouter, Link, Route, Routes } from "react-router-dom";
import Main from "./components/Main";
import Book from "./components/Book";
import CreateBookForm from "./components/BookForm/create";
import EditBookForm from "./components/BookForm/edit";
import NotFound from "./components/NotFound";
import BooksList from "./components/BooksList";
import LanguageSwitch from "./components/LanguageSwitch";
import { useTranslation } from "react-i18next";

export default function App() {
  const { t } = useTranslation();
  return (
    <BrowserRouter>
      <Routes>
        <Route exact path="/" element={<Main />} />
        <Route exact path="/books" element={<BooksList />} />
        <Route exact path="/books/:id" element={<Book />} />
        <Route exact path="/books/create" element={<CreateBookForm />} />
        <Route exact path="/books/edit/:id" element={<EditBookForm />} />
        <Route exact path="/books/not-found" element={<NotFound />} />
        <Route exact path="*" element={<NotFound />} />
      </Routes>
      <div>
        <Link to="/">{t("control.link.main")}</Link>
      </div>
      <LanguageSwitch />
    </BrowserRouter>
  );
}
