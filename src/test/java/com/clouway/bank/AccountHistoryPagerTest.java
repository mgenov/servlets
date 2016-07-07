package com.clouway.bank;

import com.clouway.bank.core.AccountHistoryPager;
import com.clouway.bank.core.AccountHistoryRepository;
import com.clouway.bank.core.AccountPager;
import com.clouway.bank.core.AccountRecord;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

/**
 * @author Krasimir Raikov(raikov.krasimir@gmail.com)
 */
public class AccountHistoryPagerTest {
  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();
  @Mock
  AccountHistoryRepository accountHistoryRepository;
  private AccountPager accountPager;
  private int pageSize = 20;

  @Before
  public void setUp() {
    accountPager = new AccountHistoryPager(pageSize, accountHistoryRepository);
  }

  @Test
  public void calculatingPage() {
    List<AccountRecord> recordsExpected = new ArrayList<>();
    recordsExpected.add(new AccountRecord(130L, "Parvan", "deposit", 13D));
    context.checking(new Expectations() {{

      oneOf(accountHistoryRepository).getAccountRecords("Samuel", 0, pageSize);
      will(returnValue(recordsExpected));
    }});


    List<AccountRecord> accountRecords = accountPager.requestPage("Samuel", 0);

    assertThat(accountPager.getCurrentPageNumber(), is(equalTo(0)));
    assertThat(accountRecords, is(equalTo(recordsExpected)));
  }

  @Test
  public void calculatingSecondPage() {
    List<AccountRecord> recordsExpected = new ArrayList<>();
    recordsExpected.add(new AccountRecord(100L, "Petar", "deposit", 12.5D));
    context.checking(new Expectations() {{

      oneOf(accountHistoryRepository).getAccountRecords("Samuel", pageSize, pageSize + 1);
      will(returnValue(recordsExpected));
    }});


    List<AccountRecord> accountRecords = accountPager.requestPage("Samuel", 1);

    assertThat(accountPager.getCurrentPageNumber(), is(equalTo(1)));
    assertThat(accountRecords, is(equalTo(recordsExpected)));
  }

}