import { useRef, useState } from "react";
import { CommentDao } from "../../dao/CommentDao";
import { useTranslation } from "react-i18next";

export default function CommentsSection({ bookId }) {
  const [comments, setComments] = useState([]);
  const { t } = useTranslation();
  const commentsDaoRef = useRef(new CommentDao());

  useState(() => {
    commentsDaoRef.current.getAllCommentsByBookId(bookId).then((res) => {
      setComments(res.data);
    });
  }, []);

  return (
    <>
      <h3>{t("title.comments")}</h3>
      <ul>
        {comments.map((comment) => (
          <li key={comment.id}>{comment.text}</li>
        ))}
      </ul>
    </>
  );
}
