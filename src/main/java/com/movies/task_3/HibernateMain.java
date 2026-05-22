package com.movies.task_3;


import jakarta.persistence.criteria.*;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class HibernateMain {

    private static SessionFactory buildSessionFactory() {
        return new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }

    // Сохранение нескольких фильмов
    static void saveMovies(SessionFactory sf) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();

            session.persist(new Movie("Матрица", "Фантастика", 1999));
            session.persist(new Movie("Начало", "Фантастика", 2010));
            session.persist(new Movie("Тёмный рыцарь", "Боевик", 2008));
            session.persist(new Movie("Паразиты", "Триллер", 2019));
            session.persist(new Movie("Интерстеллар", "Фантастика", 2014));

            tx.commit();
            System.out.println("Фильмы сохранены");
        }
    }

    // Обновление через HQL
    static void updateMovieByHQL(SessionFactory sf, String title, int newYear) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();

            int updated = session.createMutationQuery(
                            "UPDATE Movie SET year = :year WHERE title = :title"
                    )
                    .setParameter("year", newYear)
                    .setParameter("title", title)
                    .executeUpdate();

            tx.commit();
            System.out.println("Обновлено записей: " + updated);
        }
    }

    // Удаление по id
    static void deleteById(SessionFactory sf, int id) {
        try (Session session = sf.openSession()) {
            Transaction tx = session.beginTransaction();

            Movie movie = session.get(Movie.class, id);
            if (movie != null) {
                session.remove(movie);
                System.out.println("Удалён: " + movie.getTitle());
            }

            tx.commit();
        }
    }

    // Поиск через HQL
    static List<Movie> findByGenreHQL(SessionFactory sf, String genre) {
        try (Session session = sf.openSession()) {
            return session.createQuery("FROM Movie WHERE genre = :genre ORDER BY year", Movie.class)
                    .setParameter("genre", genre)
                    .list();
        }
    }

    // Поиск через Criteria API
    static List<Movie> findByYearRange(SessionFactory sf, int fromYear, int toYear) {
        try (Session session = sf.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> root = cq.from(Movie.class);

            cq.select(root)
                    .where(cb.between(root.get("year"), fromYear, toYear))
                    .orderBy(cb.asc(root.get("year")));

            return session.createQuery(cq).list();
        }
    }

    // Поиск по названию через Criteria API
    static List<Movie> findByTitleLike(SessionFactory sf, String titlePart) {
        try (Session session = sf.openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Movie> cq = cb.createQuery(Movie.class);
            Root<Movie> root = cq.from(Movie.class);

            cq.select(root).where(cb.like(cb.lower(root.get("title")), "%" + titlePart.toLowerCase() + "%"));

            return session.createQuery(cq).list();
        }
    }

    public static void main(String[] args) {
        try (SessionFactory sf = buildSessionFactory()) {
            // Сохранение
            saveMovies(sf);

            System.out.println("\n=== Обновление через HQL ===");
            updateMovieByHQL(sf, "Матрица", 1998);

            System.out.println("\n=== Удаление по id ===");
            deleteById(sf, 3);

            System.out.println("\n=== Поиск фантастики (HQL) ===");
            findByGenreHQL(sf, "Фантастика").forEach(System.out::println);

            System.out.println("\n=== Фильмы 2000-2015 (Criteria API) ===");
            findByYearRange(sf, 2000, 2015).forEach(System.out::println);

            System.out.println("\n=== Поиск 'тёмн' (Criteria API) ===");
            findByTitleLike(sf, "тёмн").forEach(System.out::println);
        }
    }
}

