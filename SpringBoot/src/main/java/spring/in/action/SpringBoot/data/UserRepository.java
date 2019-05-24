package spring.in.action.SpringBoot.data;

import spring.in.action.SpringBoot.User;

public interface UserRepository {
    User findByUsername(String username);

    void save(User user);
}

