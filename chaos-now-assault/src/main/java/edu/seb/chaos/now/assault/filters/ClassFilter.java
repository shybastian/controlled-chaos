package edu.seb.chaos.now.assault.filters;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ClassFilter implements org.springframework.aop.ClassFilter {

    @Override
    public boolean matches(Class<?> clazz) {
        return !inPackage(clazz);
    }

    private boolean inPackage(Class<?> clazz) {
        return clazz.getName().startsWith("edu.seb.chaos.now.assault") ||
                clazz.getName().startsWith("edu.seb.chaos.now.http") ||
                clazz.getName().startsWith("edu.seb.chaos.fix");
    }
}
