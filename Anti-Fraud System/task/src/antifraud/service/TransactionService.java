package antifraud.service;

import antifraud.entities.Ip;
import antifraud.entities.StolenCard;
import antifraud.entities.TransactionDTO;
import antifraud.persist.IpRepository;
import antifraud.persist.StolenCardRepository;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.commons.validator.routines.checkdigit.LuhnCheckDigit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    IpRepository ipRepository;
    @Autowired
    StolenCardRepository stolenCardRepository;

    InetAddressValidator validator = InetAddressValidator.getInstance();


    public ResponseEntity<?> checkAmount(TransactionDTO input)
            throws NumberFormatException, NullPointerException {

        long amount = input.getAmount();
        String ipAddress = input.getIp();
        String cardNumber = input.getNumber();
        StringBuilder info = new StringBuilder();
        String result = null;

        if (!validator.isValidInet4Address(ipAddress) ||
                !validCard(cardNumber) ||
                amount <= 0) {
            System.out.println("let see");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        if (amount <= 200) {
            result = "ALLOWED";
            info = new StringBuilder("none");
        } else if (amount <= 1500) {
            result = "MANUAL_PROCESSING";
            info.append("amount");
        } else {
            result = "PROHIBITED";
            info.append("amount");
        }

        if (stolenCardRepository.findByNumber(cardNumber).isPresent()) {
            if (!result.equals("PROHIBITED")) {
                info = new StringBuilder("card-number");
                result = "PROHIBITED";
            } else {
                info.append(", card-number");
            }

        }

        if (ipRepository.findByIp(ipAddress).isPresent()) {
            if (!result.equals("PROHIBITED")) {
                info = new StringBuilder("ip");
                result = "PROHIBITED";
            } else {
                info.append(", ip");

            }

        }

        return new ResponseEntity<>(
                Map.of("result", result,
                        "info", info),
                HttpStatus.OK);

    }

    public ResponseEntity<?> validateIp(String ipAddress) {
        if (!validator.isValidInet4Address(ipAddress)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        if (ipRepository.findByIp(ipAddress).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        Ip ip = new Ip(ipAddress);
        ipRepository.save(ip);

        return new ResponseEntity<>(
                ip,
                HttpStatus.OK);

    }

    public ResponseEntity<?> deleteIp(String ip) {
        if (!validator.isValidInet4Address(ip)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Ip ipaddress = ipRepository.findByIp(ip)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ipRepository.delete(ipaddress);
        String status = "IP " + ip + " successfully removed!";
        return new ResponseEntity<>(
                Map.of("status", status),
                HttpStatus.OK);

    }

    public ResponseEntity<?> getAllIp() {
        return new ResponseEntity<>(
                ipRepository.findAllByOrderById(),
                HttpStatus.OK);
    }

    public boolean validCard(String cardNumber) {
        if (LuhnCheckDigit.LUHN_CHECK_DIGIT.isValid(cardNumber)) {
            return true;
        } else {
            System.out.println("invalid card");
            return false;
        }
    }

    public ResponseEntity<?> validateCard(String cardNumber) {

        if (!validCard(cardNumber)) {
            System.out.println("bad_number");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (stolenCardRepository.findByNumber(cardNumber).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        StolenCard stolenCard = new StolenCard(cardNumber);
        stolenCardRepository.save(stolenCard);

        return new ResponseEntity<>(
                stolenCard,
                HttpStatus.OK);
    }


    public ResponseEntity<?> deleteCard(String number) {
        if (!validCard(number)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        StolenCard stolenCard = stolenCardRepository
                .findByNumber(number)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        stolenCardRepository.delete(stolenCard);
        String status = "Card " + number + " successfully removed!";
        return new ResponseEntity<>(
                Map.of("status", status),
                HttpStatus.OK);

    }

    public ResponseEntity<?> getAllCard() {
        return new ResponseEntity<>(
                stolenCardRepository.findAllByOrderById(),
                HttpStatus.OK);
    }
}