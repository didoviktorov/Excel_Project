package app;

import java.util.*;

public class Person {

    private String fullName;
    private String egn;
    private double bond;
    private String invoiceNumber;
    private Date startDate;
    private String wholeInterest;
    private Map<String, String> rates;
    private String city;
    private String address;
    private String court;
    private String courtAddress;
    private String postalCode;
    private String charge;
    private String honorary;

    public Person(String egn, String fullName, double bond,
                  String invoiceNumber, Date startDate, String city,
                  String address, String court, String courtAddress, String postalCode, String charge, String honorary) {
        this.fullName = fullName;
        this.egn = egn;
        this.bond = bond;
        this.invoiceNumber = invoiceNumber;
        this.startDate = startDate;
        this.city = city;
        this.address = address;
        this.court = court;
        this.courtAddress = courtAddress;
        this.postalCode = postalCode;
        this.charge = charge;
        this.honorary = honorary;
    }

    public String getFullName() {
        return this.fullName;
    }

    public String getEgn() {
        return this.egn;
    }

    public double getBond() {
        return this.bond;
    }

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public String getWholeInterest() {
        return this.wholeInterest;
    }

    public void setWholeInterest(String wholeInterest) {
        this.wholeInterest = wholeInterest;
    }

    public Map<String, String> getRates() {
        return Collections.unmodifiableMap(this.rates);
    }

    public void setRates(Map<String, String> rates) {
        this.rates = new LinkedHashMap<>(rates);
    }

    public String getCity() {
        return this.city;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCourt() {
        return this.court;
    }

    public String getCourtAddress() {
        return this.courtAddress;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCharge() {
        return this.charge;
    }

    public String getHonorary() {
        return this.honorary;
    }

    public String getLastDate () {
        int lastDateIndex = this.rates.keySet().size() - 1;
        List<String> dates = new ArrayList<>(this.rates.keySet());
        String lastDate = dates.get(lastDateIndex).split("&")[1];
        return lastDate;
    }
}
