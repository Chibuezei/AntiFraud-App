package antifraud.service;

import antifraud.entities.Role;
import antifraud.entities.UserDTO;
import antifraud.persist.DataLoader;
import antifraud.entities.User;
import antifraud.persist.RoleRepository;
import antifraud.persist.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    DataLoader dataLoader;

    public ResponseEntity<?> save(User user) {
        if (userRepository.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Role role = findAll().isEmpty() ?
                roleRepository.findByName("ADMINISTRATOR") :
                roleRepository.findByName("MERCHANT");
        if (role == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found!");
        if (findAll().isEmpty()) user.isLocked(false);
        user.addRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(new UserDTO(user), HttpStatus.CREATED);
    }

    public UserDTO grantRole(String username, String roleName) {
        Role role = roleRepository.findByName(roleName.toUpperCase());
        if (role == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found!");
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRoles().contains(role))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Role already granted");
        user.addRole(role);
        return new UserDTO(userRepository.save(user));
    }

    public ResponseEntity<?> deleteUser(String username) {
        User user = userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        userRepository.delete(user);
        return new ResponseEntity<>(
                Map.of("username", user.getUsername(), "status", "Deleted successfully!"),
                HttpStatus.OK);
    }

    public List<UserDTO> findAll() {

        List<UserDTO> userDTOList = new ArrayList<>();
        for (User user : userRepository.findAllByOrderById()) {
            userDTOList.add(new UserDTO(user));
        }
        return userDTOList;
    }

    public void lock(User user) {
        user.isLocked(true);
        userRepository.save(user);
    }

    public void unlock(User user) {
        user.isLocked(false);
        userRepository.save(user);
    }

    public Map<String, String> changeAccess(@RequestBody Map<String, String> input) {
        User user1 = userRepository.findByUsernameIgnoreCase(input.get("username"))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        String operation = input.get("operation");

        switch (operation) {
            case "LOCK":
                lock(user1);
                return Map.of("status",
                        String.format("User %s locked!", user1.getUsername()));
            case "UNLOCK":
                unlock(user1);
                return Map.of("status",
                        String.format("User %s unlocked!", user1.getUsername()));
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Operation must be only LOCK or UNLOCK!");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User" + username + " is NOT found!"));
    }

}
