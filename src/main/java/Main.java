import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws NothingToUndo {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Main begin!");
        Account ac = new Account("bnk");
        // количества валют
        System.out.println("Количества валют. Проверка отката изменений!");
        ac.setCurcnt(CurrencyTypes.RUB, 100);
        ac.setCurcnt(CurrencyTypes.INR, 50);
        ac.setCurcnt(CurrencyTypes.INR, 500);
        ac.printCurCnt();
        System.out.println("Количества валют. Состояние после отката изменений!");
        ac.undo(); //.undo().undo();
        ac.printCurCnt();

        // имя владельца счета
        System.out.println("Имя владельца счета!");
        ac.setName("bnk2");
        ac.undo();

        System.out.println("\nСохранение");
        ac.setCurcnt(CurrencyTypes.RUB, 1000);
        ac.setCurcnt(CurrencyTypes.INR, 2000);
        ac.printCurCnt();
        Loadable bak = ac.Save();

        ac.setName("_bnk_");
        ac.setCurcnt(CurrencyTypes.RUB, 10);
        ac.setCurcnt(CurrencyTypes.INR, 20);
        System.out.println("\nСохранение, специально изменили значения на другие");
        ac.printCurCnt();

        //восстановление прежних значений
        bak.load();
        System.out.println("\nВосстановление");
        ac.printCurCnt();

        System.out.println("Main end!");

    }
}
