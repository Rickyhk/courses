import model.*;
import controller.*;
import repository.*;
import view.TextMenu;

import java.io.BufferedReader;
import java.util.*;

/**
 * Created by cosmin on 10/25/16.
 */
public class Main {
    public static Controller getNewController(IStmt prg) {
        /// Create the data structures for the program execution
        MyIStack<IStmt> exeStack = new MyStack<>(new Stack<IStmt>());
        MyIDictionary<String, Integer> symTable = new MyDictionary<>(new HashMap<String, Integer>());
        MyIList<Integer> out = new MyList<>(new ArrayList<Integer>());
        MyIDictionary<Integer, Tuple<String, BufferedReader>> fileTable = new MyDictionary<>(new HashMap<Integer, Tuple<String, BufferedReader>>());
        MyIHeap<Integer> heap = new MyHeap<Integer>(new HashMap<Integer, Integer>());

        PrgState prgState = new PrgState(exeStack, symTable, out, prg, fileTable, heap, 1);
        IRepository repo = new Repository(prgState, "log.txt");
        Controller ctrl = new Controller(repo);
        return ctrl;

    }
    public static void main(String [] args) {
        /*
        *   Lab2Ex1:
        *   v = 2;
        *   print (v)
        *
        * */
        IStmt lab2ex1= new CompStmt(new AssignStmt("v",new ConstExp(2)), new PrintStmt(new
                VarExp("v")));
        /*
        *   Lab2Ex2:
        *   a = 2 + 3 * 5;
        *   b = a + 1;
        *   print (b)
        *
        * */
        IStmt lab2ex2 = new CompStmt(new AssignStmt("a", new ArithExp('+',new ConstExp(2),new
                ArithExp('*',new ConstExp(3), new ConstExp(5)))),
                new CompStmt(new AssignStmt("b",new ArithExp('+',new VarExp("a"), new
                        ConstExp(1))), new PrintStmt(new VarExp("b"))));
        /*
        *   Lab2Ex3:
        *   a = 2 - 2;
        *   If a then v = 2 else v = 3;
        *   print (v)
        *
        * */
        IStmt lab2ex3 = new CompStmt(new AssignStmt("a", new ArithExp('-',new ConstExp(2), new
                ConstExp(2))),
                new CompStmt(new IfStmt(new VarExp("a"),new AssignStmt("v",new ConstExp(2)), new
                        AssignStmt("v", new ConstExp(3))), new PrintStmt(new VarExp("v"))));


        /*
        *   Lab5Ex1
        *   openRFile (var_f, "test.in");
        *   readFile (var_f, var_c); print (var_c);
        *   If var_c then readFile (var_f, var_c); print (var_c) else print (0);
        *   closeRFile (var_f)
        *
        * */
        IStmt lab5ex1 = new CompStmt(
                new OpenRFileStmt("var_f", "test.in"),
                new CompStmt(
                        new ReadFileStmt(new VarExp("var_f"), "var_c"),
                        new CompStmt(
                                new PrintStmt(new VarExp("var_c")),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("var_c"),
                                                new CompStmt(
                                                    new ReadFileStmt(new VarExp("var_f"), "var_c"),
                                                    new PrintStmt(new VarExp("var_c"))
                                                ),
                                                new PrintStmt(new ConstExp(0))
                                        ),
                                        new CloseRFileStmt(new VarExp("var_f"))
                                )
                        )
                )
        );

        /*
        *   Lab5Ex2
        *   openRFile (var_f, "test.in");
        *   readFile (var_f + 2, var_c); print (var_c);
        *   If var_c then readFile (var_f, var_c); print (var_c) else print (0);
        *   closeRFile (var_f)
        * */
        IStmt lab5ex2 = new CompStmt(
                new OpenRFileStmt("var_f", "test.in"),
                new CompStmt(
                        new ReadFileStmt(new ArithExp('+', new VarExp("var_f"), new ConstExp(2)), "var_c"),
                        new CompStmt(
                                new PrintStmt(new VarExp("var_c")),
                                new CompStmt(
                                        new IfStmt(
                                                new VarExp("var_c"),
                                                new CompStmt(
                                                    new ReadFileStmt(new VarExp("var_f"), "var_c"),
                                                    new PrintStmt(new VarExp("var_c"))
                                                ),
                                                new PrintStmt(new ConstExp(0))
                                        ),
                                        new CloseRFileStmt(new VarExp("var_f"))
                                )
                        )
                )
        );
        /**
         *v=10;new(v,20);new(a,22);wH(a,30);print(a);print(rH(a));a=0
         *
         * */
        IStmt lab6ex1 =
        new CompStmt(
                new AssignStmt("v", new ConstExp(10)),
                new CompStmt(
                    new NewStmt("v", new ConstExp(20)),
                        new CompStmt(
                            new NewStmt("a", new ConstExp(22)),
                            new CompStmt(
                                new WriteHeapStmt("a", new ConstExp(30)),
                                new CompStmt(
                                    new PrintStmt(new VarExp("a")),
                                    new CompStmt(
                                            new PrintStmt(new ReadHeapExp("a")),
                                            new AssignStmt("a", new ConstExp(0)))))
                        )
                )
        );
        IStmt lab7test = new CompStmt(
            new AssignStmt("v", new ConstExp(0)),
                new WhileStmt(new CompExp("<=", new VarExp("v"), new ConstExp(10)),
                        new CompStmt(
                                new PrintStmt(new VarExp("v")),
                                new AssignStmt("v", new ArithExp('+', new VarExp("v"), new ConstExp(1)))
                                ))
        );
        IStmt lab7ex1 = new CompStmt(
            new AssignStmt("v", new ConstExp(6)),
                new CompStmt(
                    new WhileStmt(new ArithExp('-', new VarExp("v"), new ConstExp(4)),
                            new CompStmt(
                                    new PrintStmt(new VarExp("v")),
                                    new AssignStmt("v", new ArithExp('-', new VarExp("v"), new ConstExp(1)))
                                    )),
                        new PrintStmt(new VarExp("v")))
        );
//        v=6; (while (v-4) print(v);v=v-1);print(v)
        TextMenu menu = new TextMenu(new MyDictionary<String, Command>(new HashMap<String, Command>()));
        menu.addCommand(new ExitCommand("0", "Exit"));
        menu.addCommand(new RunExample("1", lab2ex1.toString(), getNewController(lab2ex1)));
        menu.addCommand(new RunExample("2", lab2ex2.toString(), getNewController(lab2ex2)));
        menu.addCommand(new RunExample("3", lab2ex3.toString(), getNewController(lab2ex3)));
        menu.addCommand(new RunExample("4", lab5ex1.toString(), getNewController(lab5ex1)));
        menu.addCommand(new RunExample("5", lab5ex2.toString(), getNewController(lab5ex2)));
        menu.addCommand(new RunExample("6", lab6ex1.toString(), getNewController(lab6ex1)));
        menu.addCommand(new RunExample("7", lab7test.toString(), getNewController(lab7test)));
        menu.addCommand(new RunExample("8", lab7ex1.toString(), getNewController(lab7ex1)));
        menu.addCommand(new InputProgramCommand("9", "Input a program", getNewController(null)));
        menu.show();
    }
}