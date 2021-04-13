package Reader;

import org.junit.Test;
import store.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReadTest {

    @Test
    public void test01() {
        List amen = new ArrayList();
        List goods = new ArrayList();
        boolean alo = Read.ReadWarehousePlan(amen);
        if(alo){
            boolean hell = Read.ReadStock(amen, goods);
        }
    }
}