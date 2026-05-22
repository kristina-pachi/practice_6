package com.movies.task_2;

import java.sql.*;
import java.util.List;

public class DAOTest {

    public static void main(String[] args) throws Exception {

        Connection conn = DriverManager.getConnection(
                "jdbc:h2:mem:movietest;DB_CLOSE_DELAY=-1", "sa", "");

        MovieDAO dao = new MovieDAOImpl(conn);

        dao.dropTable();
        dao.createTable();

        dao.insert(new Movie("Матрица", "Фантастика", 1999));
        dao.insert(new Movie("Начало", "Фантастика", 2010));
        dao.insert(new Movie("Лев", "Драма", 2016));
        dao.insert(new Movie("Джокер", "Триллер", 2019));

        dao.updateTitle(1, "Матрица: Перезагрузка");
        dao.delete(2);

        System.out.println("\n=== Все фильмы ===");
        dao.findAll().forEach(System.out::println);

        System.out.println("\n=== По id ===");
        System.out.println(dao.findById(1));

        System.out.println("\n=== По жанру 'Фантастика' ===");
        dao.findByGenre("Фантастика").forEach(System.out::println);

        System.out.println("\n=== По году 2019 ===");
        dao.findByYear(2019).forEach(System.out::println);

        System.out.println("\n=== По части названия 'мат' ===");
        dao.findByTitle("мат").forEach(System.out::println);
    }
}
