import { useRef, useState, useEffect } from "react";
import { GenreDao } from "../../../dao/GenreDao";
import { AuthorDao } from "../../../dao/AuthorDao";
import { BookDao } from "../../../dao/BookDao";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useTranslation } from "react-i18next";

export default function EditBookForm() {
  const genreDaoRef = useRef(new GenreDao());
  const authorDaoRef = useRef(new AuthorDao());
  const bookDaoRef = useRef(new BookDao());
  const { id } = useParams();

  const [genres, setGenres] = useState([]);
  const [authors, setAuthors] = useState([]);
  const initialFormState = { title: "", genre: { id: 0 }, author: { id: 0 } };
  const [book, setBook] = useState(initialFormState);
  const [formState, setFormState] = useState(initialFormState);
  const navigate = useNavigate();
  const [errors, setErrors] = useState([]);
  const { t } = useTranslation();

  const handleSubmit = (event) => {
    event.preventDefault();
    bookDaoRef.current
      .updateBook(formState)
      .then((res) => {
        errors.length = 0;
        alert(t("form.notification.edited"));
        navigate("/books");
      })
      .catch((error) => {
        setErrors(Object.entries(error.response.data));
      });
  };

  const handleReset = (event) => {
    event.preventDefault();
    setFormState(book);
  };

  const handleChangeTitle = (evt) =>
    setFormState((prev) => ({ ...prev, title: evt.target.value }));
  const handleChangeGenre = (evt) =>
    setFormState((prev) => ({ ...prev, genre: { id: evt.target.value } }));
  const handleChangeAuthor = (evt) =>
    setFormState((prev) => ({ ...prev, author: { id: evt.target.value } }));

  useEffect(() => {
    bookDaoRef.current
      .getBook(id)
      .then((res) => {
        setBook(res.data);
        setFormState(res.data);
      })
      .catch((err) => navigate("/books/not-found"));
    genreDaoRef.current.getAllGenres().then((res) => {
      setGenres(res.data);
    });

    authorDaoRef.current.getAllAuthors().then((res) => {
      setAuthors(res.data);
    });
  }, []);

  return (
    <>
      <h2>{t("title.editBookPage")}</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="title"
          onChange={handleChangeTitle}
          value={formState.title}
          placeholder={t("form.book.placeholder.title")}
        />
        <select
          name="authorId"
          value={formState.author.id}
          onChange={handleChangeAuthor}
        >
          <option value="0">{t("form.book.placeholder.author")}</option>
          {authors.map((author) => (
            <option key={author.id} value={author.id}>
              {author.fullName}
            </option>
          ))}
        </select>

        <select
          name="genre"
          value={formState.genre.id}
          onChange={handleChangeGenre}
        >
          <option value="0">{t("form.book.placeholder.genre")}</option>
          {genres.map((genre) => (
            <option key={genre.id} value={genre.id}>
              {genre.name}
            </option>
          ))}
        </select>

        <input type="submit" value={t("control.form.save")} />
        <input
          type="reset"
          onClick={handleReset}
          value={t("control.form.reset")}
        />
      </form>
      {errors.length > 0 && (
        <>
          <h3>{t("form.notification.error")}</h3>
          <ul>
            {errors.map((entry) => (
              <li key={entry[0]}>{entry[1]}</li>
            ))}
          </ul>
        </>
      )}
      <Link to="/books">{t("control.link.bookList")}</Link>
    </>
  );
}
