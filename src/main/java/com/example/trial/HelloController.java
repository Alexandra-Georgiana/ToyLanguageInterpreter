package com.example.trial;

import com.sun.jdi.Value;
import controller.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import model.ADTs.MyHeap;
import model.ADTs.MyIHeap;
import model.ADTs.MyIList;
import model.Exceptions.MyException;
import model.Expressions.*;
import model.State.PrgState;
import model.Statements.*;
import model.Types.BoolType;
import model.Types.IntType;
import model.Types.RefType;
import model.Values.BoolValue;
import model.Values.IValue;
import model.Values.IntValue;
import model.Values.StringValue;
import repository.IRepository;
import repository.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import model.ADTs.*;

public class HelloController {

    @FXML
    private ListView<String> PrgList;

    @FXML
    private ListView<String> FileTable;

    @FXML
    private TableView<Pair<Integer,IValue>> HeapTable;

    @FXML
    private TableColumn<Pair<Integer, IValue>, Integer> HeapVar;

    @FXML
    private TableColumn<Pair<Integer, IValue>, IValue> HeapValue;

    @FXML
    private ListView<String> ListPrgStates;

    @FXML
    private TextField NrPrgStates;

    @FXML
    private TableView<Pair<String, IValue>> SymTable;

    @FXML
    private TableColumn<Pair<String, IValue>, String> SymVar;

    @FXML
    private TableColumn<Pair<String, IValue>, String> SymVal;

    @FXML
    private ListView<String> Out;

    @FXML
    private ListView<String> ExeStack;

    @FXML
    private TableView<Pair<String, String>> ProcTable;

    @FXML
    private TableView<Pair<Integer, Integer>> LockTbl;

    @FXML
    private TableView<Pair<Integer, Tuple<Integer, List<Integer>, Integer>>> ToySemTbl;

    @FXML
    private TableView<Pair<Integer, Pair<Integer, List<Integer>>>> CountSemTbl;

    @FXML
    private TableView<Pair<Integer, Integer>> LatchTbl;

    @FXML
    private TableView<Pair<Integer, Pair<Integer, List<Integer>>>> BarrierTbl;

    @FXML
    private Button RunOneStep;

    @FXML
    private Button RunAll;

    private Controller controller;
    private List<PrgState> exeList;

    @FXML
    public void initialize() {
        List<IStmt> programs = new ArrayList<>();

        IStmt ex1 = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(new AssignStmt("v", new ValueExpression(new IntValue(2))),
                        new PrintStmt(new VariableExpression("v"))));
        programs.add(ex1);

        IStmt ex2 = new CompStmt(
                new VariableDeclarationStatement("a", new IntType()),
                new CompStmt(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ArithmeticExpression(new ValueExpression(new IntValue(2)), new ArithmeticExpression(new ValueExpression(new IntValue(3)), new ValueExpression(new IntValue(5)), 3), 1)),
                                new CompStmt(
                                        new AssignStmt("b", new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(1)), 1)),
                                        new PrintStmt(new VariableExpression("b"))
                                )
                        )
                )
        );
        programs.add(ex2);

        IStmt ex3 = new CompStmt(
                new VariableDeclarationStatement("a", new BoolType()),
                new CompStmt(
                        new VariableDeclarationStatement("v", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExpression(new BoolValue(true))),
                                new CompStmt(
                                        new IfStmt(
                                                new VariableExpression("a"),
                                                new AssignStmt("v", new ValueExpression(new IntValue(2))),
                                                new AssignStmt("v", new ValueExpression(new IntValue(3)))
                                        ),
                                        new PrintStmt(new VariableExpression("v"))
                                )
                        )
                )
        );
        programs.add(ex3);

        IStmt openFile = new OpenFileStatement(new ValueExpression(new StringValue("test.in")));
        IStmt readFile = new ReadFileStatement(new ValueExpression(new StringValue("test.in")), "var");
        IStmt readFile2 = new ReadFileStatement(new ValueExpression(new StringValue("test.in")), "var2");
        IStmt printVar = new PrintStmt(new VariableExpression("var"));
        IStmt printVar2 = new PrintStmt(new VariableExpression("var2"));
        IStmt closeFile = new CloseFIleStatement(new ValueExpression(new StringValue("test.in")));
        IStmt ex4 = new CompStmt(openFile, new CompStmt(readFile, new CompStmt(printVar, new CompStmt(readFile2, new CompStmt(printVar2, closeFile)))));
        programs.add(ex4);

        IStmt ex5 = new CompStmt(
                new VariableDeclarationStatement("a", new IntType()),
                new CompStmt(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompStmt(
                                new AssignStmt("a", new ValueExpression(new IntValue(2))),
                                new CompStmt(
                                        new AssignStmt("b", new ValueExpression(new IntValue(5))),
                                        new PrintStmt(new RationalExpression(new VariableExpression("a"), new VariableExpression("b"), 4))
                                )
                        )
                )
        );
        programs.add(ex5);

        IStmt ex6 = new CompStmt(
                new VariableDeclarationStatement("v", new RefType(new IntType())), // Declare v as a RefType
                new CompStmt(
                        new VariableDeclarationStatement("a", new RefType(new IntType())), // Declare a as a RefType
                        new CompStmt(
                                new HeapAllocStmt("v", new ValueExpression(new IntValue(10))), // Allocate 10 on the heap and assign the reference to v
                                new CompStmt(
                                        new HeapAllocStmt("a", new ValueExpression(new IntValue(22))), // Allocate 22 on the heap and assign the reference to a
                                        new CompStmt(
                                                new PrintStmt(new VariableExpression("v")),
                                                new PrintStmt(new VariableExpression("a"))
                                        )
                                )
                        )
                )
        );
        programs.add(ex6);

        IStmt ex7 = new CompStmt(
                new VariableDeclarationStatement("v", new RefType(new IntType())), // Declare v as a RefType
                new CompStmt(
                        new VariableDeclarationStatement("a", new RefType(new IntType())), // Declare a as a RefType
                        new CompStmt(
                                new HeapAllocStmt("v", new ValueExpression(new IntValue(10))), // Allocate 10 on the heap and assign the reference to v
                                new CompStmt(
                                        new HeapAllocStmt("a", new ValueExpression(new IntValue(22))), // Allocate 22 on the heap and assign the reference to a
                                        new CompStmt(
                                                new HeapWriteStmt(new VariableExpression("a"), new ValueExpression(new IntValue(30))), // Write 30 to the heap location referenced by a
                                                new CompStmt(
                                                        new PrintStmt(new VariableExpression("a")), // Print the reference a
                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))) // Print the value at the heap location referenced by a
                                                )
                                        )
                                )
                        )
                )
        );

        programs.add(ex7);
        IStmt ex8 = new CompStmt(
                new VariableDeclarationStatement("v", new RefType(new IntType())), // Declare v as a RefType
                new CompStmt(
                        new HeapAllocStmt("v", new ValueExpression(new IntValue(20))), // Allocate v on the heap
                        new CompStmt(
                                new VariableDeclarationStatement("a", new RefType(new RefType(new IntType()))), // Declare a as a RefType of RefType(IntType)
                                new CompStmt(
                                        new HeapAllocStmt("a", new VariableExpression("v")), // Allocate a as a reference to v
                                        new CompStmt(
                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("a"))), // Print the value pointed by a
                                                new PrintStmt(new VariableExpression("a")) // Print the reference a
                                        )
                                )
                        )
                )
        );
        programs.add(ex8);

        IStmt ex9 = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new HeapAllocStmt("v", new ValueExpression(new IntValue(20))),
                        new CompStmt(
                                new HeapAllocStmt("a", new VariableExpression("v")),
                                new CompStmt(
                                        new HeapAllocStmt("v", new ValueExpression(new IntValue(30))),
                                        new PrintStmt(new ReadHeapExpression(new ReadHeapExpression(new VariableExpression("a"))))
                                )
                        )
                )
        );
        programs.add(ex9);

        IStmt ex10 = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExpression(new IntValue(4))),
                        new CompStmt(
                                new WhileStmt(
                                        new RationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(0)), 3),
                                        new CompStmt(
                                                new PrintStmt(new VariableExpression("v")),
                                                new AssignStmt("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), 2))
                                        )
                                ),
                                new NoOperationStatement()
                        )
                )
        );
        programs.add(ex10);

        IStmt ex11 = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExpression(new IntValue(10))),
                                new CompStmt(
                                        new HeapAllocStmt("a", new ValueExpression(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(new CompStmt(
                                                        new HeapWriteStmt(new VariableExpression("a"), new ValueExpression(new IntValue(30))),
                                                        new CompStmt(
                                                                new AssignStmt("v", new ValueExpression(new IntValue(32))),
                                                                new CompStmt(
                                                                        new PrintStmt(new VariableExpression("v")),
                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
                                                                )
                                                        )
                                                )),
                                                new CompStmt(
                                                        new PrintStmt(new VariableExpression("v")),
                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(ex11);

        IStmt exMultipleForks = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new VariableDeclarationStatement("a", new RefType(new IntType())),
                        new CompStmt(
                                new AssignStmt("v", new ValueExpression(new IntValue(10))),
                                new CompStmt(
                                        new HeapAllocStmt("a", new ValueExpression(new IntValue(22))),
                                        new CompStmt(
                                                new ForkStmt(new CompStmt(
                                                        new HeapWriteStmt(new VariableExpression("a"), new ValueExpression(new IntValue(30))),
                                                        new CompStmt(
                                                                new AssignStmt("v", new ValueExpression(new IntValue(32))),
                                                                new CompStmt(
                                                                        new PrintStmt(new VariableExpression("v")),
                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
                                                                )
                                                        )
                                                )),
                                                new CompStmt(
                                                        new ForkStmt(new CompStmt(
                                                                new HeapWriteStmt(new VariableExpression("a"), new ValueExpression(new IntValue(40))),
                                                                new CompStmt(
                                                                        new AssignStmt("v", new ValueExpression(new IntValue(42))),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VariableExpression("v")),
                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
                                                                        )
                                                                )
                                                        )),
                                                        new CompStmt(
                                                                new PrintStmt(new VariableExpression("v")),
                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(exMultipleForks);

        IStmt ex12 = new CompStmt(
                new VariableDeclarationStatement("v", new BoolType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExpression(new IntValue(2))),
                        new PrintStmt(new VariableExpression("v"))
                )
        );
        programs.add(ex12);

        IStmt simpleForStmt = new CompStmt(
                new VariableDeclarationStatement("i", new IntType()),
                new ForStmt("i", new ValueExpression(new IntValue(0)),
                        new RationalExpression(new VariableExpression("i"), new ValueExpression(new IntValue(5)), 2),
                        new ArithmeticExpression(new VariableExpression("i"), new ValueExpression(new IntValue(1)), 1),
                        new PrintStmt(new VariableExpression("i"))
                )
        );
        programs.add(simpleForStmt);

        IStmt exForFork = new CompStmt(
                new VariableDeclarationStatement("a", new RefType(new IntType())),
                new CompStmt(
                        new HeapAllocStmt("a", new ValueExpression(new IntValue(20))),
                        new CompStmt(
                                new ForStmt("v", new ValueExpression(new IntValue(0)),
                                        new RationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(3)), 1),
                                        new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), 1),
                                        new ForkStmt(new CompStmt(
                                                new PrintStmt(new VariableExpression("v")),
                                                new AssignStmt("v", new ArithmeticExpression(new VariableExpression("v"), new ReadHeapExpression(new VariableExpression("a")), 3))
                                        ))
                                ),
                                new PrintStmt(new ReadHeapExpression(new VariableExpression("a")))
                        )
                )
        );
        programs.add(exForFork);

        IStmt compoundStmt = new CompStmt(
                new VariableDeclarationStatement("b", new BoolType()),
                new CompStmt(
                        new VariableDeclarationStatement("c", new IntType()),
                        new CompStmt(
                                new AssignStmt("b", new ValueExpression(new BoolValue(true))),
                                new CompStmt(
                                        new CondAsigStmt(
                                                new VariableExpression("c"),
                                                new VariableExpression("b"),
                                                new ValueExpression(new IntValue(100)),
                                                new ValueExpression(new IntValue(200))
                                        ),
                                        new CompStmt(
                                                new PrintStmt(new VariableExpression("c")),
                                                new CompStmt(
                                                        new CondAsigStmt(
                                                                new VariableExpression("c"),
                                                                new ValueExpression(new BoolValue(false)),
                                                                new ValueExpression(new IntValue(100)),
                                                                new ValueExpression(new IntValue(200))
                                                        ),
                                                        new PrintStmt(new VariableExpression("c"))
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(compoundStmt);

        IStmt switchStmt = new CompStmt(
                new VariableDeclarationStatement("a", new IntType()),
                new CompStmt(
                        new VariableDeclarationStatement("b", new IntType()),
                        new CompStmt(
                                new VariableDeclarationStatement("c", new IntType()),
                                new CompStmt(
                                        new AssignStmt("a", new ValueExpression(new IntValue(1))),
                                        new CompStmt(
                                                new AssignStmt("b", new ValueExpression(new IntValue(2))),
                                                new CompStmt(
                                                        new AssignStmt("c", new ValueExpression(new IntValue(5))),
                                                        new CompStmt(
                                                                new SwitchStmt(
                                                                        new ArithmeticExpression(new VariableExpression("a"), new ValueExpression(new IntValue(10)), 3),
                                                                        new ArithmeticExpression(new VariableExpression("b"), new VariableExpression("c"), 3),
                                                                        new CompStmt(
                                                                                new PrintStmt(new VariableExpression("a")),
                                                                                new PrintStmt(new VariableExpression("b"))
                                                                        ),
                                                                        new ValueExpression(new IntValue(10)),
                                                                        new CompStmt(
                                                                                new PrintStmt(new ValueExpression(new IntValue(100))),
                                                                                new PrintStmt(new ValueExpression(new IntValue(200)))
                                                                        ),
                                                                        new PrintStmt(new ValueExpression(new IntValue(300)))
                                                                ),
                                                                new PrintStmt(new ValueExpression(new IntValue(300)))
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(switchStmt);

        IStmt waitStmt = new CompStmt(
                new CompStmt(new VariableDeclarationStatement( "v", new IntType()),
                        new AssignStmt("v", new ValueExpression(new IntValue(20)))),
                new CompStmt(
                        new CompStmt (new PrintStmt(new VariableExpression("v")),
                        new WaitStmt(10)),
                        new PrintStmt(new ArithmeticExpression(
                                new VariableExpression("v"),
                                new ValueExpression(new IntValue(10)),
                                3 // Multiplication operation
                        ))
                )
        );
        programs.add(waitStmt);

        IStmt repUntilStmt = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExpression(new IntValue(0))),
                        new CompStmt(
                                new DoWhileStmt(
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new PrintStmt(new VariableExpression("v")),
                                                                new AssignStmt("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), 2))
                                                        )
                                                ),
                                                new AssignStmt("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), 1))
                                        ),
                                        new RationalExpression(new VariableExpression("v"), new ValueExpression(new IntValue(3)), 5)
                                ),
                                new CompStmt(
                                        new VariableDeclarationStatement("x", new IntType()),
                                        new CompStmt(
                                                new VariableDeclarationStatement("y", new IntType()),
                                                new CompStmt(
                                                        new VariableDeclarationStatement("z", new IntType()),
                                                        new CompStmt(
                                                                new VariableDeclarationStatement("w", new IntType()),
                                                                new CompStmt(
                                                                        new AssignStmt("x", new ValueExpression(new IntValue(1))),
                                                                        new CompStmt(
                                                                                new AssignStmt("y", new ValueExpression(new IntValue(2))),
                                                                                new CompStmt(
                                                                                        new AssignStmt("z", new ValueExpression(new IntValue(3))),
                                                                                        new CompStmt(
                                                                                                new AssignStmt("w", new ValueExpression(new IntValue(4))),
                                                                                                new PrintStmt(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), 3))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(repUntilStmt);

        IStmt sleepStmt = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ValueExpression(new IntValue(10))),
                        new CompStmt(
                                new ForkStmt(
                                        new CompStmt(
                                                new AssignStmt("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), 2)),
                                                new CompStmt(
                                                        new AssignStmt("v", new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(1)), 2)),
                                                        new PrintStmt(new VariableExpression("v"))
                                                )
                                        )
                                ),
                                new CompStmt(
                                        new SleepStmt(10),
                                        new PrintStmt(new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), 3))
                                )
                        )
                )
        );
        programs.add(sleepStmt);

        IStmt exProcCall = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new VariableDeclarationStatement("w", new IntType()),
                        new CompStmt(
                                new AssignStmt("v", new ValueExpression(new IntValue(2))),
                                new CompStmt(
                                        new AssignStmt("w", new ValueExpression(new IntValue(5))),
                                        new CompStmt(
                                                new CallProcStmt("sum", new ArrayList<>(Arrays.asList(
                                                        new ArithmeticExpression(new VariableExpression("v"), new ValueExpression(new IntValue(10)), 3),
                                                        new VariableExpression("w")
                                                ))),
                                                new CompStmt(
                                                        new PrintStmt(new VariableExpression("v")),
                                                        new ForkStmt(
                                                                new CompStmt(
                                                                        new CallProcStmt("product", new ArrayList<>(Arrays.asList(
                                                                                new VariableExpression("v"),
                                                                                new VariableExpression("w")
                                                                        ))),
                                                                        new ForkStmt(
                                                                                new CallProcStmt("sum", new ArrayList<>(Arrays.asList(
                                                                                        new VariableExpression("v"),
                                                                                        new VariableExpression("w")
                                                                                )))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(exProcCall);
        IStmt program = new CompStmt(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStmt(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompStmt(
                                new HeapAllocStmt("v1", new ValueExpression(new IntValue(20))),
                                new CompStmt(
                                        new HeapAllocStmt("v2", new ValueExpression(new IntValue(30))),
                                        new CompStmt(
                                                new NewLock("x"),
                                                new CompStmt(
                                                        new ForkStmt(
                                                                new CompStmt(
                                                                        new ForkStmt(
                                                                                new CompStmt(
                                                                                        new Lock("x"),
                                                                                        new CompStmt(
                                                                                                new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), 2)),
                                                                                                new Unlock("x")
                                                                                        )
                                                                                )
                                                                        ),
                                                                        new CompStmt(
                                                                                new Lock("x"),
                                                                                new CompStmt(
                                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), 1)),
                                                                                        new Unlock("x")
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompStmt(
                                                                new ForkStmt(
                                                                        new CompStmt(
                                                                                new ForkStmt(
                                                                                        new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(1)), 1))
                                                                                ),
                                                                                new CompStmt(
                                                                                        new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(1)), 1)),
                                                                                        new Unlock("x")
                                                                                )
                                                                        )
                                                                ),
                                                                new CompStmt(
                                                                        new NoOperationStatement(),
                                                                        new CompStmt(
                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v2")))
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(program);

        IStmt toySemTest = new CompStmt(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStmt(
                        new VariableDeclarationStatement("cnt", new IntType()),
                        new CompStmt(
                                new HeapAllocStmt("v1", new ValueExpression(new IntValue(2))),
                                new CompStmt(
                                        new NewToySemaphore("cnt", new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1))),
                                        new CompStmt(
                                                new ForkStmt(
                                                        new CompStmt(
                                                                new ToyAcquireStmt("cnt"),
                                                                new CompStmt(
                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3)),
                                                                        new CompStmt(
                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                new ToyReleaseStmt("cnt")
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new CompStmt(
                                                        new ForkStmt(
                                                                new CompStmt(
                                                                        new ToyAcquireStmt("cnt"),
                                                                        new CompStmt(
                                                                                new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3)),
                                                                                new CompStmt(
                                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)), 3)),
                                                                                        new CompStmt(
                                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                new ToyReleaseStmt("cnt")
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        ),
                                                        new CompStmt(
                                                                new ToyAcquireStmt("cnt"),
                                                                new CompStmt(
                                                                        new PrintStmt(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), 2)),
                                                                        new ToyReleaseStmt("cnt")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(toySemTest);

        IStmt countSemTest = new CompStmt(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStmt(
                        new VariableDeclarationStatement("cnt", new IntType()),
                        new CompStmt(
                                new HeapAllocStmt("v1", new ValueExpression(new IntValue(1))),
                                new CompStmt(
                                        new CreateCountSemaphoreStmt("cnt", new ReadHeapExpression(new VariableExpression("v1"))),
                                        new CompStmt(
                                                new ForkStmt(new CompStmt(
                                                        new CountAcquireStmt("cnt"),
                                                        new CompStmt(
                                                                new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3)),
                                                                new CompStmt(
                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                        new CountReleaseStmt("cnt")
                                                                )
                                                        )
                                                )),
                                                new CompStmt(
                                                        new ForkStmt(new CompStmt(
                                                                new CountAcquireStmt("cnt"),
                                                                new CompStmt(
                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3)),
                                                                        new CompStmt(
                                                                                new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(2)), 3)),
                                                                                new CompStmt(
                                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                        new CountReleaseStmt("cnt")
                                                                                )
                                                                        )
                                                                )
                                                        )),
                                                        new CompStmt(
                                                                new CountAcquireStmt("cnt"),
                                                                new CompStmt(
                                                                        new PrintStmt(new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(1)), 2)),
                                                                        new CountReleaseStmt("cnt")
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(countSemTest);

        IStmt latchTest = new CompStmt(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStmt(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompStmt(
                                new VariableDeclarationStatement("v3", new RefType(new IntType())),
                                new CompStmt(
                                        new VariableDeclarationStatement("cnt", new IntType()),
                                        new CompStmt(
                                                new HeapAllocStmt("v1", new ValueExpression(new IntValue(2))),
                                                new CompStmt(
                                                        new HeapAllocStmt("v2", new ValueExpression(new IntValue(3))),
                                                        new CompStmt(
                                                                new HeapAllocStmt("v3", new ValueExpression(new IntValue(4))),
                                                                new CompStmt(
                                                                        new NewLatchStmt("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                        new CompStmt(
                                                                                new ForkStmt(new CompStmt(
                                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v1")), new ValueExpression(new IntValue(10)), 3)),
                                                                                        new CompStmt(
                                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                new CountDownLatchStmt("cnt")
                                                                                        )
                                                                                )),
                                                                                new CompStmt(
                                                                                        new ForkStmt(new CompStmt(
                                                                                                new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v2")), new ValueExpression(new IntValue(10)), 3)),
                                                                                                new CompStmt(
                                                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                        new CountDownLatchStmt("cnt")
                                                                                                )
                                                                                        )),
                                                                                        new CompStmt(
                                                                                                new ForkStmt(new CompStmt(
                                                                                                        new HeapWriteStmt(new VariableExpression("v3"), new ArithmeticExpression(new ReadHeapExpression(new VariableExpression("v3")), new ValueExpression(new IntValue(10)), 3)),
                                                                                                        new CompStmt(
                                                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v3"))),
                                                                                                                new CountDownLatchStmt("cnt")
                                                                                                        )
                                                                                                )),
                                                                                                new CompStmt(
                                                                                                        new AwaitLatchStmt("cnt"),
                                                                                                        new CompStmt(
                                                                                                                new PrintStmt(new ValueExpression(new IntValue(100))),
                                                                                                                new CompStmt(
                                                                                                                        new CountDownLatchStmt("cnt"),
                                                                                                                        new PrintStmt(new ValueExpression(new IntValue(100)))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(latchTest);

        IStmt barrierTest = new CompStmt(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStmt(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompStmt(
                                new VariableDeclarationStatement("v3", new RefType(new IntType())),
                                new CompStmt(
                                        new VariableDeclarationStatement("cnt", new IntType()),
                                        new CompStmt(
                                                new HeapAllocStmt("v1", new ValueExpression(new IntValue(2))),
                                                new CompStmt(
                                                        new HeapAllocStmt("v2", new ValueExpression(new IntValue(3))),
                                                        new CompStmt(
                                                                new HeapAllocStmt("v3", new ValueExpression(new IntValue(4))),
                                                                new CompStmt(
                                                                        new NewBarrier("cnt", new ReadHeapExpression(new VariableExpression("v2"))),
                                                                        new CompStmt(
                                                                                new ForkStmt(
                                                                                        new CompStmt(
                                                                                                new AwaitBarrierStmt(new VariableExpression("cnt")),
                                                                                                new CompStmt(
                                                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(
                                                                                                                new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                                3
                                                                                                        )),
                                                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("v1")))
                                                                                                )
                                                                                        )
                                                                                ),
                                                                                new CompStmt(
                                                                                        new ForkStmt(
                                                                                                new CompStmt(
                                                                                                        new AwaitBarrierStmt(new VariableExpression("cnt")),
                                                                                                        new CompStmt(
                                                                                                                new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(
                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                        new ValueExpression(new IntValue(10)),
                                                                                                                        3
                                                                                                                )),
                                                                                                                new CompStmt(
                                                                                                                        new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(
                                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                                                3
                                                                                                                        )),
                                                                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("v2")))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompStmt(
                                                                                                new AwaitBarrierStmt(new VariableExpression("cnt")),
                                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v3")))
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );
        programs.add(barrierTest);

        IStmt lock = new CompStmt(
                new VariableDeclarationStatement("v1", new RefType(new IntType())),
                new CompStmt(
                        new VariableDeclarationStatement("v2", new RefType(new IntType())),
                        new CompStmt(
                                new VariableDeclarationStatement("x", new IntType()),
                                new CompStmt(
                                        new VariableDeclarationStatement("q", new IntType()),
                                        new CompStmt(
                                                new HeapAllocStmt("v1", new ValueExpression(new IntValue(20))),
                                                new CompStmt(
                                                        new HeapAllocStmt("v2", new ValueExpression(new IntValue(30))),
                                                        new CompStmt(
                                                                new NewLock("x"),
                                                                new CompStmt(
                                                                        new ForkStmt(
                                                                                new CompStmt(
                                                                                        new ForkStmt(
                                                                                                new CompStmt(
                                                                                                        new Lock("x"),
                                                                                                        new CompStmt(
                                                                                                                new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(
                                                                                                                        new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                        new ValueExpression(new IntValue(1)),
                                                                                                                        2 // Subtraction
                                                                                                                )),
                                                                                                                new Unlock("x")
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompStmt(
                                                                                                new Lock("x"),
                                                                                                new CompStmt(
                                                                                                        new HeapWriteStmt(new VariableExpression("v1"), new ArithmeticExpression(
                                                                                                                new ReadHeapExpression(new VariableExpression("v1")),
                                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                                3 // Multiplication
                                                                                                        )),
                                                                                                        new Unlock("x")
                                                                                                )
                                                                                        )
                                                                                )
                                                                        ),
                                                                        new CompStmt(
                                                                                new NewLock("q"),
                                                                                new CompStmt(
                                                                                        new ForkStmt(
                                                                                                new CompStmt(
                                                                                                        new ForkStmt(
                                                                                                                new CompStmt(
                                                                                                                        new Lock("q"),
                                                                                                                        new CompStmt(
                                                                                                                                new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(
                                                                                                                                        new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                        new ValueExpression(new IntValue(5)),
                                                                                                                                        1 // Addition
                                                                                                                                )),
                                                                                                                                new Unlock("q")
                                                                                                                        )
                                                                                                                )
                                                                                                        ),
                                                                                                        new CompStmt(
                                                                                                                new Lock("q"),
                                                                                                                new CompStmt(
                                                                                                                        new HeapWriteStmt(new VariableExpression("v2"), new ArithmeticExpression(
                                                                                                                                new ReadHeapExpression(new VariableExpression("v2")),
                                                                                                                                new ValueExpression(new IntValue(10)),
                                                                                                                                3 // Multiplication
                                                                                                                        )),
                                                                                                                        new Unlock("q")
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        ),
                                                                                        new CompStmt(
                                                                                                new NoOperationStatement(),
                                                                                                new CompStmt(
                                                                                                        new NoOperationStatement(),
                                                                                                        new CompStmt(
                                                                                                                new NoOperationStatement(),
                                                                                                                new CompStmt(
                                                                                                                        new NoOperationStatement(),
                                                                                                                        new CompStmt(
                                                                                                                                new Lock("x"),
                                                                                                                                new CompStmt(
                                                                                                                                        new PrintStmt(new ReadHeapExpression(new VariableExpression("v1"))),
                                                                                                                                        new CompStmt(
                                                                                                                                                new Unlock("x"),
                                                                                                                                                new CompStmt(
                                                                                                                                                        new Lock("q"),
                                                                                                                                                        new CompStmt(
                                                                                                                                                                new PrintStmt(new ReadHeapExpression(new VariableExpression("v2"))),
                                                                                                                                                                new Unlock("q")
                                                                                                                                                        )
                                                                                                                                                )
                                                                                                                                        )
                                                                                                                                )
                                                                                                                        )
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        );

        programs.add(lock);

        ObservableList<String> items = FXCollections.observableArrayList();

        for (IStmt prg : programs) {
            Integer index = programs.indexOf(prg) + 1;
            String prgf = index + ". " + prg.toString();
            items.add(prgf);
        }

        PrgList.setItems(items);

        PrgList.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                    setBackground(null);
                } else {
                    setText(item);
                    setTextFill(Color.BLACK); // Set text color to white
                    if (isSelected()) {
                        setBackground(new Background(new BackgroundFill(Color.web("#A9A9A9"), CornerRadii.EMPTY, null))); // Selected background color
                    } else {
                        setBackground(new Background(new BackgroundFill(Color.web("#0000"), CornerRadii.EMPTY, null))); // Non-selected background color
                    }
                }
            }
        });

        PrgList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("Selected program: " + newValue);

                Integer index = Integer.parseInt(newValue.split("\\.")[0]) - 1;
                IStmt selectedProgram = programs.get(index);
                MyProcTbl procTbl = new MyProcTbl( new HashMap<>());
                addProcedures(procTbl);

                PrgState prgState = new PrgState(selectedProgram, procTbl);
                exeList = new ArrayList<>();
                exeList.add(prgState);
                IRepository repo = new Repository(exeList, "log.txt");
                controller = new Controller(repo);

            }
        });

        ListPrgStates.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateHeap();
                populateSymbolTable();
                populateExecutionStack();
                populateProcTbl();
                populateFileTable();
                populateLockTbl();
            }
        });

        RunOneStep.setOnAction(actionEvent -> {
            if (controller == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            boolean programStateLeft = Objects.requireNonNull(getCurrentProgramState()).getExcutionStack().isEmpty();
            if (programStateLeft) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program has finished", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            try{
                controller.oneStepForAllPrg(exeList);
                populate();
            }catch (InterruptedException | MyException | IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was interrupted", ButtonType.OK);
                alert.showAndWait();
            }
        });

        RunAll.setOnAction(actionEvent -> {
            if (controller == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was not selected", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            boolean programStateLeft = Objects.requireNonNull(getCurrentProgramState()).getExcutionStack().isEmpty();
            if (programStateLeft) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program has finished", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            try{
                controller.allStep();
                populate();
            }catch (InterruptedException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was interrupted", ButtonType.OK);
                alert.showAndWait();
            } catch (MyException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program has finished", ButtonType.OK);
                alert.showAndWait();
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The program was interrupted", ButtonType.OK);
                alert.showAndWait();
            }
        });

        SymTable.getColumns().add(SymVar);  // Add the variable name column
        SymTable.getColumns().add(SymVal);  // Add the value column

    }

    private PrgState getCurrentProgramState() {
        List<PrgState> prgList = controller.getRepo().getPrgList();

        if (prgList == null || prgList.isEmpty()) {
            System.err.println("Error: Program state list is empty or null.");
            return null;
        }

        int currentIndex = ListPrgStates.getSelectionModel().getSelectedIndex();

        if (currentIndex == -1 || currentIndex >= prgList.size()) {
            System.out.println("Warning: Invalid index selected. Defaulting to the first program state.");
            return prgList.get(0);
        }

        return prgList.get(currentIndex);
    }

    private void addProcedures(MyIProcTbl procTbl){
        IStmt proc1 = new CompStmt(
                new VariableDeclarationStatement("x", new IntType()),
                new CompStmt(
                        new AssignStmt("x", new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), 1)),
                        new PrintStmt(new VariableExpression("x"))
                )
        );
        List<String> varSum = new ArrayList<>();
        varSum.add("a");
        varSum.add("b");
        procTbl.update("sum", new Pair<>(varSum, proc1));

        IStmt proc2 = new CompStmt(
                new VariableDeclarationStatement("v", new IntType()),
                new CompStmt(
                        new AssignStmt("v", new ArithmeticExpression(new VariableExpression("a"), new VariableExpression("b"), 3)),
                        new PrintStmt(new VariableExpression("v"))
                )
        );
        List<String> varProd = new ArrayList<>();
        varProd.add("a");
        varProd.add("b");
        procTbl.update("product", new Pair<>(varProd, proc2));

    }


    private void populate(){
        populateProgramStateIdentifiers();
        populateFileTable();
        populateOutput();
        populateSymbolTable();
        populateExecutionStack();
        populateHeap();
        populateNoPrgStates();
        populateProcTbl();
        populateLockTbl();
        populateToySemaphoreTbl();
        populateCountSemaphoreTable();
        populateLatchTbl();
    }

    private void populateProcTbl() {
        PrgState state = getCurrentProgramState();
        if (state == null) {
            System.err.println("Error: Current program state is null.");
            return;
        }

        MyIProcTbl procTbl = state.getProcTbl();
        if (procTbl == null) {
            System.err.println("Error: Procedure table is null.");
            return;
        }

        List<Pair<String, String>> procList = new ArrayList<>();
        for (Map.Entry<String, Pair<List<String>, IStmt>> entry : procTbl.getContent().entrySet()) {
            String procNameWithParams = entry.getKey() + entry.getValue().getKey().toString();
            String procBody = entry.getValue().getValue().toString();
            procList.add(new Pair<>(procNameWithParams, procBody));
        }

        ProcTable.setItems(FXCollections.observableList(procList));
        ProcTable.refresh();

        // Clear existing columns
        ProcTable.getColumns().clear();

        // Define the columns for the ProcTable
        TableColumn<Pair<String, String>, String> procNameColumn = new TableColumn<>("ProcName");
        procNameColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<Pair<String, String>, String> procBodyColumn = new TableColumn<>("Body");
        procBodyColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Add the columns to the table
        ProcTable.getColumns().add(procNameColumn);
        ProcTable.getColumns().add(procBodyColumn);

        // Set the column resize policy to fit the content
        ProcTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void populateLockTbl() {
        PrgState state = getCurrentProgramState();
        if (state == null) {
            System.err.println("Error: Current program state is null.");
            return;
        }

        MyILockTbl lockTbl = state.getLockTbl();
        if (lockTbl == null) {
            System.err.println("Error: Lock table is null.");
            return;
        }

        List<Pair<Integer, Integer>> lockList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : lockTbl.getContent().entrySet()) {
            lockList.add(new Pair<>(entry.getKey(), entry.getValue()));
        }

        LockTbl.setItems(FXCollections.observableList(lockList));
        LockTbl.refresh();

        // Clear existing columns
        LockTbl.getColumns().clear();

        // Define the columns for the LockTable
        TableColumn<Pair<Integer, Integer>, Integer> lockIndexColumn = new TableColumn<>("Index");
        lockIndexColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<Pair<Integer, Integer>, Integer> lockValueColumn = new TableColumn<>("Value");
        lockValueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Add the columns to the table
        LockTbl.getColumns().add(lockIndexColumn);
        LockTbl.getColumns().add(lockValueColumn);

        // Set the column resize policy to fit the content
        LockTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void populateHeap() {
        PrgState state = getCurrentProgramState();  // Get the current program state
        MyIHeap heap;
        if (controller.getRepo().getPrgList().size() > 0) {
            heap = controller.getRepo().getPrgList().get(0).getHeap();
        } else {
            heap = new MyHeap();  // Fallback to an empty heap
        }

        // Initialize the list to hold heap data as Pair<Integer, IValue>
        List<Pair<Integer, IValue>> heapList = new ArrayList<>();

        // Iterate through the heap content
        for (Map.Entry<Integer, IValue> entry : heap.getHeap().getContent().entrySet()) {
            IValue value = entry.getValue(); // Get the IValue from the heap

            // Optionally handle specific types of IValue if needed (not always necessary)
            if (value instanceof Value) {
                heapList.add(new Pair<>(entry.getKey(), value)); // Add the pair
            } else {
                heapList.add(new Pair<>(entry.getKey(), value));  // Add the raw IValue if needed
            }
        }

        // Set the items of the HeapTable with the observable list of heap pairs
        HeapTable.setItems(FXCollections.observableList(heapList));
        HeapTable.refresh();

        // Define the columns for the HeapTable
        TableColumn<Pair<Integer, IValue>, Integer> varColumn = new TableColumn<>("Variable Name");
        varColumn.setCellValueFactory(new PropertyValueFactory<>("key"));  // Bind to the key (variable name)

        TableColumn<Pair<Integer, IValue>, IValue> valColumn = new TableColumn<>("Value");
        valColumn.setCellValueFactory(new PropertyValueFactory<>("value"));  // Bind to the value

        // Optionally, handle the display of IValue types (such as using toString)
        valColumn.setCellFactory(cell -> new TableCell<Pair<Integer, IValue>, IValue>() {
            @Override
            protected void updateItem(IValue item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());  // Display the string representation of IValue
                }
            }
        });

        // Add the columns to the table
        HeapTable.getColumns().clear();  // Clear any existing columns
        HeapTable.getColumns().add(varColumn);
        HeapTable.getColumns().add(valColumn);

        // Set the column resize policy to fit the content
        HeapTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FileTable.setCellFactory(listView -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setBackground(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setBackground(new Background(new BackgroundFill(Color.web("#3a393b"), CornerRadii.EMPTY, null)));
                }
            }
        });
    }
    private void populateProgramStateIdentifiers() {
        List<PrgState> programStates = controller.getRepo().getPrgList();
        List<String> programStateIdentifiers = programStates.stream()
                .map(PrgState::getId)
                .map(String::valueOf)
                .collect(Collectors.toList());

        ListPrgStates.setItems(FXCollections.observableList(programStateIdentifiers));
    }

    private void populateFileTable() {
        List<String> files = new ArrayList<>();
        if (controller != null && !controller.getRepo().getPrgList().isEmpty()) {
            PrgState currentState = controller.getRepo().getPrgList().get(0);
            if (currentState != null && currentState.getFileTable() != null) {
                for (Map.Entry<String, BufferedReader> entry : currentState.getFileTable().getContent().entrySet()) {
                    files.add(entry.getKey());
                }
            }
        }
        FileTable.setItems(FXCollections.observableArrayList(files));
        FileTable.refresh();
    }

    private void populateOutput() {
        MyIList<IValue> output;
        if (controller.getRepo().getPrgList().size() > 0) {
            output = controller.getRepo().getPrgList().get(0).getOut();
        } else {
            output = new MyList<>();
        }

        List<String> outputAsString = output.getList().stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        Out.setItems(FXCollections.observableList(outputAsString));
        Out.refresh();
    }

    private void populateToySemaphoreTbl(){
        PrgState state = getCurrentProgramState();
        List<Pair<Integer, Tuple<Integer, List<Integer>, Integer>>> toySemTblList= new ArrayList<>();
        if(state != null){
            for(Map.Entry<Integer, Tuple<Integer, List<Integer>, Integer>> entry : state.getToySemaphoreTbl().getContent().entrySet()){
                toySemTblList.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        ToySemTbl.setItems(FXCollections.observableList(toySemTblList));
        ToySemTbl.refresh();

        TableColumn<Pair<Integer, Tuple<Integer, List<Integer>, Integer>>, Integer> indexColumn = new TableColumn<>("Index");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<Pair<Integer, Tuple<Integer, List<Integer>, Integer>>, Tuple<Integer, List<Integer>, Integer>> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        ToySemTbl.getColumns().clear();
        ToySemTbl.getColumns().add(indexColumn);
        ToySemTbl.getColumns().add(valueColumn);
    }

    private void populateCountSemaphoreTable(){
        PrgState state = getCurrentProgramState();
        List<Pair<Integer, Pair<Integer, List<Integer>>>> countSemTblList = new ArrayList<>();
        if(state != null){
            for (Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : state.getCountSemaphoreTbl().getContent().entrySet()){
                countSemTblList.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        CountSemTbl.setItems(FXCollections.observableList(countSemTblList));
        CountSemTbl.refresh();

        TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Integer> indexColumn = new TableColumn<>("Index");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Pair<Integer, List<Integer>>> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        CountSemTbl.getColumns().clear();
        CountSemTbl.getColumns().add(indexColumn);
        CountSemTbl.getColumns().add(valueColumn);
    }

    public void populateLatchTbl(){
        PrgState state = getCurrentProgramState();
        List<Pair<Integer, Integer>> latchTblList = new ArrayList<>();
        if(state != null){
            for(Map.Entry<Integer, Integer> entry : state.getLatchPopulate().entrySet()){
                latchTblList.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        LatchTbl.setItems(FXCollections.observableList(latchTblList));
        LatchTbl.refresh();

        TableColumn<Pair<Integer, Integer>, Integer> indexColumn = new TableColumn<>("Index");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<Pair<Integer, Integer>, Integer> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        LatchTbl.getColumns().clear();
        LatchTbl.getColumns().add(indexColumn);
        LatchTbl.getColumns().add(valueColumn);
        LatchTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    public void populateBarrierTbl(){
        PrgState state = getCurrentProgramState();
        List<Pair<Integer, Pair<Integer, List<Integer>>>> barrierTblList = new ArrayList<>();

        if(state != null){
            for(Map.Entry<Integer, Pair<Integer, List<Integer>>> entry : state.getBarrierTable().getContent().entrySet()){
                barrierTblList.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        BarrierTbl.setItems(FXCollections.observableList(barrierTblList));
        BarrierTbl.refresh();

        TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Integer> indexColumn = new TableColumn<>("Index");
        indexColumn.setCellValueFactory(new PropertyValueFactory<>("key"));

        TableColumn<Pair<Integer, Pair<Integer, List<Integer>>>, Pair<Integer, List<Integer>>> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        BarrierTbl.getColumns().clear();
        BarrierTbl.getColumns().add(indexColumn);
        BarrierTbl.getColumns().add(valueColumn);

        BarrierTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }

    private void populateSymbolTable() {
        PrgState state = getCurrentProgramState();  // Get the current program state
        List<Pair<String, IValue>> symbolTableList = new ArrayList<>();  // List of pairs (variable name, value)

        // If there is a valid program state, populate the symbol table list
        if (state != null) {
            for (Map.Entry<String, IValue> entry : state.getSymTable().getStack().peek().getContent().entrySet()) {
                symbolTableList.add(new Pair<>(entry.getKey(), entry.getValue()));
            }
        }

        // Set the items for the table using the observable list of pairs
        SymTable.setItems(FXCollections.observableList(symbolTableList));
        SymTable.refresh();  // Refresh the table view

        // Define the "Variable Name" column
        TableColumn<Pair<String, IValue>, String> varColumn = new TableColumn<>("Variable Name");
        varColumn.setCellValueFactory(new PropertyValueFactory<>("key"));  // Bind to the key (variable name)

        // Define the "Value" column
        TableColumn<Pair<String, IValue>, IValue> valColumn = new TableColumn<>("Value");
        valColumn.setCellValueFactory(new PropertyValueFactory<>("value"));  // Bind to the value

        // Optionally, handle how to display the value in the Value column
        valColumn.setCellFactory(cell -> new TableCell<Pair<String, IValue>, IValue>() {
            @Override
            protected void updateItem(IValue item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                }
            }
        });

        // Add the columns to the table
        SymTable.getColumns().clear();  // Clear any existing columns
        SymTable.getColumns().add(varColumn);  // Add the variable name column
        SymTable.getColumns().add(valColumn);  // Add the value column

        // Set the column resize policy to fit the content
        SymTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void populateExecutionStack() {
        if (ExeStack != null) {
            List<String> stackItems = new ArrayList<>(); // Initialize stackItems
            PrgState currentState = getCurrentProgramState();
            if (currentState != null) {
                stackItems = currentState.getExcutionStack().getStack().stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
            ExeStack.setItems(FXCollections.observableList(stackItems));
            ExeStack.refresh();
        } else {
            System.err.println("exeStack ListView is not initialized.");
        }
    }

    private void populateNoPrgStates() {
        NrPrgStates.setText(String.valueOf(controller.getRepo().getPrgList().size()));
    }



}