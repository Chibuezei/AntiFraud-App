package antifraud.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Map;
@Service
public class TransactionService {

    public static ResponseEntity<?> checkAmount(Map<String, String> input)
            throws NumberFormatException, NullPointerException {

        try {
            long amount = Long.parseLong((input.get("amount").toString()));

            if (amount <= 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            if (amount <= 200) {
                return new ResponseEntity<>(
                        Map.of("result", "ALLOWED"),
                        HttpStatus.OK);
            }

            if (amount <= 1500) {
                return new ResponseEntity<>(
                        Map.of("result", "MANUAL_PROCESSING"),
                        HttpStatus.OK);
            }

            return new ResponseEntity<>(
                    Map.of("result", "PROHIBITED"),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}