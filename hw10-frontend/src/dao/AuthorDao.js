import axios from "axios";

export class AuthorDao {
  constructor() {
    this.fetch = axios.create({
      baseURL: "/api/v1/",
    });
  }
  async getAllAuthors() {
    return this.fetch.get("/authors");
  }
}
