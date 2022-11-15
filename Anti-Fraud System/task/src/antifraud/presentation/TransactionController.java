package antifraud.presentation;

import antifraud.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {

    @PostMapping("/transaction")
    public ResponseEntity<?> checkTransaction(@RequestBody Map<String, String> input) {
        return TransactionService.checkAmount(input);
    }

}
