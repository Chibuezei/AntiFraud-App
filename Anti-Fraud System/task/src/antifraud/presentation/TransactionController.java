package antifraud.presentation;

import antifraud.entities.TransactionDTO;
import antifraud.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/antifraud")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<?> checkTransaction(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionService.checkAmount(transactionDTO);
    }

    @PostMapping("/stolencard")
    public ResponseEntity<?> postCard(@RequestBody Map<String, String> input) {
        String cardNumber = input.get("number");
        return transactionService.validateCard(cardNumber);
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<?> deleteCard(@PathVariable("number") String number) {
        return transactionService.deleteCard(number);
    }

    @GetMapping("/stolencard")
    public ResponseEntity<?> getCards() {
        return transactionService.getAllCard();
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<?> postIp(@RequestBody Map<String, String> input) {
        String ip = input.get("ip");
        return transactionService.validateIp(ip);
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<?> deleteIp(@PathVariable("ip") String ip) {
        return transactionService.deleteIp(ip);
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<?> getIps() {
        return transactionService.getAllIp();
    }

}
