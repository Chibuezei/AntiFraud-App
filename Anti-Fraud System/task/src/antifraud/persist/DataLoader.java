package antifraud.persist;

import antifraud.entities.Role;
import antifraud.persist.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    @Autowired
    private RoleRepository roleRepository;

    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        createRoles();
    }

    private void createRoles() {
        try {
            roleRepository.save(new Role("MERCHANT"));
            roleRepository.save(new Role("ADMINISTRATOR"));
            roleRepository.save(new Role("SUPPORT"));
            roleRepository.save(new Role("ROLE_AUDITOR"));
        } catch (Exception e) {

        }
    }
}