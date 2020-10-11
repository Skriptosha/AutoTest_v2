package testEFR;

import org.junit.Test;

public class Service {



    @Test
    public void test(){
        //ESB_TO_PC
        //http://10.4.111.135
        //10.4.111.139
        /*
        Mqlib.receiveMessageFromMQ("ESB_TO_PC", "10.4.111.139",
                "1414", "QM01", "ESB.SVRCONN");

         */
        
        String x = "0.15";
        String regex = "\\d{1,}[.]\\d\\d";
        // ФИО - \D+\s\D+\s\D+
        // дата рождения [«]\d{2}[»]\s\D+\s\d{4}\s\D[.]
        // место рождения .+
        // паспорт серия \d{4}
        // паспорт номер \d{6}
        // выдан [«]\d{2}[»]\s\D+\s\d{4}\s\D[.]
        // кем выдан .+
        // по адресу \d{6}[,]\s.+[,]\s
        // с утвержденными тарифами в размере \d{2,} [(].+?[)]\sрублей\s\d\d\sкопеек
        // уплачиваемой Страховщику в размере \d{2,} [(].+?[)]\sрублей\s\d\d\sкопеек
        // к Договору страхования в размере \d{2,} [(].+?[)]\sрублей\s\d\d\sкопеек
        // премии к плате за присоединение составляет \d{1,}[.]\d\d
        System.out.println(x.matches(regex));
        //System.out.println(LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
