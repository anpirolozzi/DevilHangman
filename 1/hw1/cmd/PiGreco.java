package hw1.cmd;


import java.util.ArrayList;
import java.util.List;

public class PiGreco implements Command
{
    private static PiGreco instance;//Istanza PiGreco
    private String doc;//Documentazione
    private ArrayList<String> names;//Nomi

    private PiGreco()
    {
        this.doc = "pi  --> valore di Pi greco";
        this.names = new ArrayList<>();
        this.names.add("pi");
        this.names.add("Pi");
        this.names.add("PI");
    }

    public static PiGreco getInstance()
    {
        if (instance != null) {return instance;}
        else return instance=new PiGreco();
    }

    public List<String> getNames()
    {
        return this.names;
    }
    public String doc() {return this.doc;}


	public String execute(String cmd) {
        if (checkpi(cmd)) return String.valueOf(Math.PI);
        else  throw new IllegalArgumentException();
    }

    public  boolean checkpi(String cmd){
        for(String x:names)
        {if (cmd.replace(" ","").equals(x))return true;}
        return false;
    }
}