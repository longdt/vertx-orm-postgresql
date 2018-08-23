package longdt.repository.query;

public abstract class SingleQuery<E> implements Query<E> {
    protected String fieldName;

    public SingleQuery(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
