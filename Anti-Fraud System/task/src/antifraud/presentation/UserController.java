package antifraud.presentation;

import antifraud.entities.User;
import antifraud.entities.UserDTO;
import antifraud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
//@PreAuthorize("isAuthenticated()")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody @Valid User user) {
        return userService.save(user);
    }

    //    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('SUPPORT') or hasRole('ROLE_AUDITOR')")
    @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {

        return userService.deleteUser(username);

    }

    @PutMapping("/role")
    public ResponseEntity<?> grantRole(@RequestBody Map<String, String> map) {
        System.out.println("here3");
        if (!map.get("role").equals("SUPPORT") && !map.get("role").equals("MERCHANT")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.grantRole(map.get("username"), map.get("role")), HttpStatus.OK);
    }

    @PutMapping("/access")
    public Map<String, String> lockUser(@RequestBody Map<String, String> input) {
        return userService.changeAccess(input);
    }

}
