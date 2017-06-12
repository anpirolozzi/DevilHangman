/* <b>ATTENZIONE: NON MODIFICARE IN ALCUN MODO QUESTO FILE</b> */
package hw1.test;

import hw1.cmd.Command;
import hw1.cmd.CmdFactory;
import hw1.cmd.CmdManager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;

/** Per calcolare il punteggio dell'homework eseguire il main di questa classe.
 * <br>
 * <b>ATTENZIONE: NON MODIFICARE IN ALCUN MODO QUESTA CLASSE.</b> */
public class Grade {
    public static void main(String[] args) {
        test();
    }







    private static void test() {
        totalScore = 0;
        String nullEx = "ERRORE mancato lancio di NullPointerException";
        String argEx = "ERRORE mancato lancio di IllegalArgumentException";
        boolean ok = test(CT.PI, 0.25f, 1, 2000, 4000);
        if (ok) ok = test(CT.SUM, 0.25f, 2, 2000, 10000);
        if (ok) ok = test(CT.POW, 0.25f, 2, 2000, 10000);
        if (ok) ok = test(CT.FACTORIZE, 0.25f, 4, 2000, 60000);
        if (ok) ok = test(null, null, false, new CT[] {CT.PI}, 2, 10000);
        if (ok) ok = test(nullEx, null, false, new CT[] {CT.PI, null}, 1, 10000);
        if (ok) ok = test(null, argEx, false, new CT[] {CT.PI, CT.SUM, CT.DUMMY}, 1, 10000);
        if (ok) ok = test(null, null, true, new CT[] {CT.PI, CT.SUM}, 1, 10000);
        if (ok) ok = test(null, null, true, new CT[] {CT.PI, CT.POW}, 1, 10000);
        if (ok) ok = test(null, null, true, new CT[] {CT.PI, CT.SUM, CT.POW}, 1, 10000);
        if (ok) ok = test(null, null, true, new CT[] {CT.PI, CT.SUM, CT.POW, CT.FACTORIZE}, 1, 70000);
        if (ok) ok = test(1, 10000);
        System.out.println("Punteggio totale: "+totalScore);
    }

    private static boolean test(CT ct, float sc, float scE, int ms, int msE) {
        if (!runTest("Test commando "+ct+" metodo names", sc, ms, () -> test_cmd_names(ct)))
            return false;
        if (!runTest("Test commando "+ct+" metodo doc", sc, ms, () -> test_cmd_doc(ct)))
            return false;
        if (!runTest("Test commando "+ct+" metodo execute", scE, msE, () -> test_cmd_exe(ct)))
            return false;
        return true;
    }

    private static boolean test(String nullEx, String argEx, boolean testH, CT[] ctt, float sc, int ms) {
        return runTest("Test CmdManager coi comandi "+Arrays.toString(ctt)+" ("+(testH ? "con help" : "senza help")+")",
                sc, ms, () -> test_manager(nullEx, argEx, testH, ctt));
    }

    private static boolean test(float sc, int ms) {
        return runTest("Test metodo add di due CmdManager ", sc, ms, Grade::test_manager_add);
    }

    private static Result test_cmd_names(CT ct) {
        try {
            Command cmd = ct.get.get();
            List<String> list = cmd.getNames();
            if (!check(list, ct.names))
                return new Result("ERRORE "+list+"  !=  "+ Arrays.asList(ct.names));
        } catch (Throwable t) { return handleThrowable(t); }
        return new Result();
    }

    private static Result test_cmd_doc(CT ct) {
        try {
            Command cmd = ct.get.get();
            String d = cmd.doc();
            if (!ct.doc.equalsIgnoreCase(d))
                return new Result("ERRORE "+q(d)+"  !=  "+q(ct.doc));
        } catch (Throwable t) { return handleThrowable(t); }
        return new Result();
    }

    private static Optional<Result> test_exe(CT ct, Function<String,String> exe) {
        for (String[] aTest : ct.tests) {
            for (String name : ct.names) {
                String in = name+" "+aTest[0];
                String err = "ERRORE su stringa di comando " + q(in) + ":  ";
                try {
                    String out = exe.apply(in);
                    if (aTest[1] != null) {
                        if (!ct.check.test(aTest[1], out))
                            return Optional.of(new Result(err+q(out)+"  !=  "+q(aTest[1])));
                    } else
                        return Optional.of(new Result(err+"non lancia IllegalArgumentException"));
                } catch (IllegalArgumentException e) {
                    if (aTest[1] != null)
                        return Optional.of(new Result(err+"lancia IllegalArgumentException"));
                }
            }
        }
        return Optional.empty();
    }

    private static Optional<Result> test_exe_c(CT ct, Function<String,String> exe) {
        if (ct.cTests != null) {
            for (String[] aTest : ct.cTests) {
                for (String name : ct.names) {
                    String in = name+" "+aTest[0];
                    String err = "ERRORE su stringa di comando " + q(in);
                    try {
                        String out = exe.apply(in);
                        out = String.valueOf(out.hashCode());
                        if (!aTest[1].equals(out))
                            return Optional.of(new Result(err));
                    } catch (IllegalArgumentException e) {
                        return Optional.of(new Result(err+":  lancia IllegalArgumentException"));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private static Result test_cmd_exe(CT ct) {
        try {
            Command cmd = ct.get.get();
            Optional<Result> r = test_exe(ct, cmd::execute);
            if (r.isPresent()) return r.get();
            r = test_exe_c(ct, cmd::execute);
            if (r.isPresent()) return r.get();
        } catch (Throwable t) { return handleThrowable(t); }
        return new Result();
    }

    private static String test_h(CmdManager cm, String ch, CT...ctt) {
        String err;
        if (ch == null) {
            err = checkH(cm.commands(), ctt);
            return err != null ? "metodo commands: "+err : null;
        } else {
            err = checkH(cm.execute(ch), ctt);
            return err != null ? "stringa di comando "+ch+": "+err : null;
        }
    }

    private static Optional<Result> test_h(CmdManager cm, CT...ctt) {
        String err = test_h(cm, "h", ctt);
        if (err != null) return Optional.of(new Result(err));
        err = test_h(cm, "help", ctt);
        if (err != null) return Optional.of(new Result(err));
        for (CT c : ctt)
            for (String nm : c.names) {
                String hc = "h "+nm;
                String d = cm.execute(hc);
                if (!c.doc.equalsIgnoreCase(d))
                    return Optional.of(new Result("ERRORE stringa di comando "+q(hc)+":  "+q(d)+"  !=   "+q(c.doc)));
            }
        return Optional.empty();
    }

    private static Result test_manager(String nullEx, String argEx, boolean testH, CT...ctt) {
        try {
            CmdManager cm = new CmdManager(CT.getCommands(ctt));
            if (nullEx != null || argEx != null)
                return new Result(nullEx != null ? nullEx : argEx);
            String err = test_h(cm, null, ctt);
            if (err != null) return new Result(err);
            if (testH) {
                Optional<Result> r = test_h(cm, ctt);
                if (r.isPresent()) return r.get();
            }
            for (CT c : ctt) {
                Optional<Result> r = test_exe(c, cm::execute);
                if (r.isPresent()) return r.get();
                r = test_exe_c(c, cm::execute);
                if (r.isPresent()) return r.get();
            }
        } catch (Throwable t) {
            if ((nullEx != null && t instanceof NullPointerException) ||
                    (argEx != null && t instanceof IllegalArgumentException))
                return new Result();
            else
                return handleThrowable(t);
        }
        return new Result();
    }

    private static Result test_manager_add() {
        CT[] ctt1 = new CT[] {CT.PI, CT.SUM, CT.POW, CT.FACTORIZE};
        CT[] ctt2 = new CT[] {CT.PI};
        try {
            CmdManager cm1 = new CmdManager(CT.getCommands(ctt1));
            CmdManager cm2 = new CmdManager(CT.getCommands(ctt2));
            try {
                cm2.execute("sum 1 2");
                return new Result("ERRORE per CmdManager coi comandi "+
                        Arrays.toString(ctt2)+" metodo execute("+q("sum 1 2")+") non lancia IllegalArgumentException");
            } catch (IllegalArgumentException e) {}
            cm2.add(CmdFactory.getSum());
            Optional<Result> r = test_exe(CT.SUM, cm2::execute);
            if (r.isPresent()) return r.get();
            r = test_exe(CT.POW, cm1::execute);
            if (r.isPresent()) return r.get();
            try {
                cm1.add(CT.DUMMY.get.get());
                return new Result("ERRORE per CmdManager coi comandi "+Arrays.toString(ctt1)+
                        "add di un comando con nomi "+Arrays.toString(CT.DUMMY.names)+
                        " non lancia IllegalArgumentException");
            } catch (IllegalArgumentException e) {}
        } catch (Throwable t) { return handleThrowable(t); }
        return new Result();
    }

    private static enum CT {
        PI("pi  --> valore di Pi greco", new String[] {"pi","Pi","PI"},
                CmdFactory::getPI, Grade::checkD,
                new String[][] {{"","3.141592653589793"}, {" a",null}}, null),
        SUM("+ x y ... z  --> somma x+y+...+z", new String[] {"+","sum"},
                CmdFactory::getSum, Grade::checkD, genSum(100), null),
        POW("pow x y  --> x elevato a y", new String[] {"^","**","pow"},
                CmdFactory::getPow, Grade::checkD, genPow(100), null),
        FACTORIZE("factorize n  --> fattorizzazione di n", new String[] {"factorize"},
                CmdFactory::getFactorize, Grade::checkS, new String[][] {
                {"43464","2(3) 3 1811"},
                {"702807232234046820","2(2) 3 5 13537 371797 2327323"},
                {"57177","3(2) 6353"},
                {"11369296125","3(2) 5(3) 11(2) 17(4)"},
                {"665702","2 332851"},
                {"7544222046562688368","2(4) 7(2) 14618561 658254407"},
                {"876687","3 7 109 383"},
                {"1474748166668105642","2 691 3701 288330600731"},
                {"936272","2(4) 163 359"},
                {"823869","3(2) 91541"},
                {"-2652796429489398951",null},
                {"113517167968750","2 5(10) 7 13(2) 17(3)"},
                {"185098","2 19 4871"},
                {"213702325710721","14618561(2)"},
                {"ab",null},
                {"3723","3 17 73"},
                {"628161","3 41 5107"},
                {"-4192466368098232125",null},
                {"520468","2(2) 13 10009"},
                {"502485","3 5 139 241"},
                {"-8647327695999288945",null},
                {"-2233655005392667029",null},
                {"456435","3(4) 5 7(2) 23"},
                {"2028429820228621619","3121 96001 6770027939"},
                {"395383","395383"},
                {"972916","2(2) 7 34747"},
                {"995608","2(3) 97 1283"},
                {"6592096796362163496","2(3) 3(3) 857 1277 50261 554839"},
                {"65404","2(2) 83 197"},
                {"6155757394405202332","2(2) 7 3679969 59741937601"},
                {"80348","2(2) 53 379"},
                {"980193","3 397 823"},
                {"334284","2(2) 3 89 313"},
                {"6732801558219702163","11 19 5279 5527 1104099979"},
                {"ab",null},
                {"457632","2(5) 3(2) 7 227"},
                {"745154","2 23 97 167"},
                {"859813","101 8513"},
                {"4364407983261687926","2 353 712493 8676409447"},
                {"-1298737958944428799",null},
                {"540138","2 3 90023"},
                {"224485","5 17 19 139"},
                {"916872","2(3) 3 11 23 151"},
                {"975574","2 19 25673"},
                {"596863","596863"},
                {"1251","3(2) 139"},
                {"247314","2 3 47 877"},
                {"833449","833449"},
                {"ab",null},
                {"673560","2(3) 3(2) 5 1871"},
                {"138416","2(4) 41 211"},
                {"2002192668358590976","2(9) 22549 173423768477"},
                {"440574","2 3 97 757"},
                {"387035","5 11 31 227"},
                {"4849852518515014161","3 223 19088687 379774987"},
                {"594434","2 19 15643"},
                {"-3819053477359156802",null},
                {"981408","2(5) 3 10223"},
                {"2732983441203969650","2 5(2) 19 17047 161047 1047883"},
                {"136393","136393"},
                {"320525","5(2) 12821"},
                {"ab",null},
                {"957116","2(2) 29 37 223"},
                {"484197","3 7 23057"},
                {"236164","2(2) 17 23 151"}
        }, new String[][] {
                {"810405","1200442212"},
                {"6142871161893173903","334094665"},
                {"533985","-1242581558"},
                {"841478","-361830193"},
                {"4122819416387650808","-1616723805"},
                {"248828","-577816950"},
                {"78716","-1419079820"},
                {"7922078443940058986","-1068612879"},
                {"61882","-1904737397"},
                {"299693","-2035025085"},
                {"49361","2029220800"},
                {"632526","1108985569"},
                {"8596289388710682851","782120283"},
                {"252670","-1045241851"},
                {"780119","1627799114"},
                {"4726091792445995192","1159295211"},
                {"940448","352162423"},
                {"462992","-1583075736"},
                {"2315578024475930783","-618807143"},
                {"944738","1117567349"},
                {"372782","1032722666"}
        }),
        DUMMY("dummy  -->", new String[] {"dummy","+"}, Grade::getDummy, Grade::checkS, null, null);


        CT(String doc, String[] names, Supplier<Command> get,
           BiPredicate<String,String> check, String[][] tests, String[][] cTests) {
            this.doc = doc;
            this.names = names;
            this.get = get;
            this.check = check;
            this.tests = tests;
            this.cTests = cTests;
        }

        public final String doc;
        public final String[] names;
        public final Supplier<Command> get;
        public final BiPredicate<String,String> check;
        public final String[][] tests, cTests;

        public static Command[] getCommands(CT[] ctt) {
            Command[] cc = new Command[ctt.length];
            for (int i = 0 ; i < ctt.length ; i++)
                cc[i] = ctt[i].get.get();
            return cc;
        }
    }

    private static Command getDummy() {
        return new Command() {
            @Override
            public List<String> getNames() { return Collections.unmodifiableList(names); }
            @Override
            public String execute(String cmd) { return ""; }
            @Override
            public String doc() { return CT.DUMMY.doc; }
            private final List<String> names = new ArrayList<>(Arrays.asList(CT.DUMMY.names));
        };
    }

    private static String q(String s) { return "\"" + s + "\""; }

    private static String[][] genSum(int nt) {
        String[][] tests = new String[nt][];
        for (int i = 0 ; i < nt ; i++) {
            int na = 1 + (i % 10);
            String in = "";
            double s = 0;
            boolean ex = false;
            for (int j = 0 ; j < na ; j++) {
                double x = (j%3 == 0 ? i - j : 20*Math.cos(i + j));
                s += x;
                in += (j > 0 ? " " : "") + x;
                if (i%10 == 9 && j%4 == 2) {
                    ex = true;
                    in += ""+"abcdefghijklmnopqrstuvwxyz".charAt(j * j % 26);
                }
            }
            tests[i] = new String[] {in, (ex ? null : ""+s)};
        }
        return tests;
    }

    private static String[][] genPow(int nt) {
        String[][] tests = new String[nt][];
        for (int i = 0 ; i < nt ; i++) {
            int na = (i%20 != 19 ? 2 : (i%2 == 0 ? 1 : 3));
            String in = "";
            double s = 0;
            boolean ex = (na != 2);
            for (int j = 0 ; j < na ; j++) {
                double x = ((i+j)%3 == 0 ? i - j : 20*Math.cos(i + j));
                s = (j == 0 ? x : Math.pow(s, x));
                in += (j > 0 ? " " : "") + x;
                if (i%15 == 12 && j%2 == i%2) {
                    ex = true;
                    in += ""+"abcdefghijklmnopqrstuvwxyz".charAt(j*j % 26);
                }
            }
            tests[i] = new String[] {in, (ex ? null : ""+s)};
        }
        return tests;
    }

    private static String checkH(String h, CT[] ctt) {
        if (h == null)
            return "ERRORE comando help ritorna null";
        String[] lines = h.split("\\n");
        Set<Set<String>> corrSet = new HashSet<>();
        corrSet.add(new HashSet<>(Arrays.asList("h","help")));
        for (CT c : ctt)
            corrSet.add(new HashSet<>(Arrays.asList(c.names)));
        Set<Set<String>> hSet = new HashSet<>();
        for (String l : lines) {
            l = l.trim();
            if (l.isEmpty()) continue;
            String[] names = l.split(",");
            for (int i = 0 ; i < names.length ; i++)
                names[i] = names[i].trim();
            hSet.add(new HashSet<>(Arrays.asList(names)));
        }
        if (corrSet.equals(hSet))
            return null;
        else {
            String[][] corrArray = new String[corrSet.size()][];
            int i = 0;
            for (Set<String> s : corrSet)
                corrArray[i++] = s.toArray(new String[s.size()]);
            return "ERRORE " + q(h) + " per i nomi "+ Arrays.deepToString(corrArray);
        }
    }

    private static boolean check(List<String> list, String[] arr) {
        Set<String> setList = new HashSet<>(list);
        Set<String> arrSet = new HashSet<>(Arrays.asList(arr));
        return setList.equals(arrSet);
    }

    private static boolean checkD(String corr, String val) {
        try {
            double c = Double.parseDouble(corr);
            double v = Double.parseDouble(val);
            if (Double.isNaN(c))
                return Double.isNaN(v);
            double d = Math.abs(c - v);
            double ac = Math.abs(c);
            if (ac > 0.00001) return (d < 0.0001*ac);
            else return d < 0.000001;
        } catch (Exception e) { return false; }
    }

    private static boolean checkS(String corr, String val) {
        return corr.equals(val);
    }

    private static void print(Result r) {
        System.out.println(!r.fatal && r.err == null ? "OK" : r.err);
    }
    private static void print(String m) { System.out.print(m); }
    private static void println(String m) { print(m+"\n"); }

    private static Result handleThrowable(Throwable t) {
        String msg = "";
        boolean fatal = false;
        if (t instanceof Exception)
            msg += "Eccezione inattesa: ";
        else {
            msg += "ERRORE GRAVE, impossibile continuare il test: ";
            fatal = true;
        }
        return new Result(msg+t, fatal);
    }

    private static boolean runTest(String msg, float score, int ms, Callable<Result> test) {
        FutureTask<Result> future = new FutureTask<>(test);
        Thread t = new Thread(future);
        t.setDaemon(true);
        print(msg+" ");
        PrintStream stdOut = System.out;
        System.setOut(FAKEOUT);
        t.start();
        Result res = null;
        try {
            res = future.get(ms, TimeUnit.MILLISECONDS);
        } catch (CancellationException | InterruptedException | TimeoutException | ExecutionException e) {}
        future.cancel(true);
        System.setOut(stdOut);
        if (res == null)
            println("ERRORE: limite di tempo superato ("+ms+" ms)");
        else if (res.fatal) {
            println(res.err);
            return false;
        } else if (res.err != null) {
            println(res.err);
        } else {
            println(" score "+score);
            totalScore += score;
        }
        return true;
    }

    private static class Result {
        public final String err;
        public final boolean fatal;

        public Result(String e, boolean f) {
            err = e;
            fatal = f;
        }

        public Result() { this(null, false); }
        public Result(String e) { this(e, false); }
    }

    private static class FakeOut extends OutputStream {
        @Override
        public void write(int b) throws IOException {}
    }

    private static final PrintStream FAKEOUT = new PrintStream(new FakeOut());
    private static double totalScore;
}
