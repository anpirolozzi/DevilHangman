package hw1.cmd;


import java.util.ArrayList;
import java.util.List;

public class Potenza implements Command
{
    private static Potenza instance;//Istanza Potenza
    private String doc;//Documentazione
    private ArrayList<String> names;//Nomi

    private Potenza()
    {
        this.doc = "pow x y  --> x elevato a y";
        this.names = new ArrayList<>();
        this.names.add("^");
        this.names.add("**");
        this.names.add("pow");
    }

    public static Potenza getInstance()
    {
        if (instance != null) {return instance;}
        else return instance=new Potenza();
    }

    public List<String> getNames()
    {
        return this.names;
    }
    public String doc() {return this.doc;}


    public String execute(String cmd) {
        if (checkpow(cmd)) {
            String x=findpow(cmd);
            String modcmd=cmd;
            formatpow(modcmd,x);
            String[] sep = cmd.split(" ");
            Double a = Double.parseDouble(sep[1]);
            Double b = Double.parseDouble(sep[2]);
            return String.valueOf(Math.pow(a, b));
        }
        else throw new IllegalArgumentException();
    }
    public boolean checkpow(String cmd){
                String x=findpow(cmd);
                if(!(x.equals("")))
                {
                    String modcmd=cmd;
                    formatpow(modcmd,x);
                    String[] sep = modcmd.split(" ");
                    if (!(sep.length == 3)) {return false;}
                    if (!(sep[0].equals(x))) {return false;}
                    for (int i=1;i<sep.length;i++){
                        try{Double.parseDouble(sep[i]);}
                        catch(Exception e){return false;}
                    }
                    return true;
                }
        return false;
    }

    public String formatpow(String modcmd, String x){
        //Separara il comando x dagli argomenti anche quando sono attaccati
        modcmd = modcmd.substring(x.length() + 1, modcmd.length());//  elimino x
        modcmd = x + " " + modcmd;//  e lo reinserisco con un opportuno spazio
        return modcmd;
    }

    public String findpow(String cmd){
        String cmdin="";
        for(String x:names) {
            if (cmd.substring(0, x.length()).equals(x)){
                cmdin=String.valueOf(x);
            }
        }
        return cmdin;
    }

}