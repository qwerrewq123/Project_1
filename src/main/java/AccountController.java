import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreationRequest request) {
        Account account = accountService.createAccount(request.getUserId(), request.getInitialBalance());
        return ResponseEntity.ok(account);
    }

    @PostMapping("/close")
    public ResponseEntity<String> closeAccount(@RequestBody AccountClosureRequest request) {
        accountService.closeAccount(request.getUserId(), request.getAccountNumber());
        return ResponseEntity.ok("계좌가 성공적으로 해지되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Account>> getAccounts(@RequestParam String userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(accounts);
    }
}