package longdt.vertxorm.repository.query;

public abstract class SingleQuery<E> extends AbstractQuery<E> {
    protected String fieldName;

    public SingleQuery(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
