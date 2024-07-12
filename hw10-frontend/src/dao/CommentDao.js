import axios from "axios";

export class CommentDao {
  constructor() {
    this.fetch = axios.create({
      baseURL: "/api/v1/",
    });
  }
  async getAllCommentsByBookId(id) {
    return this.fetch.get(`/comments/book/${id}`);
  }
}
