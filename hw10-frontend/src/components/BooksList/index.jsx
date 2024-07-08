import { useEffect, useRef, useState } from "react";
import { BookDao } from "../../dao/BookDao";
import { Link } from "react-router-dom";
import { useTranslation } from "react-i18next";

export default function BooksList() {
  const bookDaoRef = useRef(new BookDao());
  const [books, setBooks] = useState([]);
  const { t } = useTranslation();

  useEffect(() => {
    bookDaoRef.current.getAllBooks().then((res) => setBooks(res.data));
  }, []);

  return (
    <>
      <h2>{t("title.bookListPage")}</h2>
      <ul>
        {books.map((book) => (
          <li key={book.id}>
            <Link to={`/books/${book.id}`}>{book.title}</Link>
          </li>
        ))}
      </ul>
      <Link to="/books/create">{t("control.link.create")}</Link>
    </>
  );
}
