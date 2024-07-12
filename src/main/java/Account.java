import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.sql.SQLOutput;

public class Account {

    public Loadable Save() {return new Snapshot();}
    private class Snapshot implements Loadable
    {
        private String name;

        private HashMap<CurrencyTypes, Integer> curcnt;

        public Snapshot ()
        {
            this.name = Account.this.name;
            this.curcnt = new HashMap<>(Account.this.curcnt);

        }
        @Override
        public void load() {
            Account.this.name = this.name;
            Account.this.curcnt = new HashMap<>(this.curcnt);
        }
    }

    private Deque<Command> commands = new ArrayDeque<>();

    private Account(){};

    public  Account undo() throws NothingToUndo {
        if (commands.isEmpty()) throw new NothingToUndo();
        commands.pop().perform();
        return this;
    }

    public Account(String name)
    {
        this.setName(name);
        this.curcnt = new HashMap<>();
    }

    private String name;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException();
        String oldName = this.name;
        this.commands.push(()->{this.name = oldName;});
        this.name = name;
    }

    private HashMap<CurrencyTypes, Integer> curcnt;

    public HashMap<CurrencyTypes, Integer> getCurcnt() {
        return new HashMap<CurrencyTypes, Integer>(this.curcnt);
    }

    public void setCurcnt(CurrencyTypes curtype, Integer val) {
        if (val<0) throw new IllegalArgumentException();

        // запись для такой валюты уже есть
        if (curcnt.containsKey(curtype))
        {
            int oldval = curcnt.get(curtype);
            this.commands.push(()->{this.curcnt.put(curtype, oldval);});
        }
        // новое значение
        else
        {
            this.commands.push(()->{this.curcnt.remove(curtype);});
        }
        this.curcnt.put(curtype, val);
    }

    public void printCurCnt()
    {
        this.curcnt.values().stream().forEach(System.out::println);
    }
}
