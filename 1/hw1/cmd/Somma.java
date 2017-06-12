package hw1.cmd;


import java.util.ArrayList;
import java.util.List;
import java.lang.Double;

public class Somma implements Command
{
    private static Somma instance;//Istanza Somma
    private String doc;//Documentazione
    private ArrayList<String> names;//Nomi

    private Somma()
    {
        this.doc = "+ x y ... z  --> somma x+y+...+z";
        this.names = new ArrayList<>();
        this.names.add("+");
        this.names.add("sum");
    }

    public static Somma getInstance()
    {
        if (instance != null) {return instance;}
        else return instance=new Somma();
    }

    public List<String> getNames()
    {
        return this.names;
    }
    public String doc() {return this.doc;}

    public String execute(String cmd) {
        if (checksum(cmd)) {
            String x = findsum(cmd);
            String modcmd = cmd;
            formatsum(modcmd, x);
            String[] sep = cmd.split(" ");
            Double somma = 0.0;
            int i = 1;
            while (i < sep.length) {
                somma += Double.parseDouble(sep[i]);
                i++;}
            return String.valueOf(somma);
        }
        else throw new IllegalArgumentException();
    }

    public boolean checksum(String cmd){
        String x=findsum(cmd);
        if(!(x.equals("")))
        {
            String modcmd=cmd;
            formatsum(modcmd, x);
            String[] sep = modcmd.split(" ");
            if (!(sep[0].equals(x))) {return false;}
            for (int i=1;i<sep.length;i++){
                try{Double.parseDouble(sep[i]);}
                catch(Exception e){return false;}
            }
            return true;
        }
        return false;
    }

    public String formatsum(String modcmd, String x){
        //Separara il comando x dagli argomenti anche quando sono attaccati
        modcmd = modcmd.substring(x.length() + 1, modcmd.length());//  elimino x
        modcmd = x + " " + modcmd;//  e lo reinserisco con un opportuno spazio
        return modcmd;
    }

    public String findsum(String cmd){
        String cmdin="";
        for(String x:names) {
            if (cmd.substring(0, x.length()).equals(x)){
                cmdin=String.valueOf(x);
            }
        }
        return cmdin;
    }

}