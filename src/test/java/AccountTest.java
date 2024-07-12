import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountTest {

    @Test
    public void testCreateAccountWithoutName() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account ac = new Account("");
        });
    }

    @Test
    public void testCreateAccountWithName() {
        assertAll( () -> { Account ac = new Account("bnk"); } );
    }

    @Test
    public void testSetCurcntToNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            Account ac = new Account("bnk");
            ac.setCurcnt(CurrencyTypes.INR, -100);
        });
    }

    @Test
    public void testSetCurcntToPositiveValue() {
        Account ac = new Account("bnk");
        int cnt = 100 ;
        ac.setCurcnt(CurrencyTypes.INR, cnt);
        assertEquals(cnt, ac.getCurcnt().get(CurrencyTypes.INR));
    }

    @Test
    public void testUndoException() throws NothingToUndo {
        assertThrows(NothingToUndo.class, () -> {
            Account ac = new Account("bnk");
            ac.setCurcnt(CurrencyTypes.INR, 100);
            ac.undo().undo().undo();
        });
    }

    @Test
    public void testUndoCurcnt_null() throws NothingToUndo {
        Account ac = new Account("bnk");
        int cnt = 100 ;
        ac.setCurcnt(CurrencyTypes.INR, cnt);
        ac.undo();
        assertNull(ac.getCurcnt().get(CurrencyTypes.INR));
    }

    @Test
    public void testUndoCurcnt_prev_value() throws NothingToUndo {
        int cnt = 100 ;
        String startName = "bnk" ;
        Account ac = new Account(startName);
        ac.setCurcnt(CurrencyTypes.INR, cnt);
        ac.setCurcnt(CurrencyTypes.INR, cnt+100);
        ac.setName("bnk_2");
        ac.undo().undo();
        assertAll("AccountUndo",
                () -> { assertEquals(startName, ac.getName()); },
                () -> { assertEquals(cnt,ac.getCurcnt().get(CurrencyTypes.INR)); }
        );
    }

    @Test
    public void testSave() {
        int cntRUB = 100 ;
        int cntINR = 200 ;
        String startName = "bnk" ;
        Account ac = new Account(startName);
        ac.setCurcnt(CurrencyTypes.RUB, cntRUB);
        ac.setCurcnt(CurrencyTypes.INR, cntINR);

        Loadable bak = ac.Save();

        ac.setCurcnt(CurrencyTypes.RUB, cntRUB + 100);
        ac.setCurcnt(CurrencyTypes.INR, cntINR + 100);
        ac.setName("bnk_2");

        //восстановление прежних значений
        bak.load();

        assertAll("AccountSave",
                () -> { assertEquals(startName, ac.getName()); },
                () -> { assertEquals(cntRUB,ac.getCurcnt().get(CurrencyTypes.RUB)); },
                () -> { assertEquals(cntINR,ac.getCurcnt().get(CurrencyTypes.INR)); }
        );
    }

}
