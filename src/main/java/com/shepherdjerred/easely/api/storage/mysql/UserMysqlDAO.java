package com.shepherdjerred.easely.api.storage.mysql;

import com.shepherdjerred.easely.api.object.User;
import com.shepherdjerred.easely.api.storage.dao.UserDAO;
import org.codejargon.fluentjdbc.api.FluentJdbc;
import org.codejargon.fluentjdbc.api.FluentJdbcBuilder;
import org.codejargon.fluentjdbc.api.query.Mapper;
import org.codejargon.fluentjdbc.api.query.Query;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class UserMysqlDAO implements UserDAO {

    private final FluentJdbc fluentJdbc;
    private final Mapper<User> userMapper;

    public UserMysqlDAO(MysqlStore store) {
        fluentJdbc = new FluentJdbcBuilder().connectionProvider(store.getDatabase().getDataSource()).build();
        userMapper = rs -> new User(
                UUID.fromString(rs.getString("user_uuid")),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("easel_username"),
                rs.getString("easel_password")
        );
    }

    public Optional<User> select(String email) {
        Query query = fluentJdbc.query();
        return query.select("SELECT * FROM user WHERE email = ?")
                .params(email)
                .firstResult(userMapper);
    }

    @Override
    public Optional<User> select(UUID uuid) {
        Query query = fluentJdbc.query();
        return query.select("SELECT * FROM user WHERE user_uuid = ?")
                .params(String.valueOf(uuid))
                .firstResult(userMapper);
    }

    @Override
    public Collection<User> select() {
        Query query = fluentJdbc.query();
        return query.select("SELECT * FROM user")
                .listResult(userMapper);
    }

    @Override
    public void insert(User user) {
        Query query = fluentJdbc.query();
        query.update("INSERT INTO user VALUES (?, ?, ?, ?, ?)")
                .params(String.valueOf(user.getUuid()),
                        user.getEmail(),
                        user.getHashedPassword(),
                        user.getEaselUsername(),
                        user.getEaselPassword())
                .run();
    }

    @Override
    public void drop(User user) {
        Query query = fluentJdbc.query();
        query.update("DELETE FROM user WHERE user_uuid = ?")
                .params(String.valueOf(user.getUuid()))
                .run();
    }

    @Override
    public void update(User type) {
        // TODO
    }

}
