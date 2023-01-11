package tn.vortrix5.wizard.command;

import java.lang.reflect.Method;

import tn.vortrix5.wizard.command.Command.ExecutorType;

public final class SimpleCommand {
    private final String name;
    private final String description;
    private final ExecutorType executorType;
    private final Object object;
    private final Method method;

    public SimpleCommand(String name, String description, ExecutorType executorType, Object object, Method method) {
        this.name = name;
        this.description = description;
        this.executorType = executorType;
        this.object = object;
        this.method = method;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    public Object getObject() {
        return this.object;
    }

    public Method getMethod() {
        return this.method;
    }
}
