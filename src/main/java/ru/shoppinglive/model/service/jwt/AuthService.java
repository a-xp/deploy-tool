package ru.shoppinglive.model.service.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by rkhabibullin on 30.03.2017.
 */
@Service("userDetailsService")
public class AuthService implements UserDetailsService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Map<String, Object> map = jdbcTemplate.queryForMap("SELECT * FROM soa_deploy_user WHERE login=?", username);
            return new User(username, map.get("password").toString(), map.get("enabled").equals(1),
                    true, true, true, getAuthorities(username));
        }catch (Exception e){
            throw new UsernameNotFoundException("Can not find user", e);
        }
    }

    private List<GrantedAuthority> getAuthorities(String login){
        return jdbcTemplate.queryForList("SELECT DISTINCT r.code FROM soa_deploy_rights r " +
                "INNER JOIN soa_deploy_user_rights ur ON ur.right_id=r.id " +
                "INNER JOIN soa_deploy_user u ON u.login=? AND ur.user_id=u.id", login).stream()
                .map(map -> new SimpleGrantedAuthority(map.get("code").toString())).collect(Collectors.toList());
    }
}
