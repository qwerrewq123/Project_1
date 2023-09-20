import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/make")
    public ResponseEntity<Transaction> makeTransaction(@RequestBody TransactionRequest request) {
        Transaction transaction = transactionService.makeTransaction(request.getUserId(), request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Transaction> cancelTransaction(@RequestBody TransactionCancellationRequest request) {
        Transaction transaction = transactionService.cancelTransaction(request.getTransactionId(), request.getAccountNumber(), request.getCancelAmount());
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/get")
    public ResponseEntity<Transaction> getTransaction(@RequestParam String transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}