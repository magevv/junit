package com.encode.app;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class RestartFailedRule implements TestRule {

    @Override
    public Statement apply(Statement statement, Description description) {
        return new RestartFailedStatement(statement, description);
    }

    public class RestartFailedStatement extends Statement {

        private Statement base;
        private Description desc;

        public RestartFailedStatement(Statement base, Description desc) {
            this.base = base;
            this.desc = desc;
        }

        @Override
        public void evaluate() throws Throwable {
            Unstable unstable = desc.getAnnotation(Unstable.class);
            if (unstable != null) {
                for (int i = 1; i < unstable.value(); i++) {
                    try {
                        base.evaluate();
                        break;
                    } catch (Throwable t) {
                        System.out.println("Failed and restarted: " + desc);
                        if (i == unstable.value() - 1) {
                            base.evaluate();
                        }
                    }
                }
            } else base.evaluate();
        }
    }
}
