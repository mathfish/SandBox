package spring.in.action.SpringBoot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import spring.in.action.SpringBoot.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User findByUsername(String username) {
        return jdbcTemplate.queryForObject("SELECT * from user WHERE username = ?",
                                            this::mapRowToUser,
                                            username);
    }

    @Override
    public void save(User user) {
        String query = "INSERT INTO user(username, password, fullname, street, city, state, zip, phone)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, user.getUsername(), user.getPassword(),user.getFullname(), user.getStreet(),
                user.getCity(), user.getState(), user.getZip(), user.getPhone());
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("fullname"),
                        resultSet.getString("street"),
                        resultSet.getString("city"),
                        resultSet.getString("state"),
                        resultSet.getString("zip"),
                        resultSet.getString("phone")
                );
    }
}
