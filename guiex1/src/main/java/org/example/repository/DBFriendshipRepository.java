package org.example.repository;

import org.example.domain.Friendship;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DBFriendshipRepository {

    private final String urlDb;
    private final String usernameDb;
    private final String passwdDb;

    public DBFriendshipRepository(String urlDb, String usernameDb, String passwdDb) {
        this.urlDb = urlDb;
        this.usernameDb = usernameDb;
        this.passwdDb = passwdDb;
    }

    public Friendship<UUID> add(Friendship<UUID> entity) throws RepositoryException {
        if(find(entity)!=null)
            throw new RepositoryException("Entity exists");
        String sql="insert into friendships(id_user1,id_user2, date) values (?,?,?)";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getUser1());
            preparedStatement.setObject(2, entity.getUser2());
            preparedStatement.setObject(3, entity.getDate());
            preparedStatement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }

    public Friendship<UUID> update(Friendship<UUID> entity) throws RepositoryException {
        return null;
    }

    public Friendship<UUID> delete(Friendship<UUID> entity) throws RepositoryException {

        if(find(entity)==null)
            throw new RepositoryException("Nonexistent entity");

        String sql="delete from friendships where( id_user1 in(?) and id_user2 in (?))";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getUser1());
            preparedStatement.setObject(2, entity.getUser2());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return entity;
    }


    public Friendship<UUID> find(Friendship<UUID> entity) {
        Friendship<UUID> friendship=new Friendship<>();

        String sql="SELECT * from friendships where( id_user1 in(?) and id_user2 in (?))";
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement(sql)){
            preparedStatement.setObject(1, entity.getUser1());
            preparedStatement.setObject(2,  entity.getUser2());
            ResultSet resultSet= preparedStatement.executeQuery();
            resultSet.next();
            friendship.setUser1(resultSet.getObject("id_user1",UUID.class));
            friendship.setUser2(resultSet.getObject("id_user2",UUID.class));
            friendship.setDate(resultSet.getTimestamp("date").toLocalDateTime());
        }catch (SQLException e){

            return null;
        }
        return friendship;
    }

    public List<Friendship<UUID>> getFriends(UUID user) {
        List<Friendship<UUID>> friendshipsSet= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM friendships where (id_user1=? or id_user2=?)")){
            preparedStatement.setObject(1, user);
            preparedStatement.setObject(2, user);
            ResultSet resultSet= preparedStatement.executeQuery();
            while(resultSet.next()){
                UUID user1=resultSet.getObject("id_user1", UUID.class);
                UUID user2=resultSet.getObject("id_user2", UUID.class);
                Friendship<UUID> friendship=new Friendship<>(user1,user2);
                friendship.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                friendshipsSet.add(friendship);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendshipsSet;
    }

    public List<Friendship<UUID>> getAll() {
        List<Friendship<UUID>> friendshipsSet= new ArrayList<>();
        try(Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
            PreparedStatement preparedStatement= connection.prepareStatement("SELECT * FROM friendships");
            ResultSet resultSet= preparedStatement.executeQuery()){
            while(resultSet.next()){
                UUID user1=resultSet.getObject("id_user1", UUID.class);
                UUID user2=resultSet.getObject("id_user2", UUID.class);
                Friendship<UUID> friendship=new Friendship<>(user1,user2);
                friendship.setDate(resultSet.getTimestamp("date").toLocalDateTime());
                friendshipsSet.add(friendship);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendshipsSet;
    }

    public int size() {
        int sz = 0;
        try( Connection connection= DriverManager.getConnection(urlDb,usernameDb,passwdDb);
             PreparedStatement preparedStatement= connection.prepareStatement("SELECT COUNT(*) as total FROM friendships");
             ResultSet resultSet= preparedStatement.executeQuery()){
            resultSet.next();
            sz =resultSet.getInt("total");

        }catch(SQLException e){
            e.printStackTrace();
        }
        return sz;
    }
}
