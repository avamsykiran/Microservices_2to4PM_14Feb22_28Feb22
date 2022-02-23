package in.cts.budgetanalysis.statement.services;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.cts.budgetanalysis.statement.exceptions.BadStatementException;
import in.cts.budgetanalysis.statement.models.Statement;

@Service
public class StatementServiceImpl implements StatementService {
	
	@Autowired
	private ProfilesClient profiles;
	
	@Autowired
	private TxnsClient txns;

	private Statement generateStatement(long ahId, LocalDate from, LocalDate to) throws BadStatementException {
		Statement st = null;
		
		if(!profiles.doesAccountHolderExists(ahId)) {
			throw new BadStatementException("Statement for a non-existing account can not be generated");
		}
		
		
		
		return st;
	}
		
	@Override
	public Statement generateMonthlyStatement(long ahId, Month month, int year) throws BadStatementException {
		LocalDate from = LocalDate.of(year, month, 1);
		LocalDate to = from.with(TemporalAdjusters.lastDayOfMonth());
		return generateStatement(ahId, from, to);
	}

	@Override
	public Statement generateAnnualStatement(long ahId, int year) throws BadStatementException {
		LocalDate from = LocalDate.of(year, Month.JANUARY, 1);
		LocalDate to = from.with(TemporalAdjusters.lastDayOfYear());
		return generateStatement(ahId, from, to);
	}

}
