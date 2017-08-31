package app;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Calculator {

    private InterestList interestList;
    private Map<Date, String> list;
    private Map<String, String> rates;
    private double wholeInterest;
    private static final double POINTS = 10D;
    private static final double DAYS_IN_YEAR = 360;

    public Calculator(InterestList interestList) throws IOException, ParseException {
        this.interestList = interestList;
        this.list = this.interestList.getInterestList();
        this.rates = new LinkedHashMap<>();
        this.wholeInterest = 0.0;
    }

    public void calculateRates(Person person) throws ParseException {
            Locale.setDefault(Locale.ROOT);
            Date startDate = person.getStartDate();
            Date endDate = new Date();

            double principal = person.getBond();
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);

            while (true) {

                int daysInPeriod = getDaysInPeriod(cal, startDate, endDate);
                double olp = getPeriodInterest(this.list, cal);

                double interestRate = calculateInterestRate(olp, principal, daysInPeriod);
                this.wholeInterest += interestRate;
                String firstDateOfMonth = getDateString(startDate);
                if(isEndDateMonth(startDate, endDate)) {
                    cal.setTime(endDate);
                } else {
                    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
                String secondDateOfMonth = getDateString(cal.getTime());
                rates.put(firstDateOfMonth + "&" + secondDateOfMonth, String.format("%.2f",interestRate));
                if(isEndDateMonth(startDate, endDate)) {
                    break;
                }
                cal.add(Calendar.DATE, 1);
                startDate = cal.getTime();
            }

            person.setRates(this.rates);
            person.setWholeInterest(String.format("%.2f", this.wholeInterest));
            this.wholeInterest = 0.0;
            this.rates.clear();
    }

    private double getPeriodInterest(Map<Date, String> list, Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        Calendar cal = Calendar.getInstance();
        for (Date date : list.keySet()) {
            cal.setTime(date);
            int currentYear = cal.get(Calendar.YEAR);
            int currentMonth = cal.get(Calendar.MONTH);
            if(year == currentYear && month == currentMonth) {
                return Double.valueOf(list.get(date));
            }
        }
        return 0.0D;
    }

    private boolean isEndDateMonth(Date start, Date end) {
        Calendar calFirst = Calendar.getInstance();
        Calendar calSecond = Calendar.getInstance();
        calFirst.setTime(start);
        calSecond.setTime(end);
        if(calFirst.get(Calendar.YEAR) == calSecond.get(Calendar.YEAR)) {
            if(calFirst.get(Calendar.MONTH) == calSecond.get(Calendar.MONTH)) {
                return true;
            }
        }

        return false;
    }

    private int getDaysInPeriod(Calendar cal, Date start, Date end) {
        if(isEndDateMonth(start, end)) {
            long t1 = cal.getTimeInMillis();
            cal.setTime(end);
            long t2 = cal.getTimeInMillis();
            int daysBetween = (int) ((t2 - t1) / (1000 * 60 * 60 * 24));
            return daysBetween + 1;
        }

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) + 1;
    }

    private double calculateInterestRate(double olp, double principal, int daysInPeriod) {
        double percent = ((olp + POINTS)*(daysInPeriod / DAYS_IN_YEAR)) / 100D;
        double result = principal * percent;
        return result;
    }

    private String getDateString(Date start) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String monthString;
        if(month < 10) {
            monthString = "0" + month;
        } else {
            monthString = month + "";
        }

        String dayString;
        if(day < 10) {
            dayString = "0" + day;
        } else {
            dayString = day + "";
        }

        return dayString + "." + monthString + "." + year;
    }
}
