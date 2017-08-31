package app;

public class NumberToWord {

    private static final String CURRENCY = "лева";

    public static String reformat(String doubleNumber) {

        String[] oneToNineteen = {
            "нула", "един", "два", "три", "четири", "пет", "шест", "седем", "осем", "девет", "десет",
            "единадесет", "дванадесет", "тринадесет", "четиринадесет", "петнадесет", "шестнадесет",
            "седемнадесет", "осемнадесет", "деветнадесет"
        };

        String[] twentyToNinetyNine = {
            "двадесет", "тридесет", "четирдесет", "петдесет", "шестдесет", "седемдесет", "осемдесет", "деветдесет"};

        String[] hundreds = {
            "сто", "двеста", "триста", "четористотин", "петстотин", "шестстотин", "седемстотин", "осемстотин", "деветстотин"};

        String[] thousands = {"хиляда", "две хиляди", "три хиляди", "четири хиляди", "пет хиляди", "шест хиляди",
            "седем хиляди", "осем хиляди", "девет хиляди"};
        String[] params = doubleNumber.split("[,.]+");

        int number = Integer.valueOf(params[0]);

        StringBuilder result = new StringBuilder();
        if (number >= 0 && number <= 19) {
            if (number == 1) {
                result.append(String.format("%s лев", oneToNineteen[number]));
            } else {
                result.append(String.format("%s %s", oneToNineteen[number], CURRENCY));
            }
        } else if (number >= 20 && number <= 99) {
            if (number % 10 != 0) {
                int tenIndex = number % 10;
                int tensIndex = (number / 10) - 2;
                result.append(String.format("%s и %s %s", twentyToNinetyNine[tensIndex], oneToNineteen[tenIndex], CURRENCY));
            } else {
                number = (number / 10) - 2;
                result.append(String.format("%s %s", twentyToNinetyNine[number], CURRENCY));
            }
        } else if (number > 99 && number < 1000) {
            if (number % 100 == 0) {
                int hundredIndex = (number / 100) - 1;
                result.append(String.format("%s %s", hundreds[hundredIndex], CURRENCY));
            } else {
                int hundredIndex = (number / 100) - 1;
                int tensNumber = number % 100;

                if (tensNumber >= 20 && tensNumber <= 99) {
                    if (tensNumber % 10 != 0) {
                        int tenIndex = tensNumber % 10;
                        int tensIndex = (tensNumber / 10) - 2;
                        result.append(String.format("%s %s и %s %s",
                            hundreds[hundredIndex], twentyToNinetyNine[tensIndex], oneToNineteen[tenIndex], CURRENCY));
                    } else {
                        tensNumber = (tensNumber / 10) - 2;
                        result.append(String.format("%s и %s %s", hundreds[hundredIndex], twentyToNinetyNine[tensNumber], CURRENCY));
                    }
                } else {
                    result.append(String.format("%s и %s %s", hundreds[hundredIndex], oneToNineteen[tensNumber], CURRENCY));
                }
            }
        } else if (number > 999 && number < 10000) {
            if(number % 1000 == 0) {
                int thousandIndex = (number / 1000) - 1;
                result.append(String.format("%s %s", thousands[thousandIndex], CURRENCY));
            } else {
                int thousandIndex = (number / 1000) - 1;
                int hundredNumber = number % 1000;
                if (hundredNumber % 100 == 0) {
                    int hundredIndex = (hundredNumber / 100) - 1;
                    result.append(String.format("%s и %s %s", thousands[thousandIndex], hundreds[hundredIndex], CURRENCY));
                } else if (hundredNumber > 99) {
                    int hundredIndex = (hundredNumber / 100) - 1;
                    int tensNumber = hundredNumber % 100;
                    if (tensNumber >= 20 && tensNumber <= 99) {
                        if (tensNumber % 10 != 0) {
                            int tenIndex = tensNumber % 10;
                            int tensIndex = (tensNumber / 10) - 2;
                            result.append(String.format("%s %s %s и %s %s",
                                thousands[thousandIndex], hundreds[hundredIndex], twentyToNinetyNine[tensIndex], oneToNineteen[tenIndex], CURRENCY));
                        } else {
                            tensNumber = (tensNumber / 10) - 2;
                            result.append(String.format("%s %s и %s %s",
                                thousands[thousandIndex], hundreds[hundredIndex], twentyToNinetyNine[tensNumber], CURRENCY));
                        }
                    } else {
                        result.append(String.format("%s %s и %s %s",
                            thousands[thousandIndex], hundreds[hundredIndex], oneToNineteen[tensNumber], CURRENCY));
                    }
                } else {
                    if (hundredNumber >= 0 && hundredNumber <= 19) {
                        if (hundredNumber == 1) {
                            result.append(String.format("%s и %s лев", thousands[thousandIndex], oneToNineteen[hundredNumber]));
                        } else {
                            result.append(String.format("%s и %s %s", thousands[thousandIndex], oneToNineteen[hundredNumber], CURRENCY));
                        }
                    } else if (hundredNumber >= 20 && hundredNumber <= 99) {
                        if (hundredNumber % 10 != 0) {
                            int tenIndex = hundredNumber % 10;
                            int tensIndex = (hundredNumber / 10) - 2;
                            result.append(String.format("%s %s и %s %s", thousands[thousandIndex], twentyToNinetyNine[tensIndex], oneToNineteen[tenIndex], CURRENCY));
                        } else {
                            hundredNumber = (hundredNumber / 10) - 2;
                            result.append(String.format("%s и %s %s", thousands[thousandIndex], twentyToNinetyNine[hundredNumber], CURRENCY));
                        }
                    }
                }
            }
        }

        if(params.length > 1) {
            if(params[1].length() < 2) {
                result.append(String.format(" и %s0 ст.", params[1]));
            } else {
                result.append(String.format(" и %s ст.", params[1]));
            }

        } else {
            result.append(" и 0 ст.");
        }

        return "(" + result.toString().trim() + ")";
    }
}
