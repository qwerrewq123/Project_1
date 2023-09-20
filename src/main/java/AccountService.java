import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    // 계좌 생성
    public Account createAccount(String userId, BigDecimal initialBalance) {
        // 사용자 아이디와 초기 잔액으로 계좌 생성 로직 구현
        // 계좌 번호 생성 및 중복 체크
        // 중복된 계좌 번호가 없을 경우 새로운 계좌 생성
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(initialBalance);
        account.setAccountNumber(generateAccountNumber());
        account.setCreatedDate(LocalDateTime.now());
        account.setActive(true);
        return accountRepository.save(account);
    }

    // 계좌 해지
    public void closeAccount(String userId, String accountNumber) {
        // 계좌 해지 로직 구현
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null || !account.getUserId().equals(userId) || !account.isActive()) {
            throw new RuntimeException("계좌 해지 실패");
        }
        account.setActive(false);
        accountRepository.save(account);
    }

    // 계좌 확인
    public List<Account> getAccountsByUserId(String userId) {
        // 사용자 아이디로 계좌 목록 조회
        return accountRepository.findByUserId(userId);
    }

    private String generateAccountNumber() {
        // 계좌 번호 생성 로직 구현 (랜덤 또는 순차적 생성)
        // 중복 체크 필요
        // 예시: 랜덤 10자리 숫자 생성
        Random rand = new Random();
        String accountNumber;
        do {
            accountNumber = String.format("%010d", rand.nextInt(1000000000));
        } while (accountRepository.findByAccountNumber(accountNumber) != null);
        return accountNumber;
    }

    public Account getAccountByAccountNumber(String accountNumber) {
    return null;
    }

    public void updateAccount(Account account) {
    }
}