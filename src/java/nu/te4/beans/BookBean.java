/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import com.mysql.jdbc.Connection;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import nu.te4.support.ConnectionFactory;

/**
 *
 * @author daca97002
 */
@Stateless
public class BookBean {

    public boolean addBook(String body) {
        System.out.println("Gets to bean");
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject data = jsonReader.readObject();
        String name = data.getString("name");
        String price = data.getString("price");
        if (price.length() <= 9) {
            System.out.println("price leangth less than 9");
            int cat = data.getInt("category");
            System.err.println("categori " + cat);
            String authors = data.getString("authors");
            System.out.println(authors + "\n");

            ArrayList<Integer> authArr = new ArrayList<>();
            while (authors.contains(" ")) {
                System.out.println("from arraybuilder " + authors);

                int auth = Integer.parseInt(authors.substring(0, authors.indexOf(" ")));
                System.out.println(auth);
                authors = authors.substring(authors.indexOf(" ") + 1);
                authArr.add(auth);
            }

            try {
                Connection conn = ConnectionFactory.make();
                String query = "INSERT INTO book VALUES(NULL,?,?,?);";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, price);
                stmt.setInt(3, cat);
                stmt.execute();

                stmt.close();
                query = "SELECT LAST_INSERT_ID() AS id;";
                stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                int bookId = rs.getInt("id");
                System.out.println("BookID " + bookId);
                stmt.close();

                query = "INSERT INTO `author_book` VALUES(NULL,?,?);";
                for (int auth : authArr) {
                    System.out.println("Last for");
                    stmt = conn.prepareStatement(query);
                    stmt.setInt(1, auth);
                    stmt.setInt(2, bookId);
                    stmt.execute();
                    stmt.close();
                }
                conn.close();
                return true;
            } catch (Exception ex) {
                System.out.println("Error: " + ex);
            }
        }

        return false;
    }

    public JsonArray getBooks() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        try {
            Connection conn = ConnectionFactory.make();
            String query = "SELECT * FROM book_cat";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("id");
                String bookName = rs.getString("book_name");
                String bookPrice = rs.getString("price");
                String bookCat = rs.getString("category_name");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("id", bookId)
                        .add("name", bookName)
                        .add("price", bookPrice)
                        .add("cat", bookCat).build()
                );

            }
            return jsonArrayBuilder.build();

        } catch (Exception e) {
            System.out.println("error " + e);
        }
        return null;
    }

    public boolean deleteBook(int id) {
        try {
            Connection conn = ConnectionFactory.make();
            String query = "DELETE FROM book WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.execute();
        } catch (Exception e) {
        }
        return false;
    }

    public boolean updateBook(String body) {
        JsonReader jsonReader = Json.createReader(new StringReader(body));
        JsonObject data = jsonReader.readObject();
        int id = data.getInt("id");
        String name = data.getString("name");
        String price = data.getString("price");
        if (price.length() <= 9) {
            System.out.println("price leangth less than 9");
            int cat = data.getInt("category");
            System.out.println("categori " + cat);
            String authors = data.getString("authors");
            System.out.println(authors + "\n");

            ArrayList<Integer> authArr = new ArrayList<>();
            while (authors.contains(" ")) {
                System.out.println("from arraybuilder " + authors);

                int auth = Integer.parseInt(authors.substring(0, authors.indexOf(" ")));
                System.out.println(auth);
                authors = authors.substring(authors.indexOf(" ") + 1);
                authArr.add(auth);
            }
            try {
                Connection conn = ConnectionFactory.make();
                String query = "UPDATE `book` SET `name` = ?, `price` = ?, `category` = ? WHERE `book`.`id` = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, name);
                stmt.setString(2, price);
                stmt.setInt(3, cat);
                stmt.setInt(4, id);
                stmt.execute();
                stmt.close();
                
                query = "DELETE FROM author_book WHERE book_id = ?";
                stmt = conn.prepareStatement(query);
                stmt.setInt(1, id);
                stmt.execute();
                stmt.close();
                
                query = "INSERT INTO `author_book` VALUES(NULL,?,?);";
                for (int auth : authArr) {
                    System.out.println("Last for");
                    stmt = conn.prepareStatement(query);
                    stmt.setInt(1, auth);
                    stmt.setInt(2, id);
                    stmt.execute();
                    stmt.close();
                }
                conn.close();
                return true;
            } catch (Exception e) {
                System.err.println("errorrr " + e);
            }

        }
        return false;
    }

    public JsonArray getAuthors() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        try {
            Connection conn = ConnectionFactory.make();
            String query = "SELECT * FROM `magic_authors` ORDER BY author_name";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String authorName = rs.getString("author_name");
                String bookName = rs.getString("book_name");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("author_name", authorName)
                        .add("book_name", bookName)
                );

            }
            return jsonArrayBuilder.build();

        } catch (Exception e) {
            System.out.println("error " + e);
        }
        return null;
    }
    }

