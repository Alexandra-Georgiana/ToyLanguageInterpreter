package repository;

import model.Exceptions.MyException;
import model.State.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements IRepository {
    private List<PrgState> prgList;
    private final String logFilePath;

    public Repository(List<PrgState> prgList, String logFilePath) {
        this.prgList = new ArrayList<>(prgList);
        this.logFilePath = logFilePath;
    }

    @Override
    public List<PrgState> getPrgList() {
        return prgList;
    }

    @Override
    public void setPrgList(List<PrgState> prgList) {
        this.prgList = new ArrayList<>(prgList);
    }

    @Override
    public void logProgramState(PrgState prgState) throws MyException {
        if (this.logFilePath != null) {
            try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
                logFile.println(prgState.toString());
            } catch (IOException e) {
                throw new MyException("Error logging program state: " + e.getMessage());
            }
        }
    }
}