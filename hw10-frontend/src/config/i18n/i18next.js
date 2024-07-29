import i18next from "i18next";
import { initReactI18next } from "react-i18next";

i18next.use(initReactI18next).init({
  lng: "ru",
  resources: {
    ru: {
      translation: {
        control: {
          form: {
            save: "Сохранить",
            reset: "Сбросить",
          },
          link: {
            delete: "Удалить",
            edit: "Редактировать",
            create: "Создать",
            bookList: "Список книг",
            main: "Главная",
          },
          i18n: {
            en: "Язык - EN",
            ru: "Язык - RU",
          },
        },
        form: {
          book: {
            placeholder: {
              title: "Заголовок",
              author: "Выберите автора",
              genre: "Выберите жанр",
            },
          },
          notification: {
            edited: "Книга изменена",
            created: "Книга создана",
            error: "Ошибка",
            deleted: "Книга удалена",
          },
        },
        title: {
          bookListPage: "Список книг",
          bookPage: "Страница книги",
          editBookPage: "Редактирование книги",
          createBookPage: "Создание книги",
          notFoundPage: "Не найдено",
          startPage: "Главная",
          comments: "Комментарии",
          i18n: "Выберите язык",
        },
      },
    },
    en: {
      translation: {
        control: {
          form: {
            save: "Save",
            reset: "Reset",
          },
          link: {
            delete: "Delete",
            edit: "Edit",
            create: "Create",
            bookList: "Book list",
            main: "Start page",
          },
          i18n: {
            en: "Language - EN",
            ru: "Language - RU",
          },
        },
        form: {
          book: {
            placeholder: {
              title: "Title",
              author: "Choose author",
              genre: "Choose genre",
            },
          },
          notification: {
            edited: "Book edited",
            created: "Book created",
            error: "Error",
            deleted: "Book deleted",
          },
        },
        title: {
          bookListPage: "Book list",
          bookPage: "Book page",
          editBookPage: "Edit book",
          createBookPage: "Create book",
          notFoundPage: "Not found",
          startPage: "Start page",
          comments: "Comments",
          i18n: "Select language",
        },
      },
    },
  },
  debug: true,
  fallbackLng: "en",
  interpolation: {
    escapeValue: false,
  },
});

export default i18next;
