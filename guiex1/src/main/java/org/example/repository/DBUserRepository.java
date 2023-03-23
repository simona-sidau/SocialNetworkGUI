package org.example.repository;

import org.example.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBUserRepository {
    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public DBUserRepository(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    public User add(User entity) throws RepositoryException {
        if(find(entity)!=null)
            throw new RepositoryException("Existent entity");
        String sql="insert into users(id,user_name,passwd,first_name,last_name) values (?,?,?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.setString(2, entity.getUserName());
            preparedStatement.setString(3, entity.getPword());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;

    }

    public User update(User entity) throws RepositoryException {

        String sql="UPDATE users set uname=?,password=? where id=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setString(1, entity.getUserName());
            preparedStatement.setString(2, entity.getPword());
            preparedStatement.setObject(3, entity.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    public User delete(User entity) throws RepositoryException {
        if(find(entity)==null)
            throw new RepositoryException("Nonexistent entity");

        String sql="delete from users where id=?";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getId());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    public User find(User entity) {
        User user=new User();
        var _id=entity.getId();
        String sql="SELECT * from users where id=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, _id);
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();
            user.setId(resultSet.getObject("id", UUID.class));
            user.setUserName(resultSet.getString("uname"));
            user.setPword(resultSet.getString("password"));
        }catch (SQLException e){
            return null;
        }
        return user;

    }

    public User findUsername(String username){
        User user=new User();
        String sql="SELECT * from users where uname=?";

        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, username);
            ResultSet resultSet= preparedStatement.executeQuery();

            resultSet.next();

            user.setUserName(resultSet.getString("uname"));
            user.setPword(resultSet.getString("password"));
            user.setId(resultSet.getObject("id", UUID.class));

        }catch (SQLException e){
            return null;

        }
        return user;
    }

    public List<User> getAll() {
        List<User> usersSet= new ArrayList<>();
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM users");
             ResultSet resultSet= preparedStatement.executeQuery()){
            while(resultSet.next()){
                UUID id=UUID.fromString(resultSet.getString("id"));
                String username=resultSet.getString("uname");
                String password=resultSet.getString("password");
                User user=new User(username,password);
                user.setId(id);
                usersSet.add(user);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return usersSet;
    }

    public int size() {
        int sz = 0;
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT COUNT(*) as total FROM users");
             ResultSet resultSet= preparedStatement.executeQuery()){
            resultSet.next();
            sz=resultSet.getInt("total");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return sz;
    }



}
