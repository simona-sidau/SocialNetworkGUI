package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.Request;
import org.example.domain.User;
import org.example.domain.validators.FriendshipValidator;
import org.example.domain.validators.UserValidator;
import org.example.repository.DBFriendshipRepository;
import org.example.repository.DBRequestRepository;
import org.example.repository.DBUserRepository;
import org.example.repository.RepositoryException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Service {

    private final String dbUsername = "postgres";
    private final String dbPassword = "password";
    private final String db = "jdbc:postgresql://localhost:5432/my_social_network";

    DBUserRepository userRepository = new DBUserRepository(db, dbUsername, dbPassword);

    DBFriendshipRepository friendshipRepository = new DBFriendshipRepository(db, dbUsername, dbPassword);

    DBRequestRepository requestRepository = new DBRequestRepository(db, dbUsername, dbPassword);

    UserValidator userValidator = new UserValidator();

    FriendshipValidator friendshipValidator = new FriendshipValidator();

    public Optional<User> login(String username, String password) {
        var user = userRepository.findUsername(username);
        if (user != null && user.getPword().equals(password))
            return Optional.of(user);
        else
            return Optional.empty();
    }

    public void addRequest(Request<Integer> req) throws ServiceException {
        try {
            req.setStatus(false);
            requestRepository.add(req);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    public Request<Integer> findRequest(Request<Integer> entity){return requestRepository.find(entity);}
    public User findUserByUserName(String userName){
        return  userRepository.findUsername(userName);
    }
    public List<User> getUsers(){
        return  userRepository.getAll();
    }

    public List<Request<Integer>> getRequests(){
        return requestRepository.getAll();
    }
    public List<Request<Integer>> getUserRequests(User owner){
        return requestRepository.findRequests(owner.getId());
    }

    public  List<Friendship<UUID>> getFriendsOf(User owner) { return friendshipRepository.getFriends(owner.getId());
    }
    public User findUserById(UUID e) {
        var aux=new User();
        aux.setId(e);
        return userRepository.find(aux);
    }
    public void addFriend(Friendship<UUID> friendship) throws ServiceException {
        try {

            friendshipRepository.add(friendship);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }
    public void deleteRequest(Request<Integer> found) throws ServiceException {
        try {
            requestRepository.delete(found);
        } catch (RepositoryException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    public Friendship<UUID> findFriendship(Friendship<UUID> friendship) {
        return friendshipRepository.find(friendship);
    }

    public void deleteFriend(Friendship<UUID> friendship) throws ServiceException {
        try {
            friendshipRepository.delete(friendship);
        } catch (RepositoryException e) {
            throw new ServiceException(e);
        }
    }

}