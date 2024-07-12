import axios from "axios";

export class GenreDao {
  constructor() {
    this.fetch = axios.create({
      baseURL: "/api/v1/",
    });
  }
  async getAllGenres() {
    return this.fetch.get("/genres");
  }
}
