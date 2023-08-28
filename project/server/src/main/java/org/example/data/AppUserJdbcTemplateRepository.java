package org.example.data;

import org.example.models.AppUser;
import org.example.data.mapper.AppUserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<AppUser> findAll() {
        final String sql = "select app_user_id, username, password_hash, enabled " +
                "from app_user " +
                "order by app_user_id;";

        // Use a Lambda to fetch Appuser fields via ResultSet and then grab the roles via
        // getRolesByUsername since we have access to username. Can then create an instance of
        // AppUser and add them to our Array List of AppUsers
        List<AppUser> userList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            int userId = rs.getInt("app_user_id");
            String username = rs.getString("username");
            String passwordHash = rs.getString("password_hash");
            boolean enabled = rs.getBoolean("enabled");

            List<String> roles = getRolesByUsername(username);

            return new AppUser(userId, username, passwordHash, enabled, roles);
        });

        return userList;
    }

    @Override
    public AppUser findByUserId(int userId) {
        List<String> roles = getRolesByUsername(String.valueOf(userId));

        String sql = "select app_user_id, username, password_hash, enabled " +
                "from app_user " +
                "where app_user_id = ?";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), userId).stream().findFirst().orElse(null);
    }

    @Override
    public AppUser findByUserName(String userName) {
        List<String> roles = getRolesByUsername(userName);

        String sql = "select app_user_id, username, password_hash, enabled " +
                "from app_user " +
                "where username = ?";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), userName).stream().findFirst().orElse(null);
    }

    @Override
    public AppUser create(AppUser appUser) {
        final String sql = "insert into app_user (username, password_hash, enabled) " +
                "values (?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, appUser.getUsername());
            statement.setString(2, appUser.getPassword());
            statement.setBoolean(3, appUser.isEnabled());
            return statement;
        }, keyHolder);

        if (rowsAffected == 0) {
            return null;
        }

        appUser.setAppUserId(keyHolder.getKey().intValue());

        updateRoles(appUser);

        return appUser;
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getAppUserId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (GrantedAuthority role : authorities) {
            String sql = "insert into app_user_role (app_user_id, app_role_id) "
                    + "select ?, app_role_id from app_role where `name` = ?;";
            jdbcTemplate.update(sql, user.getAppUserId(), role.getAuthority());
        }
    }


    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
    }

        private List<String> getRolesByUserId(int userId) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), userId);
    }
}
