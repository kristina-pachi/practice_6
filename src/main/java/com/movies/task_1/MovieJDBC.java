package com.movies.task_1;

import java.sql.*;

public class MovieJDBC {

    private static final String URL = "jdbc:h2:mem:moviedb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Подключение успешно!");

            dropAndCreateTable(conn);
            insertMovies(conn);
            updateMovie(conn, 1, "Матрица: Перезагрузка", "Фантастика", 2003);
            deleteMovie(conn, 2);

            System.out.println("\n=== Все фильмы ===");
            printAllMovies(conn);

            System.out.println("\n=== Фильмы после 2000 года ===");
            findByYear(conn, 2000);

            System.out.println("\n=== Фантастика ===");
            findByGenre(conn, "Фантастика");

            System.out.println("\n=== Поиск 'начало' ===");
            findByTitle(conn, "начало");
        }
    }

    static void dropAndCreateTable(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS movies");
            stmt.execute("""
                CREATE TABLE movies (
                    id            INT AUTO_INCREMENT PRIMARY KEY,
                    title         VARCHAR(200) NOT NULL,
                    genre         VARCHAR(100),
                    release_year  INT
                )
            """);
            System.out.println("Таблица movies создана");
        }
    }

    static void insertMovies(Connection conn) throws SQLException {
        String sql = "INSERT INTO movies (title, genre, release_year) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Object[][] movies = {
                    {"Матрица", "Фантастика", 1999},
                    {"Начало", "Фантастика", 2010},
                    {"Лев", "Драма", 2016},
                    {"Джокер", "Триллер", 2019}
            };
            for (Object[] movie : movies) {
                pstmt.setString(1, (String) movie[0]);
                pstmt.setString(2, (String) movie[1]);
                pstmt.setInt(3, (Integer) movie[2]);
                pstmt.executeUpdate();
                System.out.println("Добавлен: " + movie[0]);
            }
        }
    }

    static void updateMovie(Connection conn, int id, String title, String genre, int year)
            throws SQLException {
        String sql = "UPDATE movies SET title=?, genre=?, release_year=? WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setInt(3, year);
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
        }
        System.out.println("Обновлён фильм id=" + id);
    }

    static void deleteMovie(Connection conn, int id) throws SQLException {
        String sql = "DELETE FROM movies WHERE id=?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
        System.out.println("Удалён фильм id=" + id);
    }

    static void printAllMovies(Connection conn) throws SQLException {
        String sql = "SELECT * FROM movies ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.printf("id=%d | %-30s | %-15s | %d%n",
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("release_year"));
            }
        }
    }

    static void findByYear(Connection conn, int minYear) throws SQLException {
        String sql = "SELECT * FROM movies WHERE release_year > ? ORDER BY release_year";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, minYear);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("id=%d | %-30s | %-15s | %d%n",
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getInt("release_year"));
                }
            }
        }
    }

    static void findByGenre(Connection conn, String genre) throws SQLException {
        String sql = "SELECT * FROM movies WHERE LOWER(genre) = LOWER(?) ORDER BY release_year";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, genre);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("id=%d | %-30s | %-15s | %d%n",
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getInt("release_year"));
                }
            }
        }
    }

    static void findByTitle(Connection conn, String titlePart) throws SQLException {
        String sql = "SELECT * FROM movies WHERE LOWER(title) LIKE LOWER(?) ORDER BY id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + titlePart + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    System.out.printf("id=%d | %-30s | %-15s | %d%n",
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("genre"),
                            rs.getInt("release_year"));
                }
            }
        }
    }
}
