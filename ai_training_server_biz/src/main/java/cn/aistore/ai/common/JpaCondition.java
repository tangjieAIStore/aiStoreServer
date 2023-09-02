package cn.aistore.ai.common;


import lombok.Builder;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@Builder
public class JpaCondition<T> {

    private ExampleMatcher exampleMatcher;
    private T t;

    private ExampleMatcher getExampleMatcher() {
        if (exampleMatcher == null) {
            exampleMatcher = ExampleMatcher.matching();
        }
        return exampleMatcher;
    }

    public JpaCondition addLike(String property) {
        exampleMatcher = getExampleMatcher().withMatcher(property, ExampleMatcher.GenericPropertyMatchers.contains());
        return this;
    }

    public JpaCondition addEqual(String property) {
        exampleMatcher = getExampleMatcher().withMatcher(property, ExampleMatcher.GenericPropertyMatchers.exact());
        return this;
    }

    public Example build(T t) {
        this.t = t;
        return Example.of(t, exampleMatcher);
    }

    /**
     * 用于构建用户查询条件
     * @param t
     * @return
     */
    public Example<T> buildUser(T t) {
        if (exampleMatcher == null) {
            exampleMatcher = getExampleMatcher().withMatcher("userId", ExampleMatcher.GenericPropertyMatchers.exact());
            exampleMatcher = exampleMatcher.withMatcher("tenantId", ExampleMatcher.GenericPropertyMatchers.exact());
        }
        this.t = t;
        return Example.of(t, exampleMatcher);
    }

    /**
     * 判断是否有条件
     * @return
     */
    public Boolean isHaveCondition() {
        return exampleMatcher != null;
    }
}
