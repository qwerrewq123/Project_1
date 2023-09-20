import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    // 잔액 사용
    public Transaction makeTransaction(String userId, String accountNumber, BigDecimal amount) {
        // 잔액 사용 로직 구현
        // 사용자, 계좌, 잔액 확인
        // 충분한 잔액이 있을 경우 거래 생성
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        if (account == null || !account.getUserId().equals(userId) || !account.isActive() || account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("거래 실패");
        }
        Transaction transaction = new Transaction();
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setType("WITHDRAWAL");
        transaction.setRelatedTransactionId(null); // 초기 거래
        // 거래 생성 후 계좌 잔액 갱신
        account.setBalance(account.getBalance().subtract(amount));
        accountService.updateAccount(account);
        return transactionRepository.save(transaction);
    }

    // 잔액 사용 취소
    public Transaction cancelTransaction(String transactionId, String accountNumber, BigDecimal cancelAmount) {
        // 잔액 사용 취소 로직 구현
        // 원거래 조회, 취소 금액 확인
        Transaction originalTransaction = transactionRepository.findById(Long.parseLong(transactionId)).orElse(null);
        if (originalTransaction == null || !originalTransaction.getAccountNumber().equals(accountNumber) ||
                !originalTransaction.getType().equals("WITHDRAWAL") ||
                originalTransaction.getAmount().compareTo(cancelAmount) != 0) {
            throw new RuntimeException("거래 취소 실패");
        }
        Transaction cancellationTransaction = new Transaction();
        cancellationTransaction.setAccountNumber(accountNumber);
        cancellationTransaction.setAmount(cancelAmount);
        cancellationTransaction.setTransactionDate(LocalDateTime.now());
        cancellationTransaction.setType("CANCEL");
        cancellationTransaction.setRelatedTransactionId(originalTransaction.getId());
        // 거래 생성 후 계좌 잔액 갱신
        Account account = accountService.getAccountByAccountNumber(accountNumber);
        account.setBalance(account.getBalance().add(cancelAmount));
        accountService.updateAccount(account);
        return transactionRepository.save(cancellationTransaction);
    }

    // 거래 확인
    public Transaction getTransactionById(String transactionId) {
        // 특정 거래 조회
        return transactionRepository.findById(Long.parseLong(transactionId)).orElse(null);
    }
}