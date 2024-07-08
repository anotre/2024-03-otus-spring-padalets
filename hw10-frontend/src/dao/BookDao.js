import axios from "axios";

export class BookDao {
  constructor() {
    this.fetch = axios.create({
      baseURL: "/api/v1/",
    });
  }
  async getBook(id) {
    return this.fetch.get(`/books/${id}`);
  }
  async getAllBooks() {
    return this.fetch.get("/books");
  }
  async createBook(book) {
    return this.fetch.post("/books", book);
  }
  async updateBook(book) {
    return this.fetch.patch("/books", book);
  }
  async deleteBook(id) {
    return this.fetch.delete(`/books/${id}`);
  }
}
