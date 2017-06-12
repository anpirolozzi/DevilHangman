package hw1.cmd;


import java.util.ArrayList;
import java.util.List;

public class Fattorizzazione implements Command
{
    private static Fattorizzazione instance;//Istanza Fattorizzazione
    private String doc;//Documentazione
    private ArrayList<String> names;//Nomi

    private Fattorizzazione()
    {
        this.doc = "factorize n  --> fattorizzazione di n";
        this.names = new ArrayList<>();
        this.names.add("factorize");
    }

    public static Fattorizzazione getInstance()
    {
        if (instance != null) {return instance;}
        else return instance=new Fattorizzazione();
    }

    public List<String> getNames()
    {
        return this.names;
    }
    public String doc() {return this.doc;}

    public String execute(String cmd) {
        if (checkfact(cmd)) {
            String res="";
            String x=findfact(cmd);
            String modcmd=cmd;
            formatfact(modcmd,x);
            String[] sep = cmd.split(" ");
            long i = 0;  //penultimo fattore
            long k = 0; //contatore occorrenze fattori
            long n = Long.parseLong(sep[1]);
            for (long c = 2; c * c <= n; c++) {
                while (n % c == 0) {
                    //stampa
                    if (c == i) {   //se c è uguale al penultimo fattore i
                        k++;    //aumento il contatore k
                    } else {    //altrimenti
                        if (k != 0) {   //inserisco in stringa "i(k) "
                            {
                                res += String.valueOf(i);
                            }
                            if (k > 1) {
                                res += "(" + String.valueOf(k) + ")";
                            }
                            res += " ";
                        }
                        i = c;
                        k = 1;  //reinizializzo k
                    }   //finestampa
                    n = n / c;
                }
            }
            //stampo opportunamente l'ultimo fattore scomponibile
            if (i > 0)
                res += String.valueOf(i);
            if (k > 1)
            {
                res += "(" + String.valueOf(k) + ")";
            }
            res += " ";
            //stampo il residuo
            if (n > 1) {
                res += String.valueOf(n);
            }
            //formatto e ritorno
            res=res.trim();
            return res;
        }
        else throw new IllegalArgumentException();
    }

    public boolean checkfact(String cmd){
        String x=findfact(cmd);
        if(!(x.equals("")))
        {
            String modcmd=cmd;//mi creo la copia del comando
            formatfact(modcmd,x);//e separo in questa il comando dagli argomenti
            String[] sep = modcmd.split(" ");
            if (sep.length != 2) {return false;}
            if (!(sep[0].equals(x))) {return false;}
            for (int i=1;i<sep.length;i++){
                try{
                    Long.parseLong(sep[i]);}
                catch(Exception e){return false;}
            }
            if (Long.parseLong(sep[1]) >= 2){return true;}
        }
        return false;
    }

    public String formatfact(String modcmd, String x){
        //Separara il comando x dagli argomenti anche quando sono attaccati
        modcmd = modcmd.substring(x.length() + 1, modcmd.length());//  elimino x
        modcmd = x + " " + modcmd;//  e lo reinserisco con un opportuno spazio
        return modcmd;
    }

    public String findfact(String cmd){
        String cmdin="";
        for(String x:names) {
            if (cmd.substring(0, x.length()).equals(x)){
                cmdin=String.valueOf(x);
            }
        }
        return cmdin;
    }
}