package testEFR;

import org.junit.Test;
import utils.Mqlib;

public class Service {

    @Test
    public void test(){
        //ESB_TO_PC
        //http://10.4.111.135
        //10.4.111.139
        Mqlib.receiveMessageFromMQ("ESB_TO_PC", "10.4.111.139",
                "1414", "QM01", "ESB.SVRCONN");
    }
}
