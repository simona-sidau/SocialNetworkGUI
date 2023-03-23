package org.example.domain.validators;

import org.example.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship friendship) throws ValidationException {
        if(friendship.getId() == null)
            throw new ValidationException("ID can't be null");
        if(friendship.getUser1() == friendship.getUser2())
            throw new ValidationException("User 1 can't be the same with User 2!");
    }
}
