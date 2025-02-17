package controller;

import model.ADTs.MyDictionary;
import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Statements.ForkStmt;
import model.Statements.IStmt;
import model.Types.IType;
import repository.IRepository;
import repository.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Controller{
    public IRepository repo;
    private ExecutorService executor;


    public Controller(IRepository repo) {
        this.repo = repo;
        this.executor = Executors.newFixedThreadPool(1);

    }

    public void runTypeChecker() throws MyException {
        for (PrgState state: repo.getPrgList()) {
            MyIDictionary<String, IType> typeTable = new MyDictionary<>();
            state.getExeStack().peek().typecheck(typeTable);
        }
    }


//    public PrgState executeOneStep(PrgState state) throws MyException, FileNotFoundException {
//        MyIStack<IStmt> stk = state.getExeStack();
//        if (stk.isEmpty()) {
//            throw new MyException("Program state stack is empty");
//        }
//        IStmt currentStmt = stk.pop();
//        return currentStmt.execute(state);
//    }

    public List<PrgState> removeCompletedPrg(List<PrgState> inPrgList) {
        return inPrgList.stream()
                .filter(PrgState::isNotCompleted)
                .collect(Collectors.toList());
    }

    public void oneStepForAllPrg(List<PrgState> prgList) throws InterruptedException, MyException, IOException {
        // Log the program states before executing steps
        prgList.forEach(prg -> {
            try {
                repo.logProgramState(prg);
                System.out.println("Before Step: " + prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        // Create a list of callables for parallel execution, but handle fork separately
        List<Callable<PrgState>> callList = new ArrayList<>();

        for (PrgState prg : prgList) {
            // Check if the program is ready for a step
            if (!prg.getExeStack().isEmpty()) {
                IStmt currentStmt = prg.getExeStack().peek();

                // If the statement is a fork, handle in parallel, else sequentially
                if (currentStmt instanceof ForkStmt) {
                    // Fork - execute in parallel
                    callList.add(() -> prg.oneStep());
                } else {
                    // Non-fork operations - execute sequentially
                    prg.oneStep();  // Ensure the step is executed before moving to the next one
                }
            }
        }

        // Execute parallel steps (only fork statements)
        List<PrgState> newPrgList = executor.invokeAll(callList).stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        // Handle exceptions during execution
                        return null;
                    }
                })
                .filter(p -> p != null)
                .collect(Collectors.toList());

        // Add the new program states after the parallel execution
        prgList.addAll(newPrgList);

        // Log the program states after executing steps
        prgList.forEach(prg -> {
            try {
                repo.logProgramState(prg);
                System.out.println("After Step: " + prg);
            } catch (MyException e) {
                throw new RuntimeException(e);
            }
        });

        // Update the repository with the latest program states
        repo.setPrgList(prgList);
    }


    public List<PrgState> getProgramStates(){
        return repo.getPrgList();
    }

    public void allStep() throws InterruptedException, MyException, IOException {
        runTypeChecker();
        executor = Executors.newFixedThreadPool(2);
        List<PrgState> prgList = removeCompletedPrg(repo.getPrgList());
        while (!prgList.isEmpty()) {
            oneStepForAllPrg(prgList);
            prgList = removeCompletedPrg(repo.getPrgList());
        }
        executor.shutdownNow();
        repo.setPrgList(prgList);
    }

    public Repository getRepo() {
        return (Repository) repo;
    }
}
