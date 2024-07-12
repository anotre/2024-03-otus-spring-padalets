import { useEffect, useRef, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { BookDao } from "../../dao/BookDao";
import CommentsSection from "../CommentsSection";
import { useTranslation } from "react-i18next";

export default function Book() {
  const bookDaoRef = useRef(new BookDao());
  const { id } = useParams();
  const navigate = useNavigate();
  const initialBook = {
    title: "",
    author: { fullName: "" },
    genre: { name: "" },
  };
  const [book, setBook] = useState(initialBook);
  const { t } = useTranslation();

  const handleDelete = (event) => {
    event.preventDefault();
    bookDaoRef.current.deleteBook(id).then((res) => {
      alert(t("form.notification.deleted"));
      navigate("/books");
    });
  };

  useEffect(() => {
    bookDaoRef.current
      .getBook(id)
      .then((res) => setBook(res.data))
      .catch((err) => navigate("/books/not-found"));
  }, []);

  return (
    <>
      <h1>{book.title}</h1>
      <address rel="author">{book.author.fullName}</address>
      <span>{book.genre?.name}</span>
      <br />
      <br />
      <CommentsSection bookId={id} />
      <br />
      <Link to={`/books/edit/${book.id}`}>{t("control.link.edit")}</Link>
      <br />
      <a href="" onClick={handleDelete}>
        {t("control.link.delete")}
      </a>
      <br />
      <Link to="/books">{t("control.link.bookList")}</Link>
    </>
  );
}
